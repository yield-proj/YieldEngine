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
import com.xebisco.yield.editor.scene.EntityPrefab;
import com.xebisco.yield.editor.scene.ObjectEditor;
import org.fife.rsta.ac.java.classreader.attributes.Code;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Explorer extends JPanel implements ActionListener {
    private JTextArea jta;
    private JTree tree;
    private JButton refresh;
    private JTable jtb;
    private JScrollPane jsp;

    File currDirectory = null;

    final String[] colHeads = {"File Name", "SIZE(in Bytes)", "Read Only", "Hidden"};
    String[][] data = {{"", "", "", "", ""}};

    private final File mainDir;

    private final Workspace workspace;

    public Explorer(File mainDir, String mainName, Workspace workspace) {
        this.mainDir = mainDir;
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

    public void actionPerformed(ActionEvent ev) {
        DefaultMutableTreeNode newtop = createTree(mainDir);
        if (newtop != null) {
            tree = new JTree(newtop);

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
                            JPopupMenu popupMenu = new JPopupMenu();

                            TreePath[] tps = tree.getSelectionPaths();
                            if (tps == null)
                                tps = new TreePath[]{tree.getPathForLocation(me.getX(), me.getY())};

                            if (tps[0] == null)
                                return;

                            boolean isDir = false, isScript = false, isPrefab = false, multiple = tps.length > 1, includeRoot = false;

                            if (!multiple) {
                                if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).isDirectory())
                                    isDir = true;
                                if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).getName().endsWith(".java"))
                                    isScript = true;
                                if (((File) ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject()).getName().endsWith(".ypfb"))
                                    isPrefab = true;
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
                                        CodePanel.newCodeFrame(workspace.desktopPane(), workspace.project().preferredInstall(), (File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject(), null);
                                    }
                                }));
                            }

                            if (isPrefab) {
                                popupMenu.add(new JMenuItem(new AbstractAction("Open prefab") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        YieldInternalFrame frame = new YieldInternalFrame(null);;
                                        frame.setFrameIcon(Assets.images.get("windowIcon.png"));
                                        EntityPrefab prefab;
                                        try(ObjectInputStream oi = new CustomObjectInputStream(new FileInputStream((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), new URLClassLoader(new URL[]{new File(Utils.EDITOR_DIR.getPath() + "/installs/" + workspace.project().preferredInstall().install(), "yield-core.jar").toURI().toURL(), ComponentProp.DEST.toURI().toURL()}))) {
                                            prefab = (EntityPrefab) oi.readObject();
                                        } catch (IOException | ClassNotFoundException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        frame.add(new ObjectEditor((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject(), prefab, workspace, frame));



                                        frame.setTitle("Object Editor");
                                        frame.setClosable(true);
                                        frame.setMaximizable(true);
                                        frame.setIconifiable(true);
                                        frame.setResizable(true);
                                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                                        workspace.desktopPane().add(frame);
                                        frame.setBounds(100, 100, 400, 600);
                                        frame.setVisible(true);
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
                                                throw new RuntimeException(ex);
                                            }
                                    }

                                }
                            }));

                            if (isDir) {
                                popupMenu.add(new JMenuItem(new AbstractAction("Create folder") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        Map<String, Prop[]> props = new HashMap<>();
                                        props.put("New Directory", new Prop[]{new StringProp("Name", "")});
                                        new PropsWindow(props, () -> {
                                            new File(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), (String) Objects.requireNonNull(Props.get(props.get("New Directory"), "Name")).getValue()).mkdir();
                                            Explorer.this.actionPerformed(null);
                                        }, null, "New Directory");
                                    }
                                }));
                                popupMenu.add(new JMenuItem(new AbstractAction("Create prefab") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        Map<String, Prop[]> props = new HashMap<>();
                                        props.put("New Prefab", new Prop[]{new StringProp("Name", "")});
                                        new PropsWindow(props, () -> {
                                            if (Objects.requireNonNull(Props.get(props.get("New Prefab"), "Name")).getValue().equals("")) {
                                                JOptionPane.showMessageDialog(null, "Prefab needs a name");
                                                actionPerformed(e);
                                            } else {
                                                File f = new File(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), Objects.requireNonNull(Props.get(props.get("New Prefab"), "Name")).getValue() + ".ypfb");
                                                try {
                                                    f.createNewFile();
                                                } catch (IOException ex) {
                                                    Utils.error(null, ex);
                                                }
                                                URLClassLoader core;
                                                try {
                                                    core = new URLClassLoader(new URL[]{new File(Utils.EDITOR_DIR.getPath() + "/installs/" + workspace.project().preferredInstall().install(), "yield-core.jar").toURI().toURL()});
                                                } catch (MalformedURLException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                EntityPrefab prefab = new EntityPrefab();
                                                prefab.setName((String) Objects.requireNonNull(Props.get(props.get("New Prefab"), "Name")).getValue());
                                                try {
                                                    Class<?> transform = core.loadClass("com.xebisco.yield.Transform2D");
                                                    prefab.components().add(new ComponentProp(transform, false).set(null));
                                                } catch (ClassNotFoundException e1) {
                                                    Utils.error(null, e1);
                                                }
                                                try {
                                                    core.close();
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                try(ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(f))) {
                                                    oo.writeObject(prefab);
                                                } catch (IOException ex) {
                                                    Utils.error(null, ex);
                                                }
                                            }
                                            Explorer.this.actionPerformed(null);
                                        }, null, "New Prefab");
                                    }
                                }));
                                popupMenu.add(new JMenuItem(new AbstractAction("Create script") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        Map<String, Prop[]> props = new HashMap<>();
                                        props.put("New Script", new Prop[]{new StringProp("Name", ""), new BooleanProp("Create default methods", true)});
                                        new PropsWindow(props, () -> {
                                            if (Objects.requireNonNull(Props.get(props.get("New Script"), "Name")).getValue().equals("")) {
                                                JOptionPane.showMessageDialog(null, "Script needs a name");
                                                actionPerformed(e);
                                            } else {
                                                File f = new File(((File) ((DefaultMutableTreeNode) finalTps[0].getLastPathComponent()).getUserObject()), Objects.requireNonNull(Props.get(props.get("New Script"), "Name")).getValue() + ".java");
                                                try {
                                                    f.createNewFile();

                                                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                                                        writer.append("import com.xebisco.yield.*;\n\npublic class ").append(f.getName().split("\\.java")[0]).append(" extends ComponentBehavior {\n");
                                                        if ((boolean) Objects.requireNonNull(Props.get(props.get("New Script"), "Create default methods")).getValue()) {
                                                            writer.append("\n\t//This method is called before the first update of the entity\n\t@Override\n\tpublic void onStart() {\n\t\t\n\t}\n\n\t//This method is called every frame\n\t@Override\n\tpublic void onUpdate() {\n\t\t\n\t}\n");
                                                        }
                                                        writer.append("\n}");
                                                    } catch (IOException ex) {
                                                        Utils.error(null, ex);
                                                    }
                                                } catch (IOException ex) {
                                                    Utils.error(null, ex);
                                                }
                                            }
                                            Explorer.this.actionPerformed(null);
                                        }, null, "New Script");
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

                            popupMenu.show(Explorer.this, me.getX() + 2, me.getY() + 42);
                        } else
                            doMouseClicked(me);
                    }
                });
    }

    DefaultMutableTreeNode createTree(File temp) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(temp);
        if (!(temp.exists() && temp.isDirectory()))
            return top;

        fillTree(top, temp);

        return top;
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