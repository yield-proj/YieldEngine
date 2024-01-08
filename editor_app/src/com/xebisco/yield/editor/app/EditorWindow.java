package com.xebisco.yield.editor.app;

import com.xebisco.yield.editor.app.props.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EditorWindow extends JFrame {

    private File projectRoot;
    private File sceneFile;
    private EditorScene scene;
    private EditorProject project;
    private Dimension gridSize = new Dimension(16, 16);

    private int gridOpacity = 30;

    public EditorWindow() {
        setJMenuBar(menuBar());
        setTitle(Srd.TITLE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(EditorWindow.this, Srd.LANG.getProperty("editor_closeWarning"), "Confirm Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
        setMinimumSize(new Dimension(700, 700));


        JToolBar toolBar = new JToolBar();
        JLabel xLabel = new JLabel(), yLabel = new JLabel(), zoomLabel = new JLabel();
        toolBar.setFloatable(true);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(xLabel);
        toolBar.addSeparator();
        toolBar.add(yLabel);
        toolBar.addSeparator();
        toolBar.add(zoomLabel);
        add(toolBar, BorderLayout.NORTH);

        add(new GameView(xLabel, yLabel, zoomLabel));

        setSize(new Dimension(1280, 720));
    }

    private JMenuBar menuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_file"));
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_newFolder")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showDialog(EditorWindow.this, Srd.LANG.getProperty("se_menuBar_file_newFolder_jfpSelectText")) == JFileChooser.APPROVE_OPTION) {
                    String folderName = JOptionPane.showInputDialog(Srd.LANG.getProperty("se_menuBar_file_newFolder_jfpFolderName"));
                    if (folderName == null || folderName.isEmpty()) {
                        JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("misc_operationCanceled"));
                        return;
                    }
                    File file = new File(fileChooser.getSelectedFile(), folderName);
                    File projectFile = new File(file, "project.yep");
                    if (file.exists() || projectFile.exists()) {
                        JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("misc_operationCanceled"));
                        return;
                    }
                    file.mkdirs();
                    try {
                        projectFile.createNewFile();
                        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(projectFile))) {
                            oo.writeObject(new EditorProject().setName(folderName));
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    new File(file, "Prefabs").mkdir();
                    setProjectRoot(file);
                }
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_newScene")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectRoot == null) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("se_noFolderError"));
                    return;
                }
                JFileChooser fileChooser = new JFileChooser(projectRoot);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showDialog(EditorWindow.this, Srd.LANG.getProperty("se_menuBar_file_newFolder_jfpSelectText")) == JFileChooser.APPROVE_OPTION) {
                    String folderName = JOptionPane.showInputDialog("Scene className");
                    if (folderName == null || folderName.isEmpty()) {
                        JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("misc_operationCanceled"));
                        return;
                    }
                    File file = new File(fileChooser.getSelectedFile(), folderName + ".yesc");
                    if (file.exists()) {
                        JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("misc_operationCanceled"));
                        return;
                    }
                    try {
                        file.createNewFile();
                        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file))) {
                            oo.writeObject(new EditorScene().setName(folderName));
                        }
                        openScene(file);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_newEntityPrefab")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectRoot == null) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("se_noFolderError"));
                    return;
                }

                String folderName = JOptionPane.showInputDialog("Entity/File name");
                if (folderName == null || folderName.isEmpty()) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("misc_operationCanceled"));
                    return;
                }

                File prefabFile = new File(new File(projectRoot, "Prefabs"), folderName + ".yepf");
                try {
                    if (!prefabFile.createNewFile()) {
                        JOptionPane.showMessageDialog(EditorWindow.this, "Prefab file already exists");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(EditorWindow.this, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }

                EditorEntity entity = new EditorEntity().setEntityName(folderName);

                try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(prefabFile))) {
                    oo.writeObject(entity);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                JDialog dialog = openEntity(EditorWindow.this, entity, prefabFile);
                dialog.setVisible(true);
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_newWindow")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorWindow window = new EditorWindow();
                if (projectRoot != null) window.setProjectRoot(projectRoot);
                window.setLocationRelativeTo(EditorWindow.this);
                window.setLocation(window.getLocation().x + 20, window.getLocation().y + 20);
                window.setVisible(true);
            }
        }));

        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_openFolder")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showOpenDialog(EditorWindow.this) == JFileChooser.APPROVE_OPTION) {
                    if (new File(fileChooser.getSelectedFile(), "project.yep").exists())
                        setProjectRoot(fileChooser.getSelectedFile());
                    else
                        JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("misc_operationCanceled"));
                }
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_openScene")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectRoot == null) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("se_noFolderError"));
                    return;
                }
                JFileChooser fileChooser = new JFileChooser(projectRoot);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().endsWith(".yesc");
                    }

                    @Override
                    public String getDescription() {
                        return "Yield Editor Scene (.yesc)";
                    }
                });
                if (fileChooser.showOpenDialog(EditorWindow.this) == JFileChooser.APPROVE_OPTION) {
                    setSceneFile(fileChooser.getSelectedFile());
                }
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_openEntityPrefab")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectRoot == null) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("se_noFolderError"));
                    return;
                }
                JFileChooser fileChooser = new JFileChooser(new File(projectRoot, "Prefabs"));
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
                if (fileChooser.showOpenDialog(EditorWindow.this) == JFileChooser.APPROVE_OPTION) {
                    EditorEntity prefab;
                    try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                        prefab = (EditorEntity) oi.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    JDialog dialog = openEntity(EditorWindow.this, prefab, fileChooser.getSelectedFile());
                    dialog.setVisible(true);
                }
            }
        }));

        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_openProjectInExplorer")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectRoot == null) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("se_noFolderError"));
                    return;
                }
                try {
                    Desktop.getDesktop().open(projectRoot);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }));

        fileMenu.addSeparator();

        JMenu preferencesMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_file_preferences"));

        fileMenu.add(preferencesMenu);

        preferencesMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_preferences_allSettings")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Prop[]> sections = new HashMap<>();

                sections.put("Scene View", new Prop[]{new SizeProp("Grid Size", gridSize), new SlideProp("Grid Opacity", gridOpacity, 0, 255)});
                sections.put("Engine", new Prop[]{new TextFieldProp("Yield Engine Core Jar URI", Srd.yieldEngineURL)});

                new SettingsPropDialog(EditorWindow.this, sections, () -> {
                    Map<String, Serializable> sceneViewValues = PropPanel.values(sections.get("Scene View"));
                    gridSize = (Dimension) sceneViewValues.get("Grid Size");
                    gridOpacity = (int) sceneViewValues.get("Grid Opacity");

                    Map<String, Serializable> engineValues = PropPanel.values(sections.get("Engine"));
                    boolean differentEngine = false;
                    if (!engineValues.get("Yield Engine Core Jar URI").equals(Srd.yieldEngineURL)) {
                        Srd.yieldEngineURL = (String) engineValues.get("Yield Engine Core Jar URI");
                        differentEngine = true;
                    }
                    if (differentEngine) {
                        try {
                            Srd.yieldEngineClassLoader = new URLClassLoader(new URL[]{new URI(Srd.yieldEngineURL).toURL()});
                            Class<?> globalClass;
                            if ((globalClass = Srd.yieldEngineClassLoader.loadClass("com.xebisco.yield.Global")) == null)
                                throw new IllegalStateException("invalid Yield Engine Jar URI");
                            JOptionPane.showMessageDialog(EditorWindow.this, "Yield Engine REV " + globalClass.getField("REV").get(null));
                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(EditorWindow.this, "Incompatible Yield Engine jar", "Incompatible Engine", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(EditorWindow.this, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        }));
        preferencesMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_preferences_theme")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> themes = new HashMap<>();
                for (UIManager.LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
                    themes.put(lafi.getName(), lafi.getClassName());
                }

                String[] options = themes.keySet().toArray(new String[0]);

                int option = JOptionPane.showOptionDialog(EditorWindow.this, "Installed Themes", "Theme", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

                if (option != JOptionPane.CLOSED_OPTION) {
                    try {
                        UIManager.setLookAndFeel(themes.get(options[option]));
                        reloadEditor();
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                             UnsupportedLookAndFeelException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }));

        fileMenu.add(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_reloadEditor")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadEditor();
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_exit")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(EditorWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        }));

        JMenu sceneMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_scene")) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setEnabled(scene != null);
            }
        };
        menuBar.add(sceneMenu);

        JMenu sceneAddMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_scene_add"));

        sceneMenu.add(sceneAddMenu);

        sceneAddMenu.add(new AbstractAction(Srd.LANG.getProperty("se_menuBar_scene_add_emptyEntity")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                scene.entities().add(new EditorEntity());
            }
        });

        sceneMenu.addSeparator();

        sceneMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_scene_preferences")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Prop[]> sections = new HashMap<>();

                sections.put("Scene Properties", new Prop[]{new TextFieldProp("Scene Name", scene.name()), new ColorProp("Background Color", scene.backgroundColor())});
                new SettingsPropDialog(EditorWindow.this, sections, () -> {
                    Map<String, Serializable> values = PropPanel.values(sections.get("Scene Properties"));
                    scene.setName((String) values.get("Scene Name"));
                    scene.setBackgroundColor((Color) values.get("Background Color"));
                });
            }
        }));

        sceneMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_scene_closeScene")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSceneFile(null);
            }
        }));

        JMenu runMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_run")) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setEnabled(projectRoot != null);
            }
        };
        menuBar.add(runMenu);

        return menuBar;
    }

    private void openScene(File scene) {
        int r = JOptionPane.showConfirmDialog(this, Srd.LANG.getProperty("misc_openInThis"));
        if (r == JOptionPane.YES_OPTION) {
            setSceneFile(scene);
        } else if (r == JOptionPane.NO_OPTION) {
            new EditorWindow().setSceneFile(scene);
        }
    }

    private JDialog fixedEntityDialog;

    public JDialog openEntity(Frame frame, EditorEntity entity, File toSaveFile) {
        AtomicReference<File> saveFile = new AtomicReference<>(toSaveFile);
        JDialog dialog = new JDialog(frame);

        boolean isFixed;
        if (fixedEntityDialog == null) {
            isFixed = true;
            fixedEntityDialog = dialog;
        } else isFixed = false;

        dialog.setUndecorated(isFixed);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setMinimumSize(new Dimension(280, 350));
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel(entity.entityName());
        title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(28f));
        AtomicBoolean inDialog = new AtomicBoolean(false);
        if (toSaveFile != null) dialog.setTitle(entity.entityName() + " (File Prefab)");
        else dialog.setTitle(entity.entityName() + " (In Scene)");

        title.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (toSaveFile != null) dialog.setTitle(entity.entityName() + " (File Prefab)");
                else dialog.setTitle(entity.entityName() + " (In Scene)");
                title.setText(entity.entityName());
                title.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                inDialog.set(true);
                String name = JOptionPane.showInputDialog(dialog, "Entity Name");
                if (name == null || name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, Srd.LANG.getProperty("misc_operationCanceled"));
                } else {
                    entity.setEntityName(name);
                    if (toSaveFile != null) {
                        if (JOptionPane.showConfirmDialog(dialog, "Change file name also?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            saveFile.get().renameTo(new File(saveFile.get().getParent(), name + ".yepf"));
                            saveFile.set(new File(saveFile.get().getParent(), name + ".yepf"));
                        }
                    }
                    dialog.requestFocus();
                }
                inDialog.set(false);
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
        header.add(optionsPanel, BorderLayout.EAST);

        dialog.add(header, BorderLayout.NORTH);


        List<Prop> props = new ArrayList<>();
        for (EditorComponent comp : entity.components()) {
            props.add(new ComponentProp(comp));
        }

        if (isFixed) addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (dialog.isVisible()) {
                    dialog.setLocation(EditorWindow.this.getX() + 100, EditorWindow.this.getY() + EditorWindow.this.getHeight() - dialog.getHeight() - 100);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (dialog.isVisible()) {
                    dialog.setLocation(EditorWindow.this.getX() + 100, EditorWindow.this.getY() + EditorWindow.this.getHeight() - dialog.getHeight() - 100);
                }
            }
        });

        if (isFixed)
            dialog.setLocation(EditorWindow.this.getX() + 100, EditorWindow.this.getY() + EditorWindow.this.getHeight() - dialog.getHeight() - 100);
        else dialog.setLocationRelativeTo(EditorWindow.this);


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (toSaveFile != null) {
                    try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(saveFile.get()))) {
                        oo.writeObject(entity);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if (isFixed && fixedEntityDialog == dialog) fixedEntityDialog = null;

                dialog.dispose();
            }
        });
        dialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (dialog.isUndecorated()) dialog.setOpacity(1);
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                if (dialog.isUndecorated()) dialog.setOpacity(.4f);
                if (!inDialog.get()) {
                    if (toSaveFile != null) {
                        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(saveFile.get()))) {
                            oo.writeObject(entity);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                }
            }
        });


        dialog.add(new JScrollPane(new PropPanel(props.toArray(new Prop[0]))));


        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton(new AbstractAction(Srd.LANG.getProperty("ce_addComponent")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addComponentDialog = new JDialog();
                addComponentDialog.setTitle(Srd.LANG.getProperty("ce_addComponent"));
                addComponentDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addComponentDialog.setSize(200, 200);
                addComponentDialog.setLocationRelativeTo(dialog);
                JList<Class<?>> components;
                try {
                    components = new JList<>(getComponentClasses(Srd.yieldEngineClassLoader, "com.xebisco.yield").toArray(new Class<?>[0]));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                components.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        l.setText(((Class<?>) value).getSimpleName());
                        l.setHorizontalAlignment(CENTER);
                        l.setFont(l.getFont().deriveFont(Font.BOLD));
                        l.setIcon(new ImageIcon(Objects.requireNonNull(ComponentProp.class.getResource("/com/xebisco/yield/editor/app/codeIcon.png"))));
                        return l;
                    }
                });
                addComponentDialog.add(new JScrollPane(components));
                JPanel buttonP = new JPanel();
                buttonP.add(new JButton(new AbstractAction(Srd.LANG.getProperty("ce_addComponent")) {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                }));
                addComponentDialog.setVisible(true);
            }
        });
        buttonPanel.add(addButton);
        if (isFixed) buttonPanel.add(new JButton(new AbstractAction(Srd.LANG.getProperty("misc_close")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }));
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.getRootPane().setDefaultButton(addButton);

        return dialog;
    }

    public static List<Class<?>> getComponentClasses(ClassLoader cl, String pack) throws Exception {
        List<String> classNames = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new URI(Srd.yieldEngineURL).toURL().openStream()));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
        }

        List<Class<?>> ret = new ArrayList<>();
        for (String className : classNames) {
            if (className.startsWith(pack)) {
                Class<?> c = cl.loadClass(className);
                if (!Modifier.isAbstract(c.getModifiers()) && classInstanceOf(c, cl.loadClass("com.xebisco.yield.ComponentBehavior")))
                    ret.add(c);
            }
        }

        return ret;
    }

    private static boolean classInstanceOf(Class<?> c, Class<?> c2) {
        Class<?> o = c;
        while (o.getSuperclass() != null) {
            o = o.getSuperclass();
            if (o.equals(c2)) return true;
        }
        return false;
    }


    class GameView extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

        private boolean dragging, moving;
        private Point lastDraggingPosition = new Point();
        private double viewPositionX, viewPositionY, mousePositionX, mousePositionY;

        private final JLabel xLabel, yLabel, zoomLabel;

        private final Timer RIGHT_TIMER = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPositionX += 1 / zoom;
                repaint();
            }
        }), LEFT_TIMER = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPositionX -= 1 / zoom;
                repaint();
            }
        }), UP_TIMER = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPositionY -= 1 / zoom;
                repaint();
            }
        }), DOWN_TIMER = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPositionY += 1 / zoom;
                repaint();
            }
        });

        private double zoom = 1;

        private EditorEntity selectedEntity;

        private boolean placeInGrid, xArrowSelected, yArrowSelected;

        public GameView(JLabel xLabel, JLabel yLabel, JLabel zoomLabel) {
            this.xLabel = xLabel;
            this.yLabel = yLabel;
            this.zoomLabel = zoomLabel;
            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
            setFocusable(true);
            requestFocus();
            updateLabels();
        }

        private void updateLabels() {
            xLabel.setText(String.format("x: %.1f", viewPositionX));
            yLabel.setText(String.format("y: %.1f", viewPositionY));
            zoomLabel.setText(String.format("zoom: %.1f", zoom));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (scene != null) {
                g.setColor(scene.backgroundColor());
                g.fillRect(0, 0, getWidth(), getHeight());
                Graphics2D g2 = (Graphics2D) g;
                if (zoom < 1) zoom = 1;
                g2.translate(getWidth() / 2., getHeight() / 2.);
                g2.scale(zoom, zoom);
                g2.translate(-getWidth() / 2., -getHeight() / 2.);
                g2.translate(-viewPositionX, -viewPositionY);

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                updateLabels();

                //TODO draw objs

                for (EditorEntity e : scene().entities()) {
                    e.draw(g2);
                    g2.setColor(new Color(213, 213, 213, 65));
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Point2D.Double position = e.position();
                    g2.fill(new Arc2D.Double(position.x - (8 / zoom / 2), position.y - (8 / zoom / 2), 8 / zoom, 8 / zoom, 0, 360, Arc2D.CHORD));
                }

                g2.setStroke(new BasicStroke((float) (1 / zoom)));

                g2.setColor(new Color(255 - scene.backgroundColor().getRed(), 255 - scene.backgroundColor().getGreen(), 255 - scene.backgroundColor().getBlue(), gridOpacity).brighter());

                int startX = (int) viewPositionX;
                while (startX % gridSize.width != 0) startX++;

                for (int i = 0; i < getWidth() / gridSize.width + 1; i++) {
                    g.drawLine(i * gridSize.width + startX, (int) viewPositionY, i * gridSize.width + startX, (int) (getHeight() + viewPositionY));
                }

                int startY = (int) viewPositionY;
                while (startY % gridSize.height != 0) startY++;

                for (int i = 0; i < getHeight() / gridSize.height + 1; i++) {
                    g.drawLine((int) viewPositionX, i * gridSize.height + startY, (int) (getWidth() + viewPositionX), i * gridSize.height + startY);
                }

                //Y
                g.setColor(new Color(10, 255, 10, 100));
                g.drawLine(0, (int) viewPositionY, 0, (int) (getHeight() + viewPositionY));


                //X
                g.setColor(new Color(255 - 100, 10, 10, 100));
                g.drawLine((int) viewPositionX, 0, (int) (getWidth() + viewPositionX), 0);

                if (placeInGrid) {
                    Point loc = closerGridLocationTo(new Point2D.Double(mousePositionX, mousePositionY));
                    double ballSize = 12 / zoom;
                    Shape circle = new Arc2D.Double(loc.x - ballSize / 2, loc.y - ballSize / 2, ballSize, ballSize, 0, 360, Arc2D.CHORD);
                    g2.fill(circle);
                }

                if (selectedEntity != null) {
                    drawArrows(g2, selectedEntity.position().x, selectedEntity.position().y);
                }
            } else {
                g.setColor(getBackground().darker());
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(getForeground());
                String noSceneLoaded = "No Scene Loaded";
                int s = g.getFontMetrics().stringWidth(noSceneLoaded);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.drawString(noSceneLoaded, getWidth() / 2 - s / 2, getHeight() / 2 + g.getFont().getSize() / 2);
            }
        }

        private Point closerGridLocationTo(Point2D.Double point) {
            boolean negativeX = point.x < 0, negativeY = point.y < 0;
            Point p = new Point((((int) Math.abs(point.getX()) + gridSize.width / 2) / gridSize.width) * gridSize.width, (((int) Math.abs(point.getY()) + gridSize.height / 2) / gridSize.height) * gridSize.height);
            if (negativeX) p.x *= -1;
            if (negativeY) p.y *= -1;
            return p;
        }

        private void drawArrows(Graphics2D g, double x, double y) {

            g.setStroke(new BasicStroke((float) (2 / zoom)));
            g.setColor(new Color(100, 100, 255, 120));
            g.draw(new Rectangle2D.Double(x, y - 14 / zoom, 14 / zoom, 14 / zoom));

            //X Arrow
            if (!moving)
                if (mousePositionX >= x && mousePositionX <= x + 120. / zoom && mousePositionY >= y - 14 / zoom && mousePositionY <= y + 7 / zoom) {
                    xArrowSelected = true;
                } else {
                    xArrowSelected = false;
                }
            if (xArrowSelected) g.setColor(Color.YELLOW);
            else g.setColor(Color.RED);

            g.setStroke(new BasicStroke((float) (4 / zoom)));
            g.draw(new Line2D.Double(x, y, x + 98. / zoom, y));
            g.draw(new Line2D.Double(x + 100. / zoom - 8. / zoom, y - 8. / zoom, x + 100. / zoom, y));
            g.draw(new Line2D.Double(x + 100. / zoom - 8. / zoom, y + 8. / zoom, x + 100. / zoom, y));


            //Y Arrow
            if (!moving)
                if (mousePositionY <= y && mousePositionY >= y - 120. / zoom && mousePositionX >= x - 7 / zoom && mousePositionX <= x + 14 / zoom) {
                    yArrowSelected = true;
                } else {
                    yArrowSelected = false;
                }
            if (yArrowSelected) g.setColor(Color.YELLOW);
            else g.setColor(Color.GREEN);

            g.draw(new Line2D.Double(x, y, x, y - 98. / zoom));
            g.draw(new Line2D.Double(x - 8. / zoom, y - 100. / zoom + 8. / zoom, x, y - 100. / zoom));
            g.draw(new Line2D.Double(x + 8. / zoom, y - 100. / zoom + 8. / zoom, x, y - 100. / zoom));
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (scene == null) return;
            double nzoom = zoom - e.getPreciseWheelRotation();
            if (nzoom < 1) nzoom = 1;
            zoom = nzoom;
            mouseMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (scene == null) return;
            if (dragging) {
                viewPositionX += ((double) (lastDraggingPosition.x - e.getLocationOnScreen().x) / zoom);
                viewPositionY += ((double) (lastDraggingPosition.y - e.getLocationOnScreen().y) / zoom);
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
                repaint();
            }

            if (moving) {
                if (selectedEntity != null) {
                    if (xArrowSelected)
                        selectedEntity.setPosition(selectedEntity.position().x - ((double) (lastDraggingPosition.x - e.getLocationOnScreen().x) / zoom), selectedEntity.position().y);
                    if (yArrowSelected)
                        selectedEntity.setPosition(selectedEntity.position().x, selectedEntity.position().y - ((double) (lastDraggingPosition.y - e.getLocationOnScreen().y) / zoom));
                    lastDraggingPosition.setLocation(e.getLocationOnScreen());
                }
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (scene == null) return;
            mousePositionX = e.getX() / zoom + viewPositionX + getWidth() / 2. - (getWidth() / 2. / zoom);
            mousePositionY = e.getY() / zoom + viewPositionY + getHeight() / 2. - (getHeight() / 2. / zoom);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (scene == null) return;
            for (EditorEntity entity : scene.entities()) {
                Dimension d = entity.size();
                Point2D.Double p = entity.position();
                if (p.x - d.width / 2. > mousePositionX && p.x + d.width / 2. < mousePositionX) {
                    System.out.println(entity);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (scene == null) return;
            if (e.getButton() == MouseEvent.BUTTON2) {
                dragging = true;
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
            }

            if (e.getButton() == MouseEvent.BUTTON1) {
                moving = true;
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragging = false;
            moving = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) placeInGrid = true;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) RIGHT_TIMER.start();
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) LEFT_TIMER.start();
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) UP_TIMER.start();
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) DOWN_TIMER.start();

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) placeInGrid = false;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) RIGHT_TIMER.stop();
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) LEFT_TIMER.stop();
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) UP_TIMER.stop();
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) DOWN_TIMER.stop();

            repaint();
        }
    }

    public File projectRoot() {
        return projectRoot;
    }

    public EditorWindow setProjectRoot(File folder) {
        setTitle(Srd.TITLE + " (" + folder.getAbsolutePath() + ")");
        this.projectRoot = folder;
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(new File(folder, "project.yep")))) {
            setProject((EditorProject) oi.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        reloadEditor();
        return this;
    }

    public File sceneFile() {
        return sceneFile;
    }

    public EditorWindow setSceneFile(File sceneFile) {
        this.sceneFile = sceneFile;
        if (sceneFile != null) {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(sceneFile))) {
                setScene((EditorScene) oi.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public EditorScene scene() {
        return scene;
    }

    public void reloadEditor() {
        SwingUtilities.updateComponentTreeUI(EditorWindow.this);
    }

    private EditorWindow setScene(EditorScene scene) {
        this.scene = scene;
        reloadEditor();
        return this;
    }

    public EditorProject project() {
        return project;
    }

    private EditorWindow setProject(EditorProject project) {
        this.project = project;
        return this;
    }
}
