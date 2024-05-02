/*
 * Copyright [2022-2024] [Xebisco]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.editor.app.editor;

import com.xebisco.yield.editor.app.Global;
import com.xebisco.yield.editor.app.Project;
import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.props.Prop;
import com.xebisco.yield.uiutils.props.PropPanel;
import com.xebisco.yield.utils.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ScenePanel extends JPanel {
    private final GameView gameView;
    private final EditorScene scene;
    private final Project project;
    private EntitiesTree entitiesTree;
    private final JSplitPane mainP;

    public ScenePanel(EditorScene scene, Project project) {
        super(new BorderLayout());
        this.scene = scene;
        this.project = project;
        entitiesTree = new EntitiesTree();
        entitiesTree.tree.update();
        entitiesTree.setMinimumSize(new Dimension(200, 300));
        gameView = new GameView(this, entitiesTree);
        mainP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameView, null);
        mainP.setResizeWeight(1);
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {

            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                if (saveTimer != null) saveTimer.stop();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        });
        add(mainP);
    }

    private Timer saveTimer;

    public void openEntity(EditorEntity entity, File toSaveFile) {
        AtomicReference<File> saveFile = new AtomicReference<>(toSaveFile);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setMinimumSize(new Dimension(440, 440));

        Integer divLoc = mainP.getDividerLocation();
        if (mainP.getRightComponent() == null) divLoc = null;
        mainP.setRightComponent(panel);
        if (divLoc != null)
            mainP.setDividerLocation(divLoc);

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel(entity.entityName());
        title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(28f));
        if (toSaveFile != null) {
            //TODO indicate prefab
        } else {
        }

        title.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (toSaveFile != null) {
                    //TODO indicate prefab
                } else {
                }
                title.setText(entity.entityName());
                title.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                String name = JOptionPane.showInputDialog(panel, "Entity Name");
                if (name == null || name.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "misc_operationCanceled");
                } else {
                    entity.setEntityName(name);
                    if (toSaveFile != null) {
                        if (JOptionPane.showConfirmDialog(panel, "Change file name also?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            saveFile.get().renameTo(new File(saveFile.get().getParent(), name + ".yepf"));
                            saveFile.set(new File(saveFile.get().getParent(), name + ".yepf"));
                        }
                    }
                }
            }
        });
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(title);

        JPanel optionsPanel = new JPanel();
        JCheckBox checkBox = new JCheckBox("Enabled");
        checkBox.setSelected(entity.enabled());
        checkBox.addChangeListener(e -> {
            entity.setEnabled(checkBox.isSelected());
        });
        checkBox.setEnabled(toSaveFile == null);
        if (toSaveFile != null) {
            title.setForeground(new Color(0, 100, 100));
            title.setBorder(BorderFactory.createTitledBorder("PREFAB FILE"));
        }
        optionsPanel.add(checkBox);

        JScrollPane scrollPane = new JScrollPane();
        List<ComponentProp> props = new ArrayList<>();


        header.add(optionsPanel, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        reloadFields(props, entity, scrollPane);

        panel.add(scrollPane);

        if (saveTimer != null) {
            saveTimer.stop();
            saveTimer = null;
        }
        Font defaultFont;
        try {
            defaultFont = Font.createFont(Font.TRUETYPE_FONT, new File(project.path(), "default-font.ttf")).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        saveTimer = new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!entityExists(entity)) {
                    mainP.setRightComponent(null);
                    saveTimer.stop();
                    saveTimer = null;
                    return;
                }
                entity.setDefaultFont(defaultFont);
                saveSceneEntity(entity, true);
                if (toSaveFile != null) {
                    try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(saveFile.get()))) {
                        oo.writeObject(entity);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        saveTimer.start();


        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton(new AbstractAction(Srd.LANG.getProperty("ce_addComponent")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addComponentDialog = new JDialog();
                addComponentDialog.addWindowFocusListener(new WindowAdapter() {
                    @Override
                    public void windowLostFocus(WindowEvent e) {
                        addComponentDialog.dispose();
                    }
                });
                addComponentDialog.setTitle(Srd.LANG.getProperty("ce_addComponent"));
                addComponentDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addComponentDialog.setSize(200, 200);
                addComponentDialog.setLocationRelativeTo(panel);
                JList<Class<?>> components;
                try {
                    components = new JList<>(getComponentClasses(Global.yieldEngineClassLoader, "com.xebisco.yield").toArray(new Class<?>[0]));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                components.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && components.getSelectedValue() != null) {
                            entity.components().add(new EditorComponent(components.getSelectedValue()));
                            addComponentDialog.dispose();
                            reloadFields(props, entity, scrollPane);
                        }
                    }
                });
                components.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        l.setText(((Class<?>) value).getSimpleName());
                        l.setHorizontalAlignment(CENTER);
                        l.setFont(l.getFont().deriveFont(Font.BOLD));
                        try {
                            l.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(ComponentProp.class.getResource("/logo/codeIcon.png"))).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        return l;
                    }
                });
                addComponentDialog.add(new JScrollPane(components));
                addComponentDialog.setVisible(true);
            }
        });
        buttonPanel.add(addButton);

        if (entity.prefabFile() != null) buttonPanel.add(new JButton(new AbstractAction("Reload") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(panel, "This will remove any custom configurations, Proceed?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
                EditorEntity loaded;
                try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(entity.prefabFile()))) {
                    loaded = (EditorEntity) oi.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AtomicReference<EditorComponent> transform = new AtomicReference<>();

                if (JOptionPane.showConfirmDialog(panel, "Override tranformation properties", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    entity.components().forEach(c -> {
                        if (c.className().equals("com.xebisco.yield.Transform2D")) transform.set(c);
                    });
                }

                entity.components().clear();
                for (EditorComponent component : loaded.components()) {
                    entity.components().add(component);
                }

                if (transform.get() != null) {
                    entity.components().removeIf(c -> c.className().equals("com.xebisco.yield.Transform2D"));
                    entity.components().addFirst(transform.get());
                }
                reloadFields(props, entity, scrollPane);
            }
        }));

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.getRootPane().setDefaultButton(addButton);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void saveSceneEntity(EditorEntity entity, boolean transformSave) {
        entity.props.forEach(c -> ((ComponentProp) c).saveValues());
        if (transformSave) {
            float[] pos = new float[2];
            entity.props.forEach(c -> {
                if (((ComponentProp) c).name().equals("com.xebisco.yield.Transform2D")) {
                    ((ComponentProp) c).props.forEach(c1 -> {
                        if (c1.name().equals("position")) {
                            pos[0] = ((Point2D.Float) c1.value()).x;
                            pos[1] = ((Point2D.Float) c1.value()).y;
                        }
                    });
                }
            });
            entity.setTransformPosition(pos[0], pos[1]);
        }
        updateUI();
    }

    public static List<Class<?>> getComponentClasses(ClassLoader cl, String pack) throws Exception {
        List<String> classNames = getClassNames();

        List<Class<?>> ret = new ArrayList<>();
        for (String className : classNames) {
            if (className.startsWith(pack)) {
                Class<?> c = cl.loadClass(className);
                if (!Modifier.isAbstract(c.getModifiers()) && cl.loadClass("com.xebisco.yield.ComponentBehavior").isAssignableFrom(c) && !c.isAnnotationPresent(Global.HIDE_ANNOTATION))
                    ret.add(c);
            }
        }

        return ret;
    }

    private static List<String> getClassNames() throws IOException {
        List<String> classNames = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(Global.yieldEngineJar.openStream());
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
        }
        return classNames;
    }

    private void reloadFields(List<ComponentProp> props, EditorEntity entity, JScrollPane scrollPane) {
        int v = scrollPane.getVerticalScrollBar().getValue();
        props.clear();
        for (EditorComponent comp : entity.components()) {
            props.add(new ComponentProp(comp, () -> {
                int i = entity.components().indexOf(comp);
                if (i <= 1) return;
                entity.components().remove(comp);
                entity.components().add(i - 1, comp);
            }, () -> {
                int i = entity.components().indexOf(comp);
                if (i >= entity.components().size()) return;
                entity.components().remove(comp);
                entity.components().add(i + 1, comp);
            }, () -> {
                entity.components().remove(comp);
            }, () -> {
                reloadFields(props, entity, scrollPane);
            }));
        }
        entity.props = Arrays.asList(props.toArray());
        PropPanel p = new PropPanel(props.toArray(new Prop[0]));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        p.setPreferredSize(new Dimension(scrollPane.getWidth() - scrollPane.getVerticalScrollBar().getWidth(), p.getPreferredSize().height));
        scrollPane.setViewportView(p);
        scrollPane.getVerticalScrollBar().setValue(v);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        entitiesTree.tree.update();
    }


    public boolean entityExists(EditorEntity entity) {
        if (scene == null) return false;
        if (scene.entities().contains(entity)) return true;
        for (EditorEntity e : scene.entities()) {
            if (entityExists(entity, e)) return true;
        }
        return false;
    }

    private boolean entityExists(EditorEntity entity, EditorEntity parent) {
        if (parent.children().contains(entity)) return true;
        for (EditorEntity e : parent.children()) {
            if (entityExists(entity, e)) return true;
        }
        return false;
    }

    private JMenu addMenu(List<EditorEntity> entities, EditorEntity parent, Point2D.Double mousePosition) {
        JMenu sceneAddMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_scene_add"));

        sceneAddMenu.add(new AbstractAction(Srd.LANG.getProperty("se_menuBar_scene_add_emptyEntity")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<EditorEntity> entities1 = entities;
                if (entities1 == null)
                    entities1 = scene.entities();
                EditorEntity ee = new EditorEntity().setParent(parent);
                if (mousePosition != null) ee.setPosition(mousePosition.x, mousePosition.y);
                entities1.add(ee);
                openEntity(ee, null);
                entitiesTree.tree.update();
            }
        });

        sceneAddMenu.addSeparator();

        sceneAddMenu.add(new AbstractAction(Srd.LANG.getProperty("se_menuBar_scene_add_fromPrefab")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(project.path(), "Prefabs"));
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().endsWith(".yepf");
                    }

                    @Override
                    public String getDescription() {
                        return "Yield Entity Prefab (.yepf)";
                    }
                });
                if (fileChooser.showOpenDialog(ScenePanel.this) == JFileChooser.APPROVE_OPTION) {
                    EditorEntity prefab;
                    try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                        prefab = (EditorEntity) oi.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    prefab.setPrefabFile(fileChooser.getSelectedFile());
                    List<EditorEntity> entities1 = entities;
                    if (entities1 == null)
                        entities1 = scene.entities();
                    entities1.add(prefab.setParent(parent));
                    openEntity(prefab, fileChooser.getSelectedFile());
                    entitiesTree.tree.update();
                }
            }
        });
        return sceneAddMenu;
    }

    public void entityPopupPanel(Component invoker, EditorEntity entity) {
        entityPopupPanel(invoker, new EditorEntity[]{entity});
    }

    public void entityPopupPanel(Component invoker, EditorEntity[] entities) {
        if (entities.length == 0) return;
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JLabel(Arrays.toString(entities)));
        popupMenu.addSeparator();
        if (entities.length == 1) {
            popupMenu.add(addMenu(entities[0].children(), entities[0], null));
            popupMenu.add(new AbstractAction("Open") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openEntity(entities[0], null);
                }
            });
        }
        popupMenu.add(new AbstractAction("Duplicate") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CompletableFuture.runAsync(() -> {
                    for (EditorEntity en : entities) {
                        EditorEntity n = new EditorEntity().setEntityName(en.entityName() + " (Clone)").setParent(en.parent()).setPrefabFile(en.prefabFile());
                        n.components().clear();
                        for (EditorComponent c : en.components())
                            n.components().add(c.sameAs());
                        if (en.parent() == null)
                            scene.entities().add(n);
                        else en.parent().children().add(n);
                    }
                    entitiesTree.tree.update();
                });
            }
        });
        popupMenu.add(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CompletableFuture.runAsync(() -> {
                    for (EditorEntity en : entities) {
                        if (en.parent() != null) en.parent().children().remove(en);
                        else
                            scene.entities().remove(en);
                    }
                    if (!entityExists(gameView.selectedEntity())) gameView.setSelectedEntity(null);
                    gameView.repaint();
                    entitiesTree.tree.update();
                });
            }
        });
        popupMenu.show(invoker, invoker.getMousePosition().x, invoker.getMousePosition().y);
    }

    public void rootEntityPopupPanel(Component invoker, Point2D.Double mousePosition) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JLabel("Scene Root"));
        popupMenu.addSeparator();
        popupMenu.add(addMenu(scene.entities(), null, mousePosition));
        popupMenu.show(invoker, invoker.getMousePosition().x, invoker.getMousePosition().y);
    }

    class EntitiesTree extends JPanel {

        final Tree tree;

        public EntitiesTree() {
            super(new BorderLayout());
            add(tree = new Tree());
            JToolBar toolBar = new JToolBar();
            toolBar.add(new JButton(new AbstractAction("Reload") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tree.update();
                }
            }));
            add(toolBar, BorderLayout.NORTH);
        }

        class Tree extends JTree {

            private EditorEntity[] es() {
                if (getSelectionPaths() == null || getSelectionPaths().length == 0) return new EditorEntity[0];
                EditorEntity[] entities = new EditorEntity[getSelectionPaths().length];
                for (int i = 0; i < entities.length; i++) {
                    entities[i] = ((EntityNode) ((DefaultMutableTreeNode) getSelectionPaths()[i].getLastPathComponent()).getUserObject()).editorEntity;
                }
                return entities;
            }

            public Tree() {
                setShowsRootHandles(true);

                addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            try {
                                entityPopupPanel(Tree.this, es());
                            } catch (ClassCastException ex) {
                                rootEntityPopupPanel(Tree.this, null);
                            }
                        }
                    }
                });

                addTreeSelectionListener(e -> {
                    if (getSelectionPaths() != null && getSelectionPaths().length == 1) try {
                        gameView.setSelectedEntity(es()[0]);
                        gameView.repaint();
                    } catch (ClassCastException ignore) {
                    }
                });
            }

            public void update() {
                java.util.List<Integer> expanded = new ArrayList<>();

                for (int i = 0; i < entitiesTree.tree.getRowCount(); i++) {
                    if (tree.isExpanded(i)) {
                        expanded.add(i);
                    }
                }
                DefaultMutableTreeNode root = new DefaultMutableTreeNode("Scene Root");
                CreateChildNodes ccn = new CreateChildNodes();
                ccn.createChildren(scene.entities(), root);
                DefaultTreeModel treeModel = new DefaultTreeModel(root);
                setModel(treeModel);
                expandRow(0);
                revalidate();
                updateUI();

                expanded.forEach(tree::expandRow);
            }

            public static class CreateChildNodes {

                private void createChildren(EditorEntity entity, DefaultMutableTreeNode node) {
                    java.util.List<EditorEntity> entities = entity.children();
                    if (entities.isEmpty()) return;

                    for (EditorEntity e : entities) {
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new EntityNode(e));
                        node.add(childNode);
                        createChildren(e, childNode);
                    }
                }

                private void createChildren(List<EditorEntity> entities, DefaultMutableTreeNode node) {
                    if (entities.isEmpty()) return;

                    for (EditorEntity e : entities) {
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new EntityNode(e));
                        node.add(childNode);
                        createChildren(e, childNode);
                    }
                }

            }

            public record EntityNode(EditorEntity editorEntity) {

                @Override
                public String toString() {
                    return editorEntity.entityName();
                }
            }
        }
    }

    public GameView gameView() {
        return gameView;
    }

    public EditorScene scene() {
        return scene;
    }

    public EntitiesTree entitiesTree() {
        return entitiesTree;
    }

    public ScenePanel setEntitiesTree(EntitiesTree entitiesTree) {
        this.entitiesTree = entitiesTree;
        return this;
    }
}