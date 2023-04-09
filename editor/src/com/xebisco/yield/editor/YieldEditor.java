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

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class YieldEditor extends JFrame {

    public static BufferedImage ICON, CLOSE_ICON, SELECTED_CLOSE_ICON, YIELD_SMALL_ICON;
    private final File project;
    private JTabbedPane scenesPanel;


    public YieldEditor(File project) {
        this.project = project;
        setTitle("Yield Editor - " + project.getName());
        setMinimumSize(new Dimension(500, 300));
        setIconImage(ICON);


        //Title bar

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(new JMenuItem(new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    openEditor(null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }));

        menu.add(new JMenuItem(new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDragEnabled(true);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        openEditor(fileChooser.getSelectedFile().getAbsolutePath());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    throwException(new Exception("Operation cancelled"), YieldEditor.this);
                }
            }
        }));

        menu.addSeparator();

        menu.add(new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        }));

        menu.add(new JMenuItem(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));

        menuBar.add(menu);


        menu = new JMenu("Scene");
        menu.add(new JMenuItem(new AbstractAction("New Scene") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewDialog("New Scene", new JList<>(new String[]{"Empty", "Hello, World!"}), (n, s) -> {
                    JPanel scenePanel = new GameView();
                    scenesPanel.insertTab(n, null, scenePanel, n, 0);
                });
            }
        }));

        menuBar.add(menu);

        setJMenuBar(menuBar);

        //Title bar


        JPanel mainPanel = new JPanel();

        // add(mainPanel);

        createScenesPanel();
JTabbedPane tabbedPane = new DnDTabbedPane();
        tabbedPane.addTab("aaa", new JPanel());
        JSplitPane p1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scenesPanel, tabbedPane);

        mainPanel.setPreferredSize(getSize());

        add(p1);

        p1.setDividerLocation(1000);


        setSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                YieldEditor.this.dispose();
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        String p = null;
        if (args.length == 1)
            p = args[0];
        openEditor(p);
    }

    public static void openEditor(String projectPath) throws IOException {

        JDialog splashDialog = new JDialog();
        splashDialog.add(new JLabel(new ImageIcon(ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("splashScreen.png"))))));

        splashDialog.setUndecorated(true);
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        splashDialog.setVisible(true);

        FlatDarkLaf.setup();

        if (ICON == null) {
            log("Loading resources");
            try {

                ICON = ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("yieldIcon.png")));
                YIELD_SMALL_ICON = ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("yieldSmallIcon.png")));
            } catch (IOException e) {
                throwException(e, null);
                System.exit(1);
            }
        }


        File project = null;

        if (projectPath == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDragEnabled(true);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                project = fileChooser.getSelectedFile();
            } else {
                splashDialog.dispose();
                return;
            }
        }

        log("Verifying project");

        if (project == null) {
            assert projectPath != null;
            project = new File(projectPath);
        }

        if (!project.exists()) {
            throwException(new IllegalStateException("File does not exists"), null);
            splashDialog.dispose();
            return;
        }

        if (!project.isDirectory()) {
            throwException(new IllegalStateException("File is not a directory"), null);
            splashDialog.dispose();
            return;
        }

        log("Opening editor");

        splashDialog.dispose();

        new YieldEditor(project);
    }

    public static void log(String msg) {
        System.out.println("YE: " + msg + " (" + new Date() + ")");
    }

    public static void throwException(Exception e, Component parent) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(parent, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }

    public void createNewDialog(String title, JList<String> options, DialogConfirm create) {
        JDialog dialog = new JDialog(YieldEditor.this, true);
        dialog.setLayout(new BorderLayout());
        dialog.getRootPane().setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedSoftBevelBorder(), title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, UIManager.getFont("Label.font").deriveFont(Font.BOLD)));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(dialog.getBackground().darker());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton button;
        buttonPanel.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }));
        JTextField sceneName = new JTextField();
        buttonPanel.add(button = new JButton(new AbstractAction("Create") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sceneName.getText().hashCode() == "".hashCode()) {
                    JOptionPane.showMessageDialog(dialog, "Name field must not be empty!", title, JOptionPane.ERROR_MESSAGE);
                } else {
                    dialog.dispose();
                    create.create(sceneName.getText(), options.getSelectedValue());
                }
            }
        }));
        dialog.getRootPane().setDefaultButton(button);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setTitle(title);
        dialog.setIconImage(ICON);
        sceneName.setToolTipText(title + " Name");
        dialog.add(sceneName, BorderLayout.NORTH);
        options.setBackground(dialog.getBackground().darker());
        options.setSelectedIndex(0);
        options.setDragEnabled(false);
        dialog.add(options, BorderLayout.CENTER);
        dialog.setUndecorated(true);
        dialog.pack();

        dialog.setResizable(false);
        dialog.setSize(new Dimension(dialog.getSize().width + 50, dialog.getSize().height));
        dialog.setLocationRelativeTo(YieldEditor.this);
        dialog.setVisible(true);
    }

    private void createScenesPanel() {
        scenesPanel = new CloseableTabbedPane();

        JPopupMenu popupMenu = new JPopupMenu("Scenes");
        popupMenu.add(new JMenuItem(new AbstractAction("Close all tabs") {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (scenesPanel.getTabCount() > 0)
                    scenesPanel.removeTabAt(0);
            }
        }));
        scenesPanel.setComponentPopupMenu(popupMenu);

        //TODO

        for (int i = 0; i < 10; i++) {
            String title = "Tab " + i;
            JPanel panel = new JPanel();
            panel.add(title, new JLabel(title));
            scenesPanel.addTab(title, panel);
        }

        scenesPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
