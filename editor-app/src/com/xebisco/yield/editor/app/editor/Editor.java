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

import com.xebisco.yield.editor.app.ConfigPanel;
import com.xebisco.yield.editor.app.Global;
import com.xebisco.yield.editor.app.Project;
import com.xebisco.yield.editor.app.TitleLabel;
import com.xebisco.yield.editor.app.run.PlayPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Editor extends JFrame {
    private boolean running;
    public final URL yieldEngineJar;
    public final ClassLoader yieldEngineClassLoader;

    public final Class<? extends Annotation> VISIBLE_ANNOTATION, HIDE_ANNOTATION, SIZE_ANNOTATION;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final Project project;
    private final JPanel scenePanel = new JPanel(new BorderLayout());

    private final PlayPanel playPanel = new PlayPanel();


    private final JButton playButton, playGlobalButton, pauseButton, stopButton;

    private final ConfigPanel configPanel;

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
                    dispose();
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

        if(project.projectSettings() == null) {
            project.setProjectSettings(configPanel.values());
        } else {
            configPanel.insert(project.projectSettings());
        }

        configPanel.refresh();


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("New Prefab") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menuBar.add(fileMenu);
        fileMenu.add(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.saveProjectFile();
            }
        });

        fileMenu.addSeparator();
        fileMenu.add(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        JMenu projectMenu = new JMenu("Project");

        projectMenu.add(new AbstractAction("Project Settings...") {
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
                        configPanel.insert(cfg.values());
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

                bottom.add(new JButton(new AbstractAction("Apply") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cfg.saveValues();
                        configPanel.insert(cfg.values());
                        project.setProjectSettings(configPanel.values());
                    }
                }));

                dialog.add(bottom, BorderLayout.SOUTH);


                dialog.setSize(600, 400);

                dialog.setLocationRelativeTo(Editor.this);
                dialog.setVisible(true);
            }
        });

        menuBar.add(projectMenu);

        JMenu buildMenu = new JMenu("Build");

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
                runScene(null);
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
        tabbedPane.addTab("Play Panel", playPanel);
        //add(scenePanel);
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

        //TODO actual scenes
        scenePanel.removeAll();
        scenePanel.add(new ScenePanel(new EditorScene(), project, this));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Process runningProcess;

    public void runScene(EditorScene scene) {
        running = true;
        playButton.setEnabled(false);
        playGlobalButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        //TODO

        playPanel.setWasInScenePanel(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Scene Panel"));

        tabbedPane.setSelectedIndex(1);
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

            ProcessBuilder processBuilder = new ProcessBuilder().directory(new File(project.path(), "Build")).command("java", "-cp", jarLibs.toString(), "com.xebisco.yield.editor.runtime.Launcher");
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
        playButton.setEnabled(true);
        playGlobalButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        runningProcess.destroy();
        runningProcess = null;
        if (tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Play Panel") && playPanel.wasInScenePanel())
            tabbedPane.setSelectedIndex(0);
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