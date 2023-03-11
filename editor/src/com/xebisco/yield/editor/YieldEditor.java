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

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YieldEditor {

    private static File project;
    private JFrame frame;
    public Map<String, String> toLoadIcons = new HashMap<>();
    private final File projectFile;
    public final Map<String, ImageIcon> icons = new HashMap<>();
    private static final List<File> recentOpenedProjects;
    private static final File DATA_DIR = new File(System.getenv("APPDATA"), "YieldEditor");

    static {
        if (!DATA_DIR.exists()) {//noinspection ResultOfMethodCallIgnored
            DATA_DIR.mkdir();
            try {
                File file = new File(DATA_DIR, "recentOpenedProjects.ser");
                file.createNewFile();
                ObjectOutput output = new ObjectOutputStream(new FileOutputStream(file));
                output.writeObject(new ArrayList<>());
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (ObjectInput objectInput = new ObjectInputStream(new FileInputStream(new File(DATA_DIR, "recentOpenedProjects.ser")))) {
            //noinspection unchecked
            recentOpenedProjects = (List<File>) objectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        File project;
        if (args.length != 1) {
            if (args.length == 0) project = null;
            else {
                JOptionPane.showMessageDialog(null, "Wrong argument number", "Launch Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Wrong argument number");
            }
        } else {
            project = new File(args[0]);
        }

        new YieldEditor(project);
    }

    public void loadAssets() {

        toLoadIcons.put("editorLogo", "editorLogo.png");
        icons.put("yieldIcon", new ImageIcon(Objects.requireNonNull(YieldEditor.class.getResource("yieldIcon.png"))));
        icons.put("splashScreen", new ImageIcon(Objects.requireNonNull(YieldEditor.class.getResource("splashScreen.png"))));
        toLoadIcons.put("yieldIcon64x64rsl", null);
        toLoadIcons.put("starredIcon", "starredIcon.png");
        toLoadIcons.put("notStarredIcon", "notStarredIcon.png");


        JFrame splash = new JFrame();
        JProgressBar progressBar = new JProgressBar();
        splash.setIconImage(icons.get("yieldIcon").getImage());
        splash.add(new JLabel(icons.get("splashScreen")), BorderLayout.NORTH);
        splash.add(progressBar, BorderLayout.CENTER);
        splash.setUndecorated(true);
        splash.pack();
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);
        splash.setShape(new RoundRectangle2D.Double(0, 0, splash.getWidth(), splash.getHeight(), 25, 25));
        progressBar.setMinimum(0);
        progressBar.setMaximum(toLoadIcons.size());
        progressBar.setValue(0);
        int index = 0;
        for (String i : toLoadIcons.keySet()) {
            ImageIcon imageIcon;
            if (i.endsWith("rsl")) {
                Matcher matcher = Pattern.compile("^([^0-9]+)([0-9]+)x([0-9]+)rsl$").matcher(i);
                //noinspection ResultOfMethodCallIgnored
                matcher.matches();
                imageIcon = new ImageIcon(icons.get(matcher.group(1)).getImage().getScaledInstance(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Image.SCALE_SMOOTH));
            } else {
                imageIcon = new ImageIcon(Objects.requireNonNull(YieldEditor.class.getResource(toLoadIcons.get(i))));
            }
            icons.put(i, imageIcon);
            progressBar.setValue(index + 1);
            index++;
            /*try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        }
        splash.dispose();
        /*JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

            }
        };*/
    }

    public JMenuBar createMenuBar() {
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        JMenuItem item = new JMenuItem(new AbstractAction("Open Project...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new YieldEditor(null);
            }
        });
        menu.add(item);

        JMenu menu2 = new JMenu("Open Recent");
        menu2.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                menu2.removeAll();
                if (recentOpenedProjects.size() == 0)
                    menu2.add(new JMenuItem("No recent projects"));
                for (File project : recentOpenedProjects) {
                    menu2.add(new JMenuItem(new AbstractAction(project.getName()) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new YieldEditor(project);
                        }
                    }));
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });


        menu.add(menu2);

        mainMenuBar.add(menu);
        return mainMenuBar;
    }

    public YieldEditor(File projectFile) {
        if (projectFile == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                projectFile = fileChooser.getSelectedFile();
            }
        }
        if (projectFile != null) {
            this.projectFile = projectFile;
            if (!projectFile.exists()) {
                JOptionPane.showMessageDialog(null, "Project does not exist", "Launch Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Project does not exist");
            }

            if (projectFile.isDirectory())
                projectFile = new File(projectFile, "project.ser");
            if (!projectFile.exists()) {
                JOptionPane.showMessageDialog(null, "Is not a project", "Launch Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Is not a project");
            }
            init();
        } else {
            this.projectFile = null;
        }
    }

    private void init() {
        loadAssets();
        frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                recentOpenedProjects.add(projectFile);

                for (ListIterator<File> iterator = recentOpenedProjects.listIterator(); iterator.hasNext(); ) {
                    File customer = iterator.next();
                    if (Collections.frequency(recentOpenedProjects, customer) > 1) {
                        iterator.remove();
                    }
                }
                AtomicInteger i = new AtomicInteger();
                recentOpenedProjects.removeIf(p -> i.addAndGet(1) > 30);

                try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(new File(DATA_DIR, "recentOpenedProjects.ser")))) {
                    output.writeObject(recentOpenedProjects);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.setJMenuBar(createMenuBar());
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setTitle(projectFile.getName() + " - Yield Editor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
}
