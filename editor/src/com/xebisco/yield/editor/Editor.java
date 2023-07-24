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

import com.xebisco.yield.editor.explorer.Explorer;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;
import com.xebisco.yield.editor.scene.EditorScene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Editor extends JFrame {

    private EditorScene openedScene;
    private final Project project;
    private int gridX = 10;
    private int gridY = 10;

    private final Workspace workspace;
    private final JTextArea console;

    public Editor(Project project) {
        this.project = project;
        workspace = new Workspace(project);

        if(Assets.projects != null && Assets.projects.contains(project)) {
            Assets.projects.remove(project);
            Assets.projects.add(0, project);
        }

        setTitle("Yield Editor");
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int opt = JOptionPane.showConfirmDialog(Editor.this, "Are you sure you want to close?", "Confirm Close", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
        setIconImage(Assets.images.get("yieldIcon.png").getImage());

        JSplitPane left0SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new YieldTabbedPane(false, this).addNewTab("Explorer", new Explorer(project.getProjectLocation(), "Project Root", workspace)), new YieldTabbedPane(false, this));
        JSplitPane left1SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, left0SplitPane, new YieldTabbedPane(false, this).addNewTab("Console", console = new JTextArea()));
        console.setEditable(false);
        left0SplitPane.setResizeWeight(1);
        left1SplitPane.setResizeWeight(.5);
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());

        left.add(left1SplitPane);

        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        JMenuBar menuBarTb = new JMenuBar();
        menuBarTb.setBorderPainted(false);

        JMenu menu = new JMenu(project.getName());
        menu.setIcon(UIManager.getIcon("Tree.expandedIcon"));

        menu.add(new AbstractAction("New project", Assets.images.get("addIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Projects.newProjectFrame(Editor.this);
            }
        });

        menu.add(new AbstractAction("Back to projects window", Assets.images.get("backIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
                if (!isVisible()) Entry.openProjects();
            }
        });

        menu.addSeparator();

        JLabel label = new JLabel("  This editor project");
        label.setForeground(new Color(108, 101, 119));

        menu.add(label);

        try {
            menu.add(new AbstractAction(project.getProjectLocation().getPath(), new ImageIcon(ImageIO.read(new File(project.getProjectLocation(), "icon.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH))) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JOptionPane.showConfirmDialog(null, "Open in new Editor?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
                    }
                    new Editor(project);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        menu.addSeparator();

        label = new JLabel("  All projects");
        label.setForeground(new Color(108, 101, 119));

        menu.add(label);

        for (Project p : Assets.projects) {
            try {
                menu.add(new AbstractAction(p.getProjectLocation().getPath(), new ImageIcon(ImageIO.read(new File(p.getProjectLocation(), "icon.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH))) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (JOptionPane.showConfirmDialog(null, "Open in new Editor?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                            dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
                        }
                        new Editor(p);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        menuBarTb.add(menu);

        toolBar.add(menuBarTb);

        left.add(toolBar, BorderLayout.NORTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, workspace);

        setContentPane(mainSplitPane);

        setJMenuBar(menuBar());


        setLocationRelativeTo(null);
        //setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        mainSplitPane.setDividerLocation(.25);
        left1SplitPane.setDividerLocation(.7);
        left0SplitPane.setDividerLocation(.6);
    }

    private YieldTabbedPane createPaneWithTab(Component tab, String name) {
        YieldTabbedPane pane = new YieldTabbedPane(false, this);
        pane.addTab(name, tab);
        return pane;
    }

    private JMenuBar menuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenu submenu1 = new JMenu("New");
        submenu1.add(new AbstractAction("Scene") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Prop[]> sections = new HashMap<>();
                sections.put("New Scene", Props.newSimple());
                new PropsWindow(sections, () -> {
                    EditorScene scene = new EditorScene();
                    scene.setName((String) Objects.requireNonNull(Props.get(sections.get("New Scene"), "Name")).getValue());
                    project.getScenes().put(scene.getName(), scene);
                    setOpenedScene(scene);
                    repaint();
                }, Editor.this, null);
            }
        });
        submenu1.setMnemonic(KeyEvent.VK_M);
        menu.add(submenu1);

        JMenuItem item = new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menu.add(item);
        menu.addSeparator();
        menu.add(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        item = new JMenuItem(new AbstractAction("Repaint") {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, KeyEvent.ALT_DOWN_MASK));
        menuBar.add(item);
        item = new JMenuItem(new AbstractAction("Run") {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.ALT_DOWN_MASK));
        menuBar.add(item);


        return menuBar;
    }

    public EditorScene openedScene() {
        return openedScene;
    }

    public Editor setOpenedScene(EditorScene openedScene) {
        this.openedScene = openedScene;
        return this;
    }

    public Project project() {
        return project;
    }

    public int gridX() {
        return gridX;
    }

    public Editor setGridX(int gridX) {
        this.gridX = gridX;
        return this;
    }

    public int gridY() {
        return gridY;
    }

    public Editor setGridY(int gridY) {
        this.gridY = gridY;
        return this;
    }
}
