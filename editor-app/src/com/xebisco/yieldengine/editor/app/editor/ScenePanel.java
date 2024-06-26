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

package com.xebisco.yieldengine.editor.app.editor;

import com.xebisco.yieldengine.editor.app.DialogUtils;
import com.xebisco.yieldengine.editor.app.Global;
import com.xebisco.yieldengine.editor.app.Project;
import com.xebisco.yieldengine.editor.app.TreeTransferHandler;
import com.xebisco.yieldengine.editor.runtime.pack.EditorComponent;
import com.xebisco.yieldengine.editor.runtime.pack.EditorEntity;
import com.xebisco.yieldengine.editor.runtime.pack.EditorScene;
import com.xebisco.yieldengine.uiutils.Srd;
import com.xebisco.yieldengine.uiutils.props.Prop;
import com.xebisco.yieldengine.uiutils.props.PropPanel;

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
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ScenePanel extends JPanel {
    private final GameView gameView;
    private final EditorScene scene;
    private final Project project;
    private EntitiesTree entitiesTree;
    public final JSplitPane mainP;
    public final ActionsHandler scenePanelAH;

    private final Editor editor;

    public ScenePanel(EditorScene scene, Project project, Editor editor) {
        super(new BorderLayout());
        this.scene = scene;
        this.project = project;
        this.editor = editor;
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

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BorderLayout());
        toolBar.setFloatable(false);
        toolBar.setRollover(false);

        toolBar.setBackground(getBackground());

        JMenu viewMenu = new JMenu("View");


        JCheckBox checkBox = new JCheckBox("Origin", true);
        checkBox.addChangeListener(e -> {
            gameView.showOrigin = checkBox.isSelected();
            gameView.repaint();
        });

        viewMenu.add(checkBox);


        JMenu gridMenu = new JMenu("Grid");

        ButtonGroup group = new ButtonGroup();

        AbstractAction gridChange = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameView.gridState = GridState.valueOf(e.getActionCommand());
                gameView.repaint();
            }
        };

        JRadioButton radioButton = new JRadioButton("Simple");

        radioButton.addActionListener(gridChange);
        radioButton.setActionCommand("SIMPLE");
        radioButton.setSelected(true);
        group.add(radioButton);
        gridMenu.add(radioButton);

        radioButton = new JRadioButton("Advanced");

        radioButton.addActionListener(gridChange);
        radioButton.setActionCommand("ADVANCED");
        group.add(radioButton);
        gridMenu.add(radioButton);

        radioButton = new JRadioButton("Disabled");
        radioButton.addActionListener(gridChange);
        radioButton.setActionCommand("DISABLED");
        group.add(radioButton);
        gridMenu.add(radioButton);

        gridMenu.addSeparator();

        JSlider slider = new JSlider(10, 255, 20);
        slider.addChangeListener(e -> {
            gameView.gridOpacity = slider.getValue();
            gameView.repaint();
        });
        gridMenu.add(slider);

        viewMenu.add(gridMenu);

        JMenu toolsMenu = new JMenu("Tools");

        JComboBox<Tools> comboBox = new JComboBox<>(Tools.values());
        comboBox.addItemListener(e -> {
            gameView.tool = (Tools) comboBox.getSelectedItem();
        });

        toolBar.add(scenePanelAH = new ActionsHandler(new Component[]{viewMenu, toolsMenu, comboBox}));

        add(toolBar, BorderLayout.NORTH);
    }

    private Timer saveTimer;

    public void closeEntity() {
        mainP.setRightComponent(null);
        if (saveTimer != null) {
            saveTimer.stop();
            saveTimer = null;
        }
        updateUI();
    }

    public void openEntity(EditorEntity entity, File toSaveFile) {
        AtomicReference<File> saveFile = new AtomicReference<>(toSaveFile);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setMinimumSize(new Dimension(440, 440));

        Integer divLoc = mainP.getDividerLocation();
        if (mainP.getRightComponent() == null) divLoc = null;
        mainP.setRightComponent(panel);
        if (divLoc != null) mainP.setDividerLocation(divLoc);

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

        panel.addHierarchyListener(e -> reloadFields(props, entity, scrollPane));

        panel.add(scrollPane);

        if (saveTimer != null) {
            saveTimer.stop();
            saveTimer = null;
        }
        try {
            Global.defaultFont = Font.createFont(Font.TRUETYPE_FONT, new File(project.path(), "Assets/default-font.ttf")).deriveFont(32f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        saveTimer = new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!entityExists(entity)) {
                    closeEntity();
                    return;
                }
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
                Class<?> comp = DialogUtils.addComponent(editor);

                if(comp != null) {
                    entity.components().add(Global.addFields(comp, editor, new EditorComponent(comp.getName())));
                    reloadFields(props, entity, scrollPane);
                }
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
                    DialogUtils.error(panel, ex);
                    return;
                }

                AtomicReference<EditorComponent> transform = new AtomicReference<>();

                if (JOptionPane.showConfirmDialog(panel, "Override tranformation properties", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    entity.components().forEach(c -> {
                        if (c.className().equals("com.xebisco.yieldengine.Transform2D")) transform.set(c);
                    });
                }

                entity.components().clear();
                for (EditorComponent component : loaded.components()) {
                    entity.components().add(component);
                }

                if (transform.get() != null) {
                    entity.components().removeIf(c -> c.className().equals("com.xebisco.yieldengine.Transform2D"));
                    entity.components().add(0, transform.get());
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
                if (((ComponentProp) c).name().equals("com.xebisco.yieldengine.Transform2D")) {
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

    public static List<Class<?>> getComponentClasses(ClassLoader cl, String pack, Editor editor) throws Exception {
        List<String> classNames = getClassNames(editor);

        List<Class<?>> ret = new ArrayList<>();
        for (String className : classNames) {
            if (className.startsWith(pack)) {
                Class<?> c = cl.loadClass(className);
                if (!Modifier.isAbstract(c.getModifiers()) && cl.loadClass("com.xebisco.yieldengine.ComponentBehavior").isAssignableFrom(c) && !c.isAnnotationPresent(editor.HIDE_ANNOTATION))
                    ret.add(c);
            }
        }

        return ret;
    }

    private static List<String> getClassNames(Editor editor) throws IOException {
        List<String> classNames = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(editor.yieldEngineJar.openStream());
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
            try {
                Global.updateFields(editor.projectClass(comp.className()), editor, comp.fields());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
            }, editor));
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
                List<EditorEntity> entities1;
                if (entities == null) entities1 = scene.entities();
                else {
                    entities1 = entities;
                }
                EditorEntity ee = Global.clearComponents(new EditorEntity(), editor).setParent(parent);
                scenePanelAH.push(new ActionsHandler.ActionHA("Create Entity", () -> {
                    if (mousePosition != null) Global.setPosition(mousePosition.x, mousePosition.y, ee);
                    entities1.add(ee);
                    openEntity(ee, null);
                    entitiesTree.tree.update();
                }, () -> {
                    entities1.remove(ee);
                    entitiesTree.tree.update();
                }));
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
                    if (entities1 == null) entities1 = scene.entities();
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
                SwingUtilities.invokeLater(() -> {
                    for (EditorEntity en : entities) {
                        EditorEntity n = Global.clearComponents(new EditorEntity(), editor).setEntityName(en.entityName() + " (Clone)").setParent(en.parent()).setPrefabFile(en.prefabFile());
                        n.components().clear();
                        for (EditorComponent c : en.components())
                            n.components().add(Global.sameAs(c, editor));

                        List<EditorEntity> entities1;
                        if (en.parent() == null) entities1 = scene.entities();
                        else entities1 = en.parent().children();
                        scenePanelAH.push(new ActionsHandler.ActionHA("Duplicate Entity(" + en.entityName() + ")", () -> {
                            entities1.add(n);
                            entitiesTree.tree.update();
                        }, () -> {
                            entities1.remove(n);
                            entitiesTree.tree.update();
                        }));
                    }
                });
            }
        });
        popupMenu.add(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    for (EditorEntity en : entities) {

                        List<EditorEntity> entities1;

                        if (en.parent() != null) entities1 = en.parent().children();
                        else entities1 = scene.entities();

                        scenePanelAH.push(new ActionsHandler.ActionHA("Delete Entity(" + en.entityName() + ")", () -> {
                            entities1.remove(en);
                        }, () -> {
                            entities1.add(en);
                        }));
                    }
                    //if (!entityExists(gameView.selectedEntity())) gameView.setSelectedEntity(null);

                    gameView.repaint();
                    entitiesTree.tree.update();
                });
            }
        });
        popupMenu.show(invoker, invoker.getMousePosition().x, invoker.getMousePosition().y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!entityExists(gameView.selectedEntity())) gameView.setSelectedEntity(null);
    }

    public void rootEntityPopupPanel(Component invoker, Point2D.Double mousePosition) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JLabel("Scene Root"));
        popupMenu.addSeparator();
        popupMenu.add(addMenu(scene.entities(), null, mousePosition));
        popupMenu.show(invoker, invoker.getMousePosition().x, invoker.getMousePosition().y);
    }

    public class EntitiesTree extends JPanel {

        final Tree tree;

        public EntitiesTree() {
            super(new BorderLayout());
            add(tree = new Tree(scene));
            tree.setDragEnabled(true);
            tree.setDropMode(DropMode.ON_OR_INSERT);
            tree.setTransferHandler(new TreeTransferHandler());
            JToolBar toolBar = new JToolBar();
            toolBar.add(new JButton(new AbstractAction("Reload") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tree.update();
                }
            }));
            add(toolBar, BorderLayout.NORTH);
        }

        public class Tree extends JTree {
            public final EditorScene scene;

            private EditorEntity[] es() throws ClassCastException {
                if (getSelectionPaths() == null || getSelectionPaths().length == 0) return new EditorEntity[0];
                EditorEntity[] entities = new EditorEntity[getSelectionPaths().length];
                for (int i = 0; i < entities.length; i++) {
                    entities[i] = ((EntityNode) ((DefaultMutableTreeNode) getSelectionPaths()[i].getLastPathComponent()).getUserObject()).editorEntity;
                }
                return entities;
            }

            public Tree(EditorScene scene) {
                this.scene = scene;
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
                    try {
                        EditorEntity[] es = es();
                        int selected = getMinSelectionRow();
                        if (selected == -1 || gameView.selectedEntity() == es[0]) return;

                        if (es.length == 1 && getSelectionPaths() != null && getSelectionPaths().length == 1)
                            gameView.setSelectedEntity(es[0]);
                        EditorEntity en = es[0];
                        SwingUtilities.invokeLater(() -> {
                            openEntity(en, null);
                            Tree.this.requestFocus();
                            setSelectionRow(selected);
                        });
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

    public Project project() {
        return project;
    }

    public JSplitPane mainP() {
        return mainP;
    }

    public ActionsHandler scenePanelAH() {
        return scenePanelAH;
    }

    public Editor editor() {
        return editor;
    }

    public Timer saveTimer() {
        return saveTimer;
    }

    public ScenePanel setSaveTimer(Timer saveTimer) {
        this.saveTimer = saveTimer;
        return this;
    }
}
