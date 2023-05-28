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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Entry {
    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        splashDialog();
        openProjects();
    }

    private static void loadEverything() {
        List<String> images = getResources("/com/xebisco/yield/editor/assets/images/");
        Pattern namePattern = Pattern.compile("([^/]*)$");
        for (String path : images) {
            Matcher m = namePattern.matcher(path);
            m.find();
            String name = m.group(0);
            Assets.images.put(name, new ImageIcon(Objects.requireNonNull(Entry.class.getResource("/com/xebisco/yield/editor/assets/images/" + name))));
        }
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

    public static List<String> getResources(String contains) {
        final ArrayList<String> retval = new ArrayList<>();
        final String classPath = System.getProperty("java.class.path", ".");
        final String[] classPathElements = classPath.split(System.getProperty("path.separator"));
        for (final String element : classPathElements) {
            retval.addAll(getResources(element, contains));
        }
        return retval;
    }

    private static List<String> getResources(final String element, final String contains) {
        final ArrayList<String> retval = new ArrayList<>();
        final File file = new File(element);
        if (file.isDirectory()) {
            retval.addAll(getResourcesFromDirectory(file, contains));
        } else {
            retval.addAll(getResourcesFromJarFile(file, contains));
        }
        return retval;
    }

    private static List<String> getResourcesFromJarFile(final File file, final String contains) {
        final ArrayList<String> retval = new ArrayList<>();
        ZipFile zf;
        try {
            zf = new ZipFile(file);
        } catch (final IOException e) {
            throw new Error(e);
        }
        final Enumeration<? extends ZipEntry> e = zf.entries();
        while (e.hasMoreElements()) {
            final ZipEntry ze = e.nextElement();
            final String fileName = ze.getName();
            final boolean accept = fileName.contains(contains);
            if (accept) {
                retval.add(fileName);
            }
        }
        try {
            zf.close();
        } catch (final IOException e1) {
            throw new Error(e1);
        }
        return retval;
    }

    private static List<String> getResourcesFromDirectory(
            final File directory,
            final String contains) {
        final ArrayList<String> retval = new ArrayList<>();
        final File[] fileList = directory.listFiles();
        for (final File file : fileList) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, contains));
            } else {
                try {
                    final String fileName = file.getCanonicalPath();
                    final boolean accept = fileName.contains(contains);
                    if (accept) {
                        retval.add(fileName);
                    }
                } catch (final IOException e) {
                    throw new Error(e);
                }
            }
        }
        return retval;
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
