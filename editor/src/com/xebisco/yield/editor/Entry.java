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

package com.xebisco.yield.editor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.xebisco.yield.editor.explorer.Explorer;
import com.xebisco.yield.editor.prop.BooleanProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Entry {
    public static JDialog splashDialog;
    public final static String RUN;

    static {
        RUN = "run_" + Integer.toHexString(new Random().nextInt());
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "True");
        Locale.setDefault(Locale.US);
        //IntelliJTheme.setup(Entry.class.getResourceAsStream("/DarkPurple.theme.json"));
        FlatMacDarkLaf.setup();
        splashDialog(null);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Assets.projects != null)
                Projects.saveProjects();
            if (Assets.engineInstalls != null)
                Projects.saveInstalls();
        }, "Editor Save"));
        try {
            loadEverything();
        } catch (Error e) {
            HashMap<String, Prop[]> props = new HashMap<>();
            props.put("Resolve launch error", new Prop[]{new BooleanProp("Delete recent projects list", true), new BooleanProp("Delete editor data", false)});
            new PropsWindow(props, () -> {
                if ((boolean) Objects.requireNonNull(Props.get(props.get("Resolve launch error"), "Delete recent projects list")).getValue()) {
                    File projectsFile = new File(Utils.EDITOR_DIR, "projects.ser");
                    projectsFile.delete();
                }

                if ((boolean) Objects.requireNonNull(Props.get(props.get("Resolve launch error"), "Delete editor data")).getValue()) {
                    Utils.EDITOR_DIR.delete();
                }

                JOptionPane.showMessageDialog(null, "Please restart the editor.");
                System.exit(0);
            }, null, "Error in editor launch: " + e.getClass().getSimpleName());
        }
        Assets.init();
        Files.walkFileTree(new File(Utils.EDITOR_DIR, "out").toPath(),
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
        CompletableFuture.runAsync(() -> {
                    if (splashDialog != null)
                        splashDialog.dispose();
                    SwingUtilities.invokeLater(() -> {
                        if (args.length == 1) {
                            Project project;
                            try (ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(args[0])))) {
                                project = (Project) oi.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            if (Projects.checkInstalls()) {
                                new Editor(project);
                            } else {
                                openProjects();
                            }
                        } else if (args.length == 0) {
                            if (Assets.lastOpenedProject != null)
                                new Editor(Assets.lastOpenedProject);
                            else
                                openProjects();
                        } else throw new IllegalStateException("Wrong arguments");
                    });
                }
        );
    }

    private static void loadEverything() {
        loadImage("editorLogo.png");
        loadImage("editorLogoSmall.png");
        loadImage("yieldIcon.png");
        loadImage("yieldSmallIcon.png");
        loadImage("closeIcon.png");
        loadImage("selectedCloseIcon.png");
        loadImage("uploadIcon.png");
        loadImage("uploadIcon16.png");
        loadImage("xarrow.png");
        loadImage("yarrow.png");
        loadImage("sxarrow.png");
        loadImage("syarrow.png");
        loadImage("reloadIcon.png");
        loadImage("runIcon.png");
        loadImage("searchIcon.png");
        loadImage("workspaceIcon.png");
        loadImage("bkg.png");
        loadImage("addIcon.png");
        loadImage("backIcon.png");
        loadImage("windowIcon.png");
        loadImage("transformIcon.png");
        loadImage("editIcon.png");
        loadImage("scriptIcon.png");
        loadImage("physicsIcon.png");
        loadImage("animationIcon.png");
        loadImage("graphicalIcon.png");
        loadImage("audioIcon.png");
        loadImage("toolbarBkg.png");
        loadImage("sceneIcon.png");
        loadImage("sceneIcon32.png");
        loadImage("handIcon.png");
        loadImage("selectIcon.png");
        loadImage("optionsIcon.png");
        loadImage("zoomInIcon.png");
        loadImage("zoomOutIcon.png");
    }

    private static void loadImage(String n) {
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
    }

    public static void openProjects() {
        JFrame frame = new JFrame("Projects");
        frame.setSize(new Dimension(800, 650));
        frame.setMinimumSize(new Dimension(800, 650));
        frame.setMaximumSize(new Dimension(1000, 720));

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(Assets.images.get("yieldIcon.png").getImage());

        frame.setContentPane(new Projects(frame));
        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                frame.repaint();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void splashDialog(String title) {
        splashDialog = new JDialog();
        splashDialog.setAlwaysOnTop(true);
        splashDialog.setTitle("Yield 5 Editor");
        splashDialog.setUndecorated(true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        splashDialog.add(progressBar, BorderLayout.SOUTH);
        splashDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        if (title != null) {
            splashDialog.add(new JLabel(title), BorderLayout.CENTER);
        } else {
            splashDialog.add(new JLabel(new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/splash.png")))), BorderLayout.CENTER);
        }
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setVisible(true);
    }
}
