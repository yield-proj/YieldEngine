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

import com.formdev.flatlaf.IntelliJTheme;
import com.xebisco.yield.editor.prop.BooleanProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Entry {
    public static JDialog splashDialog;

    public static void main(String[] args) {
        //System.setProperty("sun.java2d.opengl", "True");
        Locale.setDefault(Locale.US);
        IntelliJTheme.setup(Entry.class.getResourceAsStream("/DarkPurple.theme.json"));
        splashDialog(null);
        try {
            loadEverything();
        } catch (Error e) {
            HashMap<String, Prop[]> props = new HashMap<>();
            props.put("Resolve launch error", new Prop[]{new BooleanProp("Delete recent projects list", true), new BooleanProp("Delete editor data", false)});
            new PropsWindow(props, () -> {
                if ((boolean) Objects.requireNonNull(Props.get(props.get("Resolve launch error"), "Delete recent projects list")).getValue()) {
                    new File(Utils.defaultDirectory() + "/.yield_editor");
                    File projectsFile = new File(Utils.defaultDirectory() + "/.yield_editor", "projects.ser");
                    projectsFile.delete();
                }

                if ((boolean) Objects.requireNonNull(Props.get(props.get("Resolve launch error"), "Delete editor data")).getValue()) {
                    new File(Utils.defaultDirectory() + "/.yield_editor").delete();
                }

                JOptionPane.showMessageDialog(null, "Please restart the editor.");
                System.exit(0);
            }, null, "Error in editor launch: " + e.getClass().getSimpleName());
        }
        Assets.init();
        SwingUtilities.invokeLater(() -> {
            if (args.length == 1) {
                Project project;
                try (ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(args[0])))) {
                    project = (Project) oi.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                new Editor(project);
                if (splashDialog != null)
                    splashDialog.dispose();
            } else if (args.length == 0) {
                openProjects();
                if (splashDialog != null)
                    splashDialog.dispose();
            } else throw new IllegalStateException("Wrong arguments");
        });
    }

    private static void loadEverything() {
        loadImage("editorLogo.png");
        loadImage("editorLogoSmall.png");
        loadImage("yieldIcon.png");
        loadImage("projectIcon0.png");
        loadImage("projectIcon1.png");
        loadImage("projectIcon2.png");
        loadImage("closeIcon.png");
        loadImage("selectedCloseIcon.png");
        loadImage("uploadIcon.png");
        loadImage("uploadIcon16.png");
        loadImage("xarrow.png");
        loadImage("yarrow.png");
        loadImage("sxarrow.png");
        loadImage("syarrow.png");
        loadImage("reloadIcon.png");
    }

    private static void loadImage(String n) {
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
    }

    public static void openProjects() {
        JFrame frame = new JFrame("Projects");
        frame.setSize(new Dimension(800, 650));
        frame.setMinimumSize(new Dimension(800, 650));
        frame.setMaximumSize(new Dimension(1000, 720));

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Projects.saveProjects();
                frame.dispose();
            }
        });
        frame.setIconImage(Assets.images.get("yieldIcon.png").getImage());

        frame.setContentPane(new Projects(frame));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void splashDialog(String title) {
        splashDialog = new JDialog();
        splashDialog.setTitle("Yield 5 Editor");
        splashDialog.setUndecorated(true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        splashDialog.add(progressBar, BorderLayout.SOUTH);
        splashDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        if (title != null)
            splashDialog.add(new JLabel(title), BorderLayout.LINE_END);
        splashDialog.add(new JLabel(new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/splash.png")))), BorderLayout.CENTER);
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setVisible(true);
    }
}
