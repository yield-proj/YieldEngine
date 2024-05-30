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

import com.xebisco.yield.editor.app.*;
import com.xebisco.yield.editor.app.code.CodePanel;
import com.xebisco.yield.editor.app.config.GameViewSettings;
import com.xebisco.yield.editor.app.config.PhysicsSettings;
import com.xebisco.yield.editor.app.run.PlayPanel;
import com.xebisco.yield.editor.runtime.pack.EditorEntity;
import com.xebisco.yield.editor.runtime.pack.EditorScene;
import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.props.Prop;
import com.xebisco.yield.uiutils.props.PropPanel;
import com.xebisco.yield.uiutils.props.TextFieldProp;
import com.xebisco.yield.utils.Loading;
import com.xebisco.yield.utils.Pair;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Editor extends JFrame {
    private boolean running;
    public final URL yieldEngineJar;
    public final ClassLoader yieldEngineClassLoader;

    public final Class<? extends Annotation> VISIBLE_ANNOTATION, HIDE_ANNOTATION, SIZE_ANNOTATION, INT_COLOR_ANNOTATION;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final ConfigPanel editorConfig;

    private final Project project;
    private final JPanel scenePanel = new JPanel(new BorderLayout());

    private final PlayPanel playPanel = new PlayPanel();

    private final JButton playButton, playGlobalButton, pauseButton, stopButton;

    private final ConfigPanel configPanel;
    private final CodePanel codePanel;

    private final Map<String, Object[]> configMap = new HashMap<>();

    private final List<File> scripts = new ArrayList<>();

    public void updateScripts() {
        scripts.clear();
        for(File file : Objects.requireNonNull(project.scriptsDirectory().listFiles())) {
            if(file.getName().endsWith(".java")) {
                scripts.add(file);
            }
        }
    }

    private final Timer updateFilesTimer = new Timer(1000, e -> {
        updateScripts();
    });

    public Editor(Project project) {
        this.project = project;
        try {
            Image loadedIcon = ImageIO.read(new File(project.path(), "Assets/icon.png")).getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics g = icon.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 15, 15);
            g.drawImage(loadedIcon, 1, 1, null);
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, 15, 15);
            g.dispose();
            setIconImage(icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setTitle("Yield Editor | " + project.name());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int a = JOptionPane.showConfirmDialog(Editor.this, "Save project before exiting?", "Confirm Exit", JOptionPane.YES_NO_CANCEL_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    project.saveProjectFile();
                }
                if (a == JOptionPane.YES_OPTION || a == JOptionPane.NO_OPTION) {
                    configPanel.saveValues();
                    try {
                        Global.appProps.setProperty("editor_config", Entry.convertToString(editorConfig.values()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    updateFilesTimer.stop();
                    dispose();
                    new ProjectEditor(new File(Global.appProps.getProperty("lastWorkspace"), "workspace.ser"), null);
                }
            }
        });

        try {
            yieldEngineJar = new File(project.path(), "Libraries/yield-core.jar").toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        yieldEngineClassLoader = new URLClassLoader(new URL[]{yieldEngineJar}, null);

        try {
            //noinspection unchecked
            VISIBLE_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.Visible");
            //noinspection unchecked
            HIDE_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.HideComponent");
            //noinspection unchecked
            SIZE_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.AffectsEditorEntitySize");
            //noinspection unchecked
            INT_COLOR_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.IntColor");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        setMinimumSize(new Dimension(1280, 720));


        try {
            configPanel = new ConfigPanel(new String[]{"Application", "Window"}, new ConfigProp[][]{new ConfigProp[]{new ConfigProp(project, Editor.this)}, new ConfigProp[]{new ConfigProp(yieldEngineClassLoader.loadClass("com.xebisco.yield.PlatformInit"), Editor.this)}});
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (project.projectSettings() == null) {
            project.setProjectSettings(configPanel.values());
        } else {
            configPanel.insert(project.projectSettings(), this);
        }

        configPanel.refresh();


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenu fileNewMenu = new JMenu("New");

        fileNewMenu.add(new AbstractAction("Script File...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final boolean[] err = {false};
                do {
                    JDialog newDialog = new JDialog(Editor.this, true);
                    newDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    newDialog.add(new TitleLabel("New Script File", null), BorderLayout.NORTH);
                    Prop[] props = new Prop[]{new TextFieldProp("Script Name", "EmptyScript", false),};
                    PropPanel newProjectProps = new PropPanel(props);
                    newProjectProps.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
                    newDialog.add(newProjectProps);
                    newDialog.setMinimumSize(new Dimension(450, 300));
                    newDialog.setLocationRelativeTo(Editor.this);

                    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                    JButton finish = new JButton(new AbstractAction("Finish") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Map<String, Serializable> values = PropPanel.values(props);
                            String s = (String) values.get("Script Name");
                            if (s.isEmpty()) {
                                err[0] = false;
                                JOptionPane.showMessageDialog(Editor.this, "Script Name must not be empty");
                                return;
                            }

                            if (!Srd.validateClassName(s)) {
                                err[0] = false;
                                JOptionPane.showMessageDialog(Editor.this, "Script Name must be a valid class name");
                                return;
                            }
                            AtomicBoolean alreadyExists = new AtomicBoolean(false);
                            scripts.forEach(p -> {
                                if (p.getName().replaceAll("\\.java$", "").equals(s)) {
                                    alreadyExists.set(true);
                                }
                            });


                            if (alreadyExists.get()) {
                                err[0] = false;
                                JOptionPane.showMessageDialog(Editor.this, "A script with this name already exists");
                                return;
                            }

                            File newFile = new File(project.scriptsDirectory(), s + ".java");

                            try(FileWriter writer = new FileWriter(newFile)) {
                                writer
                                        .append("import com.xebisco.yield.*;\n\npublic class ")
                                        .append(s)
                                        .append(" extends ComponentBehavior {\n\n}");
                            } catch (IOException ex) {
                                err[0] = false;
                                JOptionPane.showMessageDialog(Editor.this, ex.getMessage());
                                return;
                            }

                            tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(codePanel));
                            codePanel.addJavaCodeFile(newFile);
                            newDialog.dispose();
                        }
                    });
                    newDialog.getRootPane().setDefaultButton(finish);
                    bottom.add(finish);
                    bottom.add(new JButton(new AbstractAction("Cancel") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            err[0] = false;
                            newDialog.dispose();
                        }
                    }));
                    newDialog.add(bottom, BorderLayout.SOUTH);

                    newDialog.setVisible(true);
                } while (err[0]);
            }
        });

        //TODO New Text File...

        fileNewMenu.addSeparator();

        fileNewMenu.add(new AbstractAction("Scene...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final boolean[] err = {false};
                do {
                    JDialog newSceneDialog = new JDialog(Editor.this, true);
                    newSceneDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    newSceneDialog.add(new TitleLabel("New Scene", null), BorderLayout.NORTH);
                    Prop[] props = new Prop[]{new TextFieldProp("Scene Name", "Empty Scene", false),};
                    PropPanel newProjectProps = new PropPanel(props);
                    newProjectProps.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
                    newSceneDialog.add(newProjectProps);
                    newSceneDialog.setMinimumSize(new Dimension(450, 300));
                    newSceneDialog.setLocationRelativeTo(Editor.this);

                    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                    JButton finish = new JButton(new AbstractAction("Finish") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Map<String, Serializable> values = PropPanel.values(props);
                            String s = (String) values.get("Scene Name");
                            if (s.isEmpty()) {
                                err[0] = false;
                                JOptionPane.showMessageDialog(Editor.this, "Scene Name must not be empty");
                                return;
                            }
                            AtomicBoolean alreadyExists = new AtomicBoolean(false);
                            project.scenes().forEach(p -> {
                                if (p.name().equals(s)) {
                                    alreadyExists.set(true);
                                }
                            });


                            if (alreadyExists.get()) {
                                err[0] = false;
                                JOptionPane.showMessageDialog(Editor.this, "A scene with this name already exists");
                                return;
                            }

                            EditorScene scene = new EditorScene().setName(s);
                            project.scenes().add(scene);
                            newSceneDialog.dispose();

                            if (JOptionPane.showConfirmDialog(Editor.this, "Open " + s + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                openScene(scene);
                            }
                        }
                    });
                    newSceneDialog.getRootPane().setDefaultButton(finish);
                    bottom.add(finish);
                    bottom.add(new JButton(new AbstractAction("Cancel") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            err[0] = false;
                            newSceneDialog.dispose();
                        }
                    }));
                    newSceneDialog.add(bottom, BorderLayout.SOUTH);

                    newSceneDialog.setVisible(true);
                } while (err[0]);
            }
        });

        fileMenu.add(fileNewMenu);

        fileMenu.add(new AbstractAction("Delete Scene") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setTitle("Delete Scene");
                JList<EditorScene> scenes = new JList<>(project.scenes().toArray(new EditorScene[0]));
                scenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                scenes.setSelectedIndex(0);
                dialog.add(scenes);
                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                JButton deleteButton = new JButton(new AbstractAction("Delete") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (JOptionPane.showConfirmDialog(Editor.this, "Delete " + scenes.getSelectedValue().name() + "? This action cannot be undone.", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            if (scenePanel.getComponent(0) instanceof ScenePanel sp && sp.scene() == scenes.getSelectedValue())
                                closeScene();
                            project.scenes().remove(scenes.getSelectedValue());
                        }
                        dialog.dispose();
                    }
                });

                bottom.add(deleteButton);

                dialog.getRootPane().setDefaultButton(deleteButton);

                if (scenes.getModel().getSize() == 0) deleteButton.setEnabled(false);

                dialog.add(bottom, BorderLayout.SOUTH);
                dialog.setMinimumSize(new Dimension(300, 200));
                dialog.setLocationRelativeTo(Editor.this);
                dialog.setVisible(true);
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("Editor Settings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JDialog dialog = new JDialog(Editor.this, true);
                    dialog.setTitle("Editor Settings");
                    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    ConfigPanel cfg = new ConfigPanel(editorConfig.pages(), editorConfig.configs());
                    cfg.refresh();
                    dialog.add(cfg);

                    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                    JButton applyButton = new JButton(new AbstractAction("OK") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(() -> {
                                cfg.saveValues();
                                editorConfig.insert(cfg.values(), Editor.this);

                                HashMap<String, HashMap<String, Serializable>> values = editorConfig.values();

                                for (String t : configMap.keySet()) {
                                    values.get(t).forEach((s, s1) -> {
                                        for (Object o : configMap.get(t)) {
                                            if (o.getClass().getSimpleName().equals(s)) {
                                                try {
                                                    //noinspection unchecked
                                                    Loading.applyPropsToObject((ArrayList<Pair<Pair<String, String>, String[]>>) s1, o);
                                                } catch (NoSuchFieldException | IllegalAccessException |
                                                         NoSuchMethodException | InstantiationException |
                                                         InvocationTargetException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                                break;
                                            }
                                        }
                                    });
                                }
                                dialog.dispose();
                            });
                        }
                    });
                    bottom.add(applyButton);
                    dialog.getRootPane().setDefaultButton(applyButton);

                    bottom.add(new JButton(new AbstractAction("Close") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dialog.dispose();
                        }
                    }));

                    dialog.add(bottom, BorderLayout.SOUTH);


                    dialog.setSize(600, 400);

                    dialog.setLocationRelativeTo(Editor.this);
                    dialog.setVisible(true);
                });
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("Open Scene") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setTitle("Open Scene");
                JList<EditorScene> scenes = new JList<>(project.scenes().toArray(new EditorScene[0]));
                scenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                scenes.setSelectedIndex(0);
                dialog.add(scenes);
                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                JButton openButton = new JButton(new AbstractAction("Open") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openScene(scenes.getSelectedValue());
                        dialog.dispose();
                    }
                });

                bottom.add(openButton);

                dialog.getRootPane().setDefaultButton(openButton);

                if (scenes.getModel().getSize() == 0) openButton.setEnabled(false);

                dialog.add(bottom, BorderLayout.SOUTH);
                dialog.setMinimumSize(new Dimension(300, 200));
                dialog.setLocationRelativeTo(Editor.this);
                dialog.setVisible(true);
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("New Prefab") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menuBar.add(fileMenu);
        fileMenu.add(new AbstractAction("Save Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.saveProjectFile();
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("Close Scene") {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeScene();
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("Close Editor") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        JMenu projectMenu = new JMenu("Project");

        projectMenu.setMnemonic(KeyEvent.VK_P);

        projectMenu.add(new AbstractAction("Project Settings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Editor.this, true);
                dialog.setTitle("Project Settings");
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                ConfigPanel cfg = new ConfigPanel(configPanel.pages(), configPanel.configs());
                dialog.add(cfg);

                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                JButton applyButton = new JButton(new AbstractAction("OK") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cfg.saveValues();
                        configPanel.insert(cfg.values(), Editor.this);
                        project.setProjectSettings(configPanel.values());
                        dialog.dispose();
                    }
                });
                bottom.add(applyButton);
                dialog.getRootPane().setDefaultButton(applyButton);

                bottom.add(new JButton(new AbstractAction("Close") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                }));

                dialog.add(bottom, BorderLayout.SOUTH);


                dialog.setSize(600, 400);

                dialog.setLocationRelativeTo(Editor.this);
                dialog.setVisible(true);
            }
        });

        projectMenu.add(new AbstractAction("Manage Scenes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Editor.this, true);
                dialog.setTitle("Manage Scenes");

                JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                JPanel panel = new JPanel(new BorderLayout());
                splitPane.setBottomComponent(panel);

                Timer save = new Timer(500, null);
                save.start();

                JList<EditorScene> scenes = new JList<>(project.scenes().toArray(new EditorScene[0]));
                scenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                scenes.addListSelectionListener(e1 -> {
                    panel.removeAll();
                    panel.setBorder(BorderFactory.createTitledBorder(scenes.getSelectedValue().name()));
                    ConfigPanel confp;
                    panel.add(confp = new ConfigPanel(new String[]{"Properties"}, new ConfigProp[][]{new ConfigProp[]{new ConfigProp(scenes.getSelectedValue(), Editor.this)}}));
                    panel.updateUI();
                    for (ActionListener al : save.getActionListeners())
                        save.removeActionListener(al);

                    EditorScene scene = scenes.getSelectedValue();

                    save.addActionListener(e2 -> {
                        confp.saveValues();
                        try {
                            //noinspection unchecked
                            Loading.applyPropsToObject((List<Pair<Pair<String, String>, String[]>>) confp.values().get("Properties").get("EditorScene"), scene);
                        } catch (NoSuchFieldException | InstantiationException | IllegalAccessException |
                                 NoSuchMethodException | InvocationTargetException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (scene.name().isEmpty()) {
                            scene.setName("Unnamed scene");
                        }
                        panel.setBorder(BorderFactory.createTitledBorder(scenes.getSelectedValue().name()));
                        for (EditorScene s : project.scenes()) {
                            if (s != scene && s.name().equals(scene.name())) {
                                int i = 0;
                                while (true) {
                                    String nn = scene.name() + " " + i;
                                    boolean repeat = false;
                                    for (EditorScene s1 : project.scenes()) {
                                        if (s1 != scene && s1.name().equals(nn)) {
                                            i++;
                                            repeat = true;
                                        }
                                    }
                                    if (!repeat) {
                                        scene.setName(nn);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        scenes.updateUI();
                    });
                });
                JScrollPane scenesScrollPane = new JScrollPane(scenes);
                scenesScrollPane.setBorder(BorderFactory.createTitledBorder("Scenes"));
                splitPane.setTopComponent(scenesScrollPane);
                scenes.setSelectedIndex(0);

                splitPane.setDividerLocation(150);
                dialog.add(splitPane);

                dialog.setMinimumSize(new Dimension(600, 500));
                dialog.setLocationRelativeTo(Editor.this);
                dialog.setVisible(true);
                save.stop();
            }
        });

        menuBar.add(projectMenu);

        JMenu buildMenu = new JMenu("Build");
        projectMenu.setMnemonic(KeyEvent.VK_B);

        buildMenu.add(new AbstractAction("Clear Build") {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.clearBuild();
            }
        });

        buildMenu.add(new AbstractAction("Create Configuration File") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkError(project.createConfigFile(configPanel.values(), "config.ser", "Build"), "Create Config File");
            }
        });

        buildMenu.add(new AbstractAction("Compile Scripts") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkError(project.compileScripts(), "Compile");
            }
        });

        buildMenu.add(new AbstractAction("Pack Assets") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkError(project.packAssets("Build/data"), "Assets Packing");
            }
        });

        buildMenu.add(new AbstractAction("Pack Scenes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkError(project.packScenes("Build/scenes"), "Scenes Packing");
            }
        });

        buildMenu.add(new AbstractAction("Create Manifest") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkError(project.createManifest(), "Manifest");
            }
        });

        buildMenu.addSeparator();

        buildMenu.add(new AbstractAction("Clear Output") {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.clearOutput();
            }
        });

        buildMenu.add(new AbstractAction("Create output JAR") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkError(project.buildToJar(), "Output JAR");
            }
        });

        menuBar.add(buildMenu);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpMenu.add(new AbstractAction("About") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setResizable(false);
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setTitle("About Yield Editor");
                try {
                    dialog.add(new TitleLabel(" Yield Editor " + Global.VERSION, new ImageIcon(ImageIO.read(Objects.requireNonNull(Editor.class.getResource("/logo/logo.png"))).getScaledInstance(64, -1, Image.SCALE_SMOOTH))), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JLabel label = new JLabel("<html>" + "<p>Build: " + Global.BUILD + "</p> <br>" + "<p>A JVM game engine, a simple and efficient engine for creating 2d games in Java.</p> <br>" + "<p>Runtime: " + System.getProperty("java.runtime.version") + "</p>" + "<p>VM: " + System.getProperty("java.vm.name") + "</p> <br>" + "<small>Copyright @ 2022-2024 Xebisco. Licensed under the Apache License, Version 2.0</small>" + "</html>");
                label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                dialog.add(label);

                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                bottom.add(new JButton(new AbstractAction("OK") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                }));

                dialog.add(bottom, BorderLayout.SOUTH);

                dialog.setSize(new Dimension(460, 330));
                dialog.setLocationRelativeTo(Editor.this);
                dialog.setVisible(true);
            }
        });

        setJMenuBar(menuBar);


        JToolBar toolBar = new JToolBar();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.setBackground(getBackground());
        toolBar.addSeparator();
        toolBar.add(playButton = new JButton(new AbstractAction("", new ImageIcon(Objects.requireNonNull(Editor.class.getResource("/icons/play.png")))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                runScene(((ScenePanel) scenePanel.getComponent(0)).scene());
            }
        }));
        toolBar.add(playGlobalButton = new JButton(new AbstractAction("", new ImageIcon(Objects.requireNonNull(Editor.class.getResource("/icons/playglobal.png")))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                runScene(null);
            }
        }));
        toolBar.add(pauseButton = new JButton(new AbstractAction("", new ImageIcon(Objects.requireNonNull(Editor.class.getResource("/icons/pause.png")))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        }));
        pauseButton.setEnabled(false);

        toolBar.add(stopButton = new JButton(new AbstractAction("", new ImageIcon(Objects.requireNonNull(Editor.class.getResource("/icons/stop.png")))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        }));
        stopButton.setEnabled(false);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalGlue());

        add(toolBar, BorderLayout.NORTH);


        add(tabbedPane);
        tabbedPane.addTab("Scene Panel", scenePanel);
        tabbedPane.addTab("Code Panel", codePanel = new CodePanel(this));
        tabbedPane.addTab("Play Panel", playPanel);
        //add(scenePanel);
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

        closeScene();

        setLocationRelativeTo(null);


        try {
            editorConfig = new ConfigPanel(new String[]{"Game View", "Physics"}, new ConfigProp[][]{new ConfigProp[]{new ConfigProp(GameViewSettings.class, this)}, new ConfigProp[]{new ConfigProp(PhysicsSettings.class, this)}});
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String configString = Global.appProps.getProperty("editor_config");
        if (configString != null) {
            try {
                //noinspection unchecked
                editorConfig.insert((HashMap<String, HashMap<String, Serializable>>) Entry.convertFrom(configString), this);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < editorConfig.pages().length; i++) {
            ConfigProp[] c = editorConfig.configs()[i];
            Object[] configs = new Object[c.length];
            for (int i1 = 0; i1 < configs.length; i1++) {
                configs[i1] = c[i1].configInstance;
            }
            configMap.put(editorConfig.pages()[i], configs);
        }


        setVisible(true);
        updateFilesTimer.start();
    }

    public <T> T getSettings(String page, Class<?> clazz) {
        for (String t : configMap.keySet()) {
            if (t.equals(page)) {
                for (Object cfg : configMap.get(t)) {
                    if (cfg.getClass().equals(clazz))
                        //noinspection unchecked
                        return (T) cfg;
                }
            }
        }
        throw new IllegalArgumentException(page + " or " + clazz);
    }

    private void passEditor(EditorEntity entity, ScenePanel scenePanel) {
        scenePanel.openEntity(entity, null);
        for (EditorEntity c : entity.children()) {
            passEditor(c, scenePanel);
        }
    }

    public void openScene(EditorScene scene) {
        if (!running)
            playButton.setEnabled(true);
        scenePanel.removeAll();
        ScenePanel scenePanel;
        this.scenePanel.add(scenePanel = new ScenePanel(scene, project, this));
        for (EditorEntity e : scene.entities()) passEditor(e, scenePanel);
        scenePanel.mainP.setRightComponent(null);
        this.scenePanel.updateUI();
    }

    public void closeScene() {
        playButton.setEnabled(false);
        scenePanel.removeAll();
        JLabel noScenesLabel = new JLabel("No scenes added");
        noScenesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noScenesLabel.setVerticalAlignment(SwingConstants.CENTER);
        noScenesLabel.setForeground(noScenesLabel.getBackground().brighter().brighter());
        scenePanel.add(noScenesLabel);
        scenePanel.updateUI();
    }

    private Process runningProcess;

    public void runScene(EditorScene scene) {
        running = true;
        playButton.setEnabled(false);
        playGlobalButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        //TODO

        if (scenePanel.getComponent(0) instanceof ScenePanel sp) sp.closeEntity();

        playPanel.setWasInScenePanel(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Scene Panel"));

        tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(playPanel));
        playPanel.start();

        CompletableFuture.runAsync(() -> {
            playPanel.console().clear();
            playPanel.progress("Clearing Build...");
            project.clearBuild();
            playPanel.progress("Creating Configuration File...");
            if (checkError(project.createConfigFile(configPanel.values(), "config.ser", "Build"), "Create Config File")) {
                forceStop();
                return;
            }

            playPanel.progress("Compiling Scripts...");
            if (checkError(project.compileScripts(), "Compile")) {
                forceStop();
                return;
            }
            playPanel.progress("Packing Assets...");
            if (checkError(project.packAssets("Build/data"), "Assets Packing")) {
                forceStop();
                return;
            }

            playPanel.progress("Packing Scenes...");
            if (checkError(project.packScenes("Build/scenes"), "Scenes Packing")) {
                forceStop();
                return;
            }

            playPanel.progress("Creating Manifest...");
            if (checkError(project.createManifest(), "Manifest")) {
                forceStop();
                return;
            }

            playPanel.progress("Adding Libraries...");

            List<File> libs = Global.listf(new File(project.path(), "Libraries"));

            StringBuilder jarLibs = new StringBuilder();
            for (int i = 0; i < libs.size(); i++) {
                jarLibs.append(libs.get(i).getAbsolutePath());
                if (i < libs.size() - 1) jarLibs.append(File.pathSeparator);
            }
            ProcessBuilder processBuilder;
            if (scene != null)
                processBuilder = new ProcessBuilder().directory(new File(project.path(), "Build")).command("java", "-cp", jarLibs.toString(), "com.xebisco.yield.editor.runtime.Launcher", scene.name());
            else
                processBuilder = new ProcessBuilder().directory(new File(project.path(), "Build")).command("java", "-cp", jarLibs.toString(), "com.xebisco.yield.editor.runtime.Launcher");
            try {
                playPanel.console().println(processBuilder.command().toString());

                playPanel.progress("Running Application...");
                runningProcess = processBuilder.start();

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(runningProcess.getInputStream()));

                BufferedReader stdError = new BufferedReader(new InputStreamReader(runningProcess.getErrorStream()));

                CompletableFuture.runAsync(() -> {
                    String s;
                    while (runningProcess.isAlive()) {
                        try {
                            if ((s = stdInput.readLine()) != null) {
                                playPanel.console().println("(" + new Date() + ") " + s);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                runningProcess.waitFor();

                playPanel.progress("Shutting down process...");

                String s;

                StringBuilder o = new StringBuilder();
                while ((s = stdError.readLine()) != null) {
                    o.append(s).append("<br>");
                }

                if (!o.isEmpty())
                    JOptionPane.showMessageDialog(Editor.this, "<html>" + o + "</html>", "Running ERROR", JOptionPane.ERROR_MESSAGE);
                forceStop();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private boolean checkError(String out, String error) {
        if (out != null) {
            JOptionPane.showMessageDialog(Editor.this, "<html>" + out + "</html>", error + " Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    public void pause() {
        if (running) {
            //TODO
        }
    }

    public void stop() {
        if (runningProcess != null && runningProcess.isAlive()) {
            forceStop();
        }

    }

    public void forceStop() {
        running = false;
        playPanel.done();
        if (scenePanel.getComponent(0) instanceof ScenePanel sp && sp.scene() != null)
            playButton.setEnabled(true);
        playGlobalButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        runningProcess.destroy();
        runningProcess = null;
        if (tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Play Panel") && playPanel.wasInScenePanel())
            tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(scenePanel));
    }

    public Project project() {
        return project;
    }
}


/*
public void mouseWheelMoved(MouseWheelEvent e) {
            Point2D before = null;
            Point2D after = null;

            AffineTransform originalTransform = new AffineTransform(at);
            AffineTransform zoomedTransform = new AffineTransform();

            try {
                before = originalTransform.inverseTransform(e.getPoint(), null);
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }

            zoom *= 1-(e.getPreciseWheelRotation()/5);
            ((Painter) Frame1.painter).setScale(zoom/100);

            zoomedTransform.setToIdentity();
            zoomedTransform.translate(getWidth()/2, getHeight()/2);
            zoomedTransform.scale(scale, scale);
            zoomedTransform.translate(-getWidth()/2, -getHeight()/2);
            zoomedTransform.translate(translateX, translateY);

            try {
                after = zoomedTransform.inverseTransform(e.getPoint(), null);
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }


            double deltaX = after.getX() - before.getX();
            double deltaY = after.getY() - before.getY();
            translate(deltaX,deltaY);

            Frame1.painter.repaint();
}
 */