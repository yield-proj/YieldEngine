/*
 * Copyright [2022-2023] [Xebisco]
 *
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

package com.xebisco.yield.editor.explorer;

import com.xebisco.yield.editor.*;
import com.xebisco.yield.editor.code.CodePanel;
import com.xebisco.yield.editor.prop.*;
import com.xebisco.yield.editor.scene.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Explorer extends JPanel implements ActionListener {
    private JTextArea jta;
    private JTree tree;
    private JButton refresh;
    private JTable jtb;
    private JScrollPane jsp;

    private final SceneExplorer sceneExplorer;

    File currDirectory = null;

    final String[] colHeads = {"File Name", "SIZE(in Bytes)", "Read Only", "Hidden"};
    String[][] data = {{"", "", "", "", ""}};

    private final File mainDir;

    private final Workspace workspace;

    public Explorer(File mainDir, String mainName, SceneExplorer sceneExplorer, Workspace workspace) {
        this.mainDir = mainDir;
        this.sceneExplorer = sceneExplorer;
        this.workspace = workspace;

        setLayout(new BorderLayout());

        jta = new JTextArea(5, 30);
        refresh = new JButton(Assets.images.get("reloadIcon.png"));
        JButton newFolder = new JButton(UIManager.getIcon("Tree.closedIcon"));

        JToolBar p = new JToolBar();
        p.add(new JLabel(mainName, UIManager.getIcon("FileChooser.homeFolderIcon"), SwingConstants.CENTER));
        p.add(refresh);
        add(p, BorderLayout.NORTH);

        actionPerformed(null);

        refresh.addActionListener(this);
    }


    public static String getDefaultScript(String name) {
        return "import com.xebisco.yield.*;\n\npublic class " + name + " extends ComponentBehavior {\n\n\t//This method is called before the first update of the entity\n\t@Override\n\tpublic void onStart() {\n\t\t\n\t}\n\n\t//This method is called every frame\n\t@Override\n\tpublic void onUpdate() {\n\t\t\n\t}\n\n}";
    }

    public static String getEmptyScript(String name) {
        return "import com.xebisco.yield.*;\n\npublic class " + name + " extends ComponentBehavior {\n\n}";
    }

    public static File newScript(File dir) {
        AtomicReference<File> toReturn = new AtomicReference<>();
        Map<String, Prop[]> props = new HashMap<>();
        props.put("new_script", new Prop[]{new StringProp("script_name", ""), new BooleanProp("create_default_script_methods", true)});
        new PropsWindow(props, () -> {
            if (Objects.requireNonNull(Props.get(props.get("new_script"), "script_name")).getValue().equals("")) {
                JOptionPane.showMessageDialog(null, "Script needs a name");
            } else {
                File f = new File(dir, Objects.requireNonNull(Props.get(props.get("new_script"), "script_name")).getValue() + ".java");
                toReturn.set(f);
                try {
                    f.createNewFile();

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                        if ((boolean) Objects.requireNonNull(Props.get(props.get("new_script"), "create_default_script_methods")).getValue()) {
                            writer.append(getDefaultScript(f.getName().split("\\.java")[0]));
                        } else {
                            writer.append(getEmptyScript(f.getName().split("\\.java")[0]));
                        }
                    } catch (IOException ex) {
                        Utils.error(null, ex);
                    }
                } catch (IOException ex) {
                    Utils.error(null, ex);
                }

            }
        }, null, "new_script");
        return toReturn.get();
    }

    public void actionPerformed(ActionEvent ev) {
        DefaultMutableTreeNode newtop = createTree(mainDir);
        if (newtop != null) {
            List<Integer> expanded = getExpanded();
            tree = new JTree(newtop);
            tree.setDragEnabled(true);
            tree.setDropMode(DropMode.ON_OR_INSERT);
            tree.setTransferHandler(new TreeFileTransferHandler());
            tree.getSelectionModel().setSelectionMode(
                    TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
            expanded(expanded);
            tree.setCellRenderer(new ExplorerCellRenderer(mainDir));
        }
        if (jsp != null)
            remove(jsp);
        jsp = new JScrollPane(tree);
        add(jsp, BorderLayout.CENTER);
        tree.addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON3) {
                            tree.setSelectionPath(tree.getPathForLocation(me.getX(), me.getY()));
                            JPopupMenu popupMenu = new JPopupMenu();

                            List<JMenuItem> items = popupT();
                            for (JMenuItem item : items)
                                popupMenu.add(item);

                            popupMenu.show(Explorer.this, me.getX() + 2, me.getY() + 42);
                        } else
                            doMouseClicked(me);
                    }
                });
    }

    private boolean onFolder(String f, File file) {
        if(file == null) return false;
        File f1 = new File(workspace.project().getProjectLocation(), f);
        File p = file;
        do {
            if (p.equals(f1)) {
                return true;
            }
            p = p.getParentFile();
        } while (!Objects.equals(p, workspace.project().getProjectLocation()));
        return false;
    }

    private List<JMenuItem> popupT() {
        List<JMenuItem> popupMenu = new ArrayList<>();
        TreePath[] tps = tree.getSelectionPaths();

        if (tps == null || tps[0] == null) {
            JMenu ext = new JMenu("View Options");
            ext.add(new TextShowPanel("sort_options").panel());
            ext.add(new JMenuItem("Sort by name"));
            ext.add(new JMenuItem("Sort by modification(Newest first)"));
            ext.add(new JMenuItem("Sort by modification(Oldest first)"));
            popupMenu.add(ext);
            ext.add(new TextShowPanel("file_filter").panel());
            ext.add(new JCheckBox("Hide editor files"));
            return popupMenu;
        }

        boolean isDir = false, isScript = false, isScene = false, isPrefab = false, multiple = tps.length > 1, includeRoot = false;

        if (!multiple) {
            if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).isDirectory())
                isDir = true;
            if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).getName().endsWith(".java"))
                isScript = true;
            if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).getName().endsWith(".ypfb"))
                isPrefab = true;
            if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).getName().endsWith(".yscn"))
                isScene = true;
        }

        for (TreePath tp : tps)
            if (tp.getParentPath() == null) {
                includeRoot = true;
                break;
            }

        TreePath[] finalTps = tps;

        if (isScript) {
            popupMenu.add(new JMenuItem(new AbstractAction("Open script") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CodePanel.newCodeFrame(workspace.desktopPane(), workspace.project().preferredInstall(), (File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject(), null, workspace.recompile());
                }
            }));
        }

        if (isPrefab) {
            popupMenu.add(new JMenuItem(new AbstractAction("Open prefab") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    YieldInternalFrame frame = new YieldInternalFrame(null);
                    frame.setFrameIcon(Assets.images.get("windowIcon.png"));
                    EntityPrefab prefab;
                    try (ObjectInputStream oi = new CustomObjectInputStream(new FileInputStream((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), new URLClassLoader(new URL[]{new File(Utils.EDITOR_DIR.getPath() + "/installs/" + workspace.project().preferredInstall().install(), "yield-core.jar").toURI().toURL(), ComponentProp.DEST.toURI().toURL()}))) {
                        prefab = (EntityPrefab) oi.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.add(new ObjectEditor((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject(), prefab, workspace, frame));


                    frame.setTitle("Object Editor");
                    setupInternalFrame(frame);
                }
            }));
        }

        if (isScene) {
            popupMenu.add(new JMenuItem(new AbstractAction("Open scene") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    YieldInternalFrame frame = new YieldInternalFrame(null);
                    frame.setFrameIcon(Assets.images.get("windowIcon.png"));
                    EditorScene scene;
                    try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()))) {
                        scene = (EditorScene) oi.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.add(new SceneEditor(scene, sceneExplorer));


                    frame.setTitle("Scene Editor");
                    setupInternalFrame(frame);
                    frame.setBounds(100, 100, 600, 500);
                }
            }));
        }

        popupMenu.add(new JMenuItem(new AbstractAction("Open" + (isDir ? " in Explorer" : " in Desktop")) {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean open = true;
                if (multiple) {
                    open = JOptionPane.showConfirmDialog(null, "Open " + finalTps.length + " files?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                }
                if (open) {
                    for (TreePath tp : finalTps)
                        try {
                            Desktop.getDesktop().open(((File) ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject()));
                        } catch (IOException ex) {
                            Utils.errorNoStackTrace(null, new IOException("Could not open '" + ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject() + "'"));
                        }
                }

            }
        }));

        if (isDir) {
            popupMenu.add(new JMenuItem(new AbstractAction("Create folder") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Map<String, Prop[]> props = new HashMap<>();
                    props.put("new_directory", new Prop[]{new StringProp("name", "")});
                    new PropsWindow(props, () -> {
                        new File(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), (String) Objects.requireNonNull(Props.get(props.get("new_directory"), "name")).getValue()).mkdir();
                        Explorer.this.actionPerformed(null);
                    }, null, "new_directory");
                }
            }));
            if (onFolder("Prefabs", (File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()))
                popupMenu.add(new JMenuItem(new AbstractAction("Create prefab") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Prop[]> props = new HashMap<>();
                        props.put("new_prefab", new Prop[]{new StringProp("name", "")});
                        new PropsWindow(props, () -> {
                            if (Objects.requireNonNull(Props.get(props.get("new_prefab"), "name")).getValue().equals("")) {
                                JOptionPane.showMessageDialog(null, "Prefab needs a name");
                            } else {
                                File f = new File(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), Objects.requireNonNull(Props.get(props.get("new_prefab"), "name")).getValue() + ".ypfb");
                                try {
                                    f.createNewFile();
                                } catch (IOException ex) {
                                    Utils.error(null, ex);
                                }
                                EntityPrefab prefab = new EntityPrefab(workspace.project().preferredInstall());
                                prefab.setName((String) Objects.requireNonNull(Props.get(props.get("new_prefab"), "name")).getValue());
                                try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(f))) {
                                    oo.writeObject(prefab);
                                } catch (IOException ex) {
                                    Utils.error(null, ex);
                                }
                            }
                            Explorer.this.actionPerformed(null);
                        }, null, "new_prefab");
                    }
                }));
            if (onFolder("Scenes", (File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()))
                popupMenu.add(new JMenuItem(new AbstractAction("Create scene") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Prop[]> props = new HashMap<>();
                        props.put("new_scene", new Prop[]{new StringProp("name", "")});
                        new PropsWindow(props, () -> {
                            if (Objects.requireNonNull(Props.get(props.get("new_scene"), "name")).getValue().equals("")) {
                                JOptionPane.showMessageDialog(null, "Scene needs a name");
                            } else {
                                File f = new File(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), Objects.requireNonNull(Props.get(props.get("new_scene"), "name")).getValue() + ".yscn");
                                try {
                                    f.createNewFile();
                                } catch (IOException ex) {
                                    Utils.error(null, ex);
                                }
                                EditorScene scene = new EditorScene();
                                scene.setName((String) Objects.requireNonNull(Props.get(props.get("new_scene"), "name")).getValue());
                                try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(f))) {
                                    oo.writeObject(scene);
                                } catch (IOException ex) {
                                    Utils.error(null, ex);
                                }
                            }
                            Explorer.this.actionPerformed(null);
                        }, null, "new_scene");
                    }
                }));
            if (onFolder("Scripts", (File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()))
                popupMenu.add(new JMenuItem(new AbstractAction("Create script") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newScript(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()));
                        workspace.recompile().recompileProject();
                    }
                }));
        }

        if (!includeRoot)
            popupMenu.add(new JMenuItem(new AbstractAction("Delete") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JOptionPane.showConfirmDialog(null, "Delete " + finalTps.length + " file?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        for (TreePath tp : finalTps) {
                            Path path = ((File) ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject()).toPath();

                            try {
                                Files.walkFileTree(path,
                                        new SimpleFileVisitor<>() {

                                            @Override
                                            public FileVisitResult postVisitDirectory(Path dir,
                                                                                      IOException exc)
                                                    throws IOException {
                                                Files.delete(dir);
                                                return FileVisitResult.CONTINUE;
                                            }

                                            @Override
                                            public FileVisitResult visitFile(Path file,
                                                                             BasicFileAttributes attrs)
                                                    throws IOException {
                                                Files.delete(file);
                                                return FileVisitResult.CONTINUE;
                                            }
                                        }
                                );
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        Explorer.this.actionPerformed(null);
                    }
                }
            }));
        return popupMenu;
    }

    private void setupInternalFrame(YieldInternalFrame frame) {
        frame.setClosable(true);
        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        workspace.desktopPane().add(frame);
        frame.setBounds(100, 100, 360, 600);
        frame.setVisible(true);
    }

    DefaultMutableTreeNode createTree(File temp) {
        DefaultMutableTreeNode top = new SimpleTreeNode(temp, Comparator.comparing(o -> ((File) ((DefaultMutableTreeNode) o).getUserObject())));
        if (!(temp.exists() && temp.isDirectory()))
            return top;

        fillTree(top, temp);

        return top;
    }

    private List<Integer> getExpanded() {
        if(tree == null) return new ArrayList<>();
        List<Integer> expanded = new ArrayList<>();
        for (int i = 0; i < tree.getRowCount() - 1; i++) {
            TreePath currPath = tree.getPathForRow(i);
            TreePath nextPath = tree.getPathForRow(i + 1);
            if (currPath.isDescendant(nextPath)) {
                expanded.add(i);
            }
        }
        return expanded;
    }

    private void expanded(List<Integer> expanded) {
        for (Integer i : expanded) {
            tree.expandPath(tree.getPathForRow(i));
        }
    }

    void fillTree(DefaultMutableTreeNode root, File file) {
        if (!(file.exists() && file.isDirectory()))
            return;
        File[] filelist = file.listFiles();

        assert filelist != null;
        for (File c : filelist) {
            final DefaultMutableTreeNode tempDmtn = new DefaultMutableTreeNode(c);

            root.add(tempDmtn);
            fillTree(tempDmtn, c);
        }
    }

    public static File fPath(TreePath tp, File mainDir) {
        if (tp == null)
            return null;
        String s = tp.toString().substring(1, tp.toString().length() - 1);
        StringBuilder m = new StringBuilder();

        String[] spl = s.split(", ");

        for (int i = 1; i < spl.length; i++) {
            if (i > 1)
                m.append("\\");
            m.append(spl[i]);
        }

        return new File(mainDir, m.toString());
    }

    void doMouseClicked(MouseEvent me) {
        TreePath tp = tree.getSelectionPath();
        if (tp == null) return;

        if (tp.getParentPath() == null) {
            currDirectory = mainDir;
            return;
        }

        if (!((File) ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject()).isDirectory() && me.getClickCount() == 2) {
            popupT().get(0).getAction().actionPerformed(null);
        } else
            showFiles(fPath(tp, mainDir));
    }

    void showFiles(File file) {
        if (file.isDirectory()) {
            currDirectory = file;
        }
    }
////////////////////////////////  
///////////////////////////////  
}