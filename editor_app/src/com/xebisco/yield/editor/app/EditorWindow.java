package com.xebisco.yield.editor.app;

import com.xebisco.yield.editor.app.props.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class EditorWindow extends JFrame {

    private File projectRoot;
    private File sceneFile;
    private EditorScene scene;
    private EditorProject project;
    private Dimension gridSize = new Dimension(16, 16);

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
        setMinimumSize(new Dimension(400, 300));


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
                if (projectRoot == null || scene == null) {
                    JOptionPane.showMessageDialog(EditorWindow.this, Srd.LANG.getProperty("se_noFolderError"));
                    return;
                }


            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_newWindow")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorWindow window = new EditorWindow();
                window.setProjectRoot(projectRoot);
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

        fileMenu.addSeparator();

        JMenu preferencesMenu = new JMenu(Srd.LANG.getProperty("se_menuBar_file_preferences"));

        fileMenu.add(preferencesMenu);

        preferencesMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_file_preferences_allSettings")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Prop[]> sections = new HashMap<>();

                sections.put("Scene View", new Prop[]{
                        new SizeProp("Grid size", gridSize)
                });
                sections.put("Engine", new Prop[]{
                        new TextFieldProp("Yield Engine Core Jar URI", Srd.yieldEngineURL)
                });

                new SettingsPropDialog(EditorWindow.this, sections, () -> {
                    Map<String, Serializable> sceneViewValues = PropPanel.values(sections.get("Scene View"));
                    gridSize = (Dimension) sceneViewValues.get("Grid size");

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
                        }
                        catch (Exception ex){
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

        sceneMenu.add(new JMenuItem(new AbstractAction(Srd.LANG.getProperty("se_menuBar_scene_preferences")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Prop[]> sections = new HashMap<>();

                sections.put("Scene Properties", new Prop[]{
                        new TextFieldProp("Scene Name", scene.name()),
                        new ColorProp("Background Color", scene.backgroundColor())
                });
                new SettingsPropDialog(EditorWindow.this, sections, () -> {
                    Map<String, Serializable> values = PropPanel.values(sections.get("Scene Properties"));
                    scene.setName((String) values.get("Scene Name"));
                    scene.setBackgroundColor((Color) values.get("Background Color"));
                });
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


    class GameView extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

        private boolean dragging;
        private Point lastDraggingPosition = new Point();
        private double viewPositionX, viewPositionY, mousePositionX, mousePositionY;

        private final JLabel xLabel, yLabel, zoomLabel;

        private double zoom = 1;

        public GameView(JLabel xLabel, JLabel yLabel, JLabel zoomLabel) {
            this.xLabel = xLabel;
            this.yLabel = yLabel;
            this.zoomLabel = zoomLabel;
            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
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

                g.setColor(new Color(255 - scene.backgroundColor().getRed(), 255 - scene.backgroundColor().getGreen(), 255 - scene.backgroundColor().getBlue(), 60));

                g2.setStroke(new BasicStroke((float) (1 / zoom)));

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

        private void drawArrows(Graphics2D g, double x, double y) {
            //X Arrow
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke((float) (4 / zoom)));
            g.draw(new Line2D.Double(x, y, x + 98. / zoom, y));
            g.draw(new Line2D.Double(x + 100. / zoom - 8. / zoom, y - 8. / zoom, x + 100. / zoom, y));
            g.draw(new Line2D.Double(x + 100. / zoom - 8. / zoom, y + 8. / zoom, x + 100. / zoom, y));


            //Y Arrow
            g.setColor(Color.GREEN);
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
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePositionX = e.getX() / zoom + viewPositionX + getWidth() / 2. - (getWidth() / 2. / zoom);
            mousePositionY = e.getY() / zoom + viewPositionY + getHeight() / 2. - (getHeight() / 2. / zoom);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON2) {
                dragging = true;
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragging = false;
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
            System.out.println("aaaa");
        }

        @Override
        public void keyReleased(KeyEvent e) {

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
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(sceneFile))) {
            setScene((EditorScene) oi.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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
