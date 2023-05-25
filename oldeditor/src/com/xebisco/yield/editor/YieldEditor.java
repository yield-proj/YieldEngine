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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class YieldEditor extends JFrame {

    public static BufferedImage ICON, CLOSE_ICON, SELECTED_CLOSE_ICON, YIELD_SMALL_ICON, BACK_ICON, WHAT_ICON;
    private final File project;
    private final List<File> favorites = new ArrayList<>();
    private YieldTabbedPane scenesPanel, down, left, right;


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

        menu = new JMenu("Tool Windows");

        menu.add(new JMenuItem(new AbstractAction("Project Files") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JList<File> favorites = new JList<>(YieldEditor.this.favorites.toArray(new File[0]));
                FileList f = new FileList(project);
                JPanel filesPanel = new JPanel();
                filesPanel.setLayout(new BorderLayout());

                JToolBar toolBar = new JToolBar();

                JButton button = new JButton();
                button.setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.setDirectory(f.getDirectory().getParentFile());
                        f.reload();
                        button.setEnabled(!project.equals(f.getDirectory()));
                    }
                });
                toolBar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setEnabled(!project.equals(f.getDirectory()));
                    }
                });
                button.setIcon(new ImageIcon(BACK_ICON.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
                toolBar.add(button);

                filesPanel.add(toolBar, BorderLayout.NORTH);

                JScrollPane scrollPane = new JScrollPane(f);
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                filesPanel.add(scrollPane, BorderLayout.CENTER);
                favorites.setCellRenderer(new SmallFileCellRenderer());
                favorites.setBorder(BorderFactory.createTitledBorder("Favorites"));
                favorites.setMinimumSize(new Dimension(100, 100));
                favorites.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        favorites.setListData(YieldEditor.this.favorites.toArray(new File[0]));
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && !e.isPopupTrigger()) {
                            f.setDirectory(favorites.getSelectedValue());
                            f.reload();
                        }
                    }
                });
                f.setFavorites(YieldEditor.this.favorites);
                f.setCellRenderer(new FileCellRenderer());
                f.setDragEnabled(true);
                f.setFixedCellHeight(100);
                f.setFixedCellWidth(100);
                f.setTransferHandler(new FileListTransferHandler(f));
                f.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                f.setVisibleRowCount(-1);
                f.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && !e.isPopupTrigger()) {
                            f.setDirectory(f.getSelectedValue());
                            f.reload();
                            button.setEnabled(!project.equals(f.getDirectory()));
                        }
                    }
                });


                JSplitPane projectFiles = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, favorites, filesPanel);
                down.addTab("Project Files", projectFiles);
            }
        }));

        menuBar.add(menu);

        menu = new JMenu("Scene");
        menu.add(new JMenuItem(new AbstractAction("New Scene") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewDialog("New Scene", new JList<>(new String[]{"Empty", "Hello, World!"}), (n, s) -> {
                    GameView gameView = new GameView();
                    gameView.setScene(new InEditorScene());
                    scenesPanel.insertTab(n, null, gameView, n, 0);
                });
            }
        }));

        menuBar.add(menu);

        setJMenuBar(menuBar);

        //Title bar


        JPanel mainPanel = new JPanel();

        // add(mainPanel);

        createScenesPanel();

        GameView emptyGameView = new GameView();
        emptyGameView.setScene(new InEditorScene());

        scenesPanel.addTab("Empty Scene", emptyGameView);

        down = new YieldTabbedPane();
        left = new YieldTabbedPane();
        right = new YieldTabbedPane();

        JSplitPane mainAndLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, scenesPanel);

        mainAndLeft.setDividerLocation(200);

        JSplitPane mainAndDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainAndLeft, down);

        mainAndDown.setDividerLocation(500);

        JSplitPane mainAndRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainAndDown, right);

        mainAndRight.setDividerLocation(1000);

        mainPanel.setPreferredSize(getSize());

        add(mainAndRight);

        //FileSystemView.getFileSystemView().getSy

        mainAndLeft.setOneTouchExpandable(true);
        mainAndDown.setOneTouchExpandable(true);
        mainAndRight.setOneTouchExpandable(true);

        mainAndLeft.setResizeWeight(.5);
        mainAndDown.setResizeWeight(.5);
        mainAndRight.setResizeWeight(.5);


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
        FlatDarkLaf.setup();

        String p = null;
        if (args.length == 1)
            p = args[0];
        openEditor(p);
    }

    public static void openEditor(String projectPath) throws IOException {
        if (ICON == null) {
            log("Loading resources");
            try {
                ICON = ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("yieldIcon.png")));
                YIELD_SMALL_ICON = ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("yieldSmallIcon.png")));
                WHAT_ICON = ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("what.png")));
                BACK_ICON = ImageIO.read(Objects.requireNonNull(YieldEditor.class.getResource("backIcon.png")));
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
            return;
        }

        if (!project.isDirectory()) {
            throwException(new IllegalStateException("File is not a directory"), null);
            return;
        }

        log("Opening editor");

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
        dialog.getRootPane().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.CENTER, TitledBorder.TOP, UIManager.getFont("Label.font").deriveFont(Font.BOLD)));
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
        scenesPanel = new YieldTabbedPane();

        //TODO

        scenesPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
}
