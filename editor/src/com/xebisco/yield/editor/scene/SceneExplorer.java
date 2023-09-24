package com.xebisco.yield.editor.scene;

import com.xebisco.yield.editor.*;
import com.xebisco.yield.editor.prop.ComponentProp;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class SceneExplorer extends JPanel implements ActionListener {
    private JTree tree;
    private JTable jtb;
    private JScrollPane jsp;

    File currDirectory = null;

    final String[] colHeads = {"File Name", "SIZE(in Bytes)", "Read Only", "Hidden"};
    String[][] data = {{"", "", "", "", ""}};
    private EditorScene scene;
    private final Workspace workspace;

    private File sceneFile;

    public SceneExplorer(Workspace workspace) {
        this.workspace = workspace;
    }

    public SceneExplorer setScene(File sceneFile, EditorScene scene) {
        this.scene = scene;
        this.sceneFile = sceneFile;
        removeAll();
        if (scene != null) {
            setLayout(new BorderLayout());
            JButton refresh = new JButton(Assets.images.get("reloadIcon.png"));
            JButton newFolder = new JButton(UIManager.getIcon("Tree.closedIcon"));

            JToolBar p = new JToolBar();
            p.add(new JLabel("Scene Root", Assets.images.get("sceneIcon.png"), SwingConstants.CENTER));
            p.add(refresh);
            p.add(new JButton(new AbstractAction("View") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    YieldInternalFrame frame = new SceneEditor(sceneFile, null, scene, SceneExplorer.this);
                    frame.setFrameIcon(Assets.images.get("windowIcon.png"));


                    frame.setTitle("Scene Editor");
                    setupInternalFrame(frame, workspace.desktopPane());
                    frame.setSize(600, 500);
                }
            }));
            add(p, BorderLayout.NORTH);

            refresh.addActionListener(this);

            java.util.List<Integer> expanded = getExpanded();
            DefaultMutableTreeNode newtop = createTree(scene);
            if (newtop != null) {
                tree = new JTree(newtop);
                //TODO drag and drop
                /*tree.setDragEnabled(true);
                tree.setDropMode(DropMode.ON_OR_INSERT);
                tree.setTransferHandler(new TreeFileTransferHandler());
                tree.getSelectionModel().setSelectionMode(
                        TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);*/
                expanded(expanded);
            }
            tree.setCellRenderer(new SceneExplorerCellRenderer());
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

                                java.util.List<JMenuItem> items = popupT();
                                for (JMenuItem item : items)
                                    popupMenu.add(item);

                                popupMenu.show(SceneExplorer.this, me.getX() + 2, me.getY() + 42);
                            } else
                                doMouseClicked(me);
                        }
                    });
        } else {
            setLayout(new FlowLayout());
            add(new JLabel("No scene loaded"));
        }
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(scene != null) {
            try(ObjectOutputStream oo = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(sceneFile)))) {
                oo.writeObject(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void actionPerformed(ActionEvent ev) {
        setScene(sceneFile, scene);
    }

    public EntityPrefab rectPrefab() {
        EntityPrefab prefab = new EntityPrefab(workspace.project().preferredInstall());

        try (URLClassLoader cl = new URLClassLoader(new URL[]{new File(Utils.EDITOR_DIR + "/installs/" + workspace.project().preferredInstall().install(), "yield-core.jar").toURI().toURL()})) {
            prefab.components().add(new ComponentProp(cl.loadClass("com.xebisco.yield.Rectangle"), true).init(null));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return prefab;
    }

    private java.util.List<JMenuItem> popupT() {
        java.util.List<JMenuItem> popupMenu = new ArrayList<>();
        TreePath[] tps = tree.getSelectionPaths();

        if (tps == null) return popupMenu;

        Object o = ((DefaultMutableTreeNode) tps[0].getLastPathComponent()).getUserObject();

        JMenu menu = new JMenu("New");

        List<File> sceneObjects0 = scene.sceneObjects();

        if (o instanceof SceneObject so)
            sceneObjects0 = so.sceneObjects();
        final var sceneObjects = new ArrayList<SceneObject>();
        for(File f : sceneObjects0) {
            loadSceneObject(sceneObjects, f, workspace);
        }

        boolean str = o instanceof String;

        List<File> finalSceneObjects = sceneObjects0;
        menu.add(new JMenuItem(new AbstractAction("Empty") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //noinspection DataFlowIssue
                finalSceneObjects.add(savePrefab(new SceneObject(new EntityPrefab(workspace.project().preferredInstall()).setName("Sample Entity"), str ? null : (SceneObject) o)));
                SceneExplorer.this.actionPerformed(null);
            }
        }));
        menu.add(new JMenuItem(new AbstractAction("Rectangle") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //noinspection DataFlowIssue
                finalSceneObjects.add(savePrefab(new SceneObject(rectPrefab().setName("Sample Rectangle"), str ? null : (SceneObject) o)));
                SceneExplorer.this.actionPerformed(null);
            }
        }));
        popupMenu.add(menu);

        if (str) return popupMenu;

        assert o instanceof SceneObject;
        SceneObject so = (SceneObject) o;

        popupMenu.add(new JMenuItem(new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                YieldInternalFrame frame = new YieldInternalFrame(null);
                frame.setFrameIcon(Assets.images.get("windowIcon.png"));
                File file = new File(new File(workspace.project().getProjectLocation(), ".generated"), "pid" + so.id() + ".ypfb");
                EntityPrefab prefab;
                try (ObjectInputStream oi = new CustomObjectInputStream(new FileInputStream(file), new URLClassLoader(new URL[]{new File(Utils.EDITOR_DIR.getPath() + "/installs/" + workspace.project().preferredInstall().install(), "yield-core.jar").toURI().toURL(), ComponentProp.DEST.toURI().toURL()}))) {
                    prefab = ((SceneObject) oi.readObject()).entityPrefab();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                frame.add(new ObjectEditor(file, prefab, workspace, frame));


                frame.setTitle("Object Editor");
                setupInternalFrame(frame, workspace.desktopPane());
            }
        }));

        popupMenu.add(new JMenuItem(new AbstractAction("Find") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }));


        popupMenu.add(new JMenuItem(new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (so.parent() == null)
                    scene.sceneObjects().remove(so);
                else so.parent().sceneObjects().remove(so);
                SceneExplorer.this.actionPerformed(null);
            }
        }));


        return popupMenu;
    }

    private File savePrefab(SceneObject cso) {
        File savedF = new File(new File(workspace.project().getProjectLocation(), ".generated"), "pid" + cso.id() + ".ypfb");
        try {
            savedF.createNewFile();
            try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(savedF))) {
                oo.writeObject(cso);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        SceneExplorer.this.actionPerformed(null);
        return savedF;
    }

    public static YieldInternalFrame setupInternalFrame(YieldInternalFrame frame, JDesktopPane desktopPane) {
        frame.setFrameIcon(Assets.images.get("scriptIcon.png"));
        frame.setClosable(true);
        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.setBounds(100, 100, 360, 600);
        upPos(frame, desktopPane);
        desktopPane.add(frame);
        frame.setVisible(true);
        return frame;
    }

    private static void upPos(JInternalFrame frame, JDesktopPane desktopPane) {
        for (JInternalFrame f : desktopPane.getAllFrames()) {
            boolean a = false;
            if (f.getX() == frame.getX()) {
                frame.setLocation(frame.getX() + 50, frame.getY());
                a = true;
            }
            if (f.getY() == frame.getY()) {
                frame.setLocation(frame.getX(), frame.getY() + 50);
                a = true;
            }
            if (a) upPos(frame, desktopPane);
        }
    }

    DefaultMutableTreeNode createTree(EditorScene temp) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(scene.name());
        final var sceneObjects = new ArrayList<SceneObject>();
        for(File f : temp.sceneObjects()) {
            loadSceneObject(sceneObjects, f, workspace);
        }
        fillTree(top, sceneObjects);
        return top;
    }

    public static void loadSceneObject(ArrayList<SceneObject> sceneObjects, File f, Workspace workspace) {
        if(f.exists()) {
            try (ObjectInputStream oi = new CustomObjectInputStream(new FileInputStream(f), new URLClassLoader(new URL[]{new File(Utils.EDITOR_DIR.getPath() + "/installs/" + workspace.project().preferredInstall().install(), "yield-core.jar").toURI().toURL(), ComponentProp.DEST.toURI().toURL()}))) {
                sceneObjects.add((SceneObject) oi.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private java.util.List<Integer> getExpanded() {
        if (tree == null) return new ArrayList<>();
        java.util.List<Integer> expanded = new ArrayList<>();
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

    void fillTree(DefaultMutableTreeNode root, List<SceneObject> objects) {
        if (objects.isEmpty()) return;
        for (SceneObject o : objects) {
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(o);

            final var sceneObjects = new ArrayList<SceneObject>();
            for(File f : o.sceneObjects()) {
                loadSceneObject(sceneObjects, f, workspace);
            }
            fillTree(tn, sceneObjects);

            root.add(tn);
        }
    }

    void doMouseClicked(MouseEvent me) {
        //TODO

    }

    public JTree tree() {
        return tree;
    }

    public SceneExplorer setTree(JTree tree) {
        this.tree = tree;
        return this;
    }

    public JTable jtb() {
        return jtb;
    }

    public SceneExplorer setJtb(JTable jtb) {
        this.jtb = jtb;
        return this;
    }

    public JScrollPane jsp() {
        return jsp;
    }

    public SceneExplorer setJsp(JScrollPane jsp) {
        this.jsp = jsp;
        return this;
    }

    public File currDirectory() {
        return currDirectory;
    }

    public SceneExplorer setCurrDirectory(File currDirectory) {
        this.currDirectory = currDirectory;
        return this;
    }

    public String[] colHeads() {
        return colHeads;
    }

    public String[][] data() {
        return data;
    }

    public SceneExplorer setData(String[][] data) {
        this.data = data;
        return this;
    }

    public EditorScene scene() {
        return scene;
    }

    public SceneExplorer setScene(EditorScene scene) {
        this.scene = scene;
        return this;
    }

    public Workspace workspace() {
        return workspace;
    }

    public File sceneFile() {
        return sceneFile;
    }

    public SceneExplorer setSceneFile(File sceneFile) {
        this.sceneFile = sceneFile;
        return this;
    }

    /*
    void showFiles(File file) {
        if (file.isDirectory()) {
            currDirectory = file;
        }
    }*/
}
