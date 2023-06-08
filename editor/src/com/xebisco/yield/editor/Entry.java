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
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class Entry {
    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        splashDialog();
        openProjects();
    }

    private static void loadEverything() {
        String n = "editorLogo.png";
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
        n = "editorLogoSmall.png";
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
        n = "yieldIcon.png";
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
        n = "projectIcon0.png";
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
        n = "projectIcon1.png";
        Assets.images.put(n, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/" + n))));
        n = "projectIcon2.png";
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
                try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(Utils.defaultDirectory() + "/.yield_editor/projects.ser"))) {
                    oo.writeObject(Assets.projects);
                } catch (IOException ex) {
                    Utils.error(frame, ex);
                }
                frame.dispose();
            }
        });
        frame.setIconImage(Assets.images.get("yieldIcon.png").getImage());

        frame.setContentPane(new Projects(frame));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void splashDialog() {
        JDialog splashDialog = new JDialog();
        splashDialog.setTitle("Yield 5 Editor");
        splashDialog.setUndecorated(true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        splashDialog.add(progressBar, BorderLayout.SOUTH);
        splashDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        splashDialog.add(new JLabel(new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/splash.png")))), BorderLayout.CENTER);
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setVisible(true);
        try {
            loadEverything();
        } catch (NullPointerException e) {
            Utils.error(splashDialog, e);
        }
        splashDialog.dispose();
    }
}