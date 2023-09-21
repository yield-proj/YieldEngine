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

import com.xebisco.yield.editor.code.CompilationException;
import com.xebisco.yield.editor.explorer.Explorer;
import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;
import com.xebisco.yield.editor.scene.EditorScene;
import com.xebisco.yield.editor.scene.SceneExplorer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.ToolProvider;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Editor extends JFrame implements IRecompile {

    private EditorScene openedScene;
    private final Project project;
    private int gridX = 10;
    private int gridY = 10;

    private final CloseableTabbedPane workspaces;
    private final JTextArea console;

    public static List<File> getFilesByExtension(File dir, String extension) {
        List<File> out = new ArrayList<>();
        File[] files = dir.listFiles();
        assert files != null;
        for (File f : files) {
            if (f.isDirectory())
                out.addAll(getFilesByExtension(f, extension));
            else if (f.getName().endsWith("." + extension)) out.add(f);
        }
        return out;
    }

    private YieldToolBar toolBar() {
        YieldToolBar toolBar = new YieldToolBar("Workspace Tool Bar");
        toolBar.setBackground(toolBar.getBackground().darker());
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        toolBar.setBorderPainted(false);
        toolBar.add(new JLabel("Workspaces", Assets.images.get("workspaceIcon.png"), JLabel.LEFT));
        toolBar.add(Box.createHorizontalGlue());

        toolBar.add(new JLabel(" "));

        JButton run = new JButton();
        Color savedBkg = new Color(run.getBackground().getRGB());

        toolBar.add(new JLabel(" "));

        run.setAction(new AbstractAction("", Assets.images.get("runIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                run.setBackground(new Color(89, 157, 94));
                run.repaint();
            }
        });

        toolBar.add(run);
        toolBar.add(new JButton(new AbstractAction("", Assets.images.get("searchIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }));
        return toolBar;
    }

    public Editor(Project project) {
        Assets.lastOpenedProject = project;
        this.project = project;
        Assets.openedEditors.add(this);
        Workspace workspace = new Workspace(project, this);
        workspaces = new CloseableTabbedPane(false);
        workspaces.addTab("Workspace", workspace);

        Entry.splashDialog(null);

        CompletableFuture.runAsync(() -> {
            recompileProject();

            Entry.splashDialog.dispose();
        });

        if (Assets.projects != null && Assets.projects.contains(project)) {
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
                    Assets.openedEditors.remove(Editor.this);
                    dispose();
                }
            }
        });
        setIconImage(Assets.images.get("yieldIcon.png").getImage());

        SceneExplorer sceneExplorer = new SceneExplorer();

        JSplitPane left0SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new YieldTabbedPane(false, this).addNewTab("Explorer", new Explorer(project.getProjectLocation(), "Project Root", sceneExplorer, workspace)), new YieldTabbedPane(false, this).addNewTab("Scene Explorer", sceneExplorer));
        JSplitPane left1SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, left0SplitPane, new YieldTabbedPane(false, this).addNewTab("Console", console = new JTextArea()));
        console.setEditable(false);
        left0SplitPane.setResizeWeight(1);
        left1SplitPane.setResizeWeight(.5);
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());

        left.add(left1SplitPane);


        ProjectPanel projectPanel = getProjectPanel();

        YieldToolBar toolBar = new YieldToolBar("Project") {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color startColor = projectPanel.invbg.darker();
                Color endColor = getBackground().darker();

                GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth() / 2, getHeight(), endColor);
                ((Graphics2D) g).setPaint(gradient);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        panel.add(projectPanel);

        toolBar.add(panel);

        left.add(toolBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(toolBar(), BorderLayout.NORTH);
        mainPanel.add(workspaces);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, mainPanel);
        mainSplitPane.setBackground(mainSplitPane.getBackground().darker());

        setContentPane(mainSplitPane);

        setJMenuBar(menuBar());


        setLocationRelativeTo(null);
        //setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        mainSplitPane.setDividerLocation(.25);
        left1SplitPane.setDividerLocation(.7);
        left0SplitPane.setDividerLocation(.6);
    }

    private static class ProjectPanel extends JPanel {
        boolean selected;

        private final BufferedImage icon;
        final Color bg;
        final Color invbg;
        private final String text;

        public ProjectPanel(BufferedImage icon, String text) {
            this.text = text;
            this.icon = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics gc = this.icon.getGraphics();
            gc.drawImage(icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH), 0, 0, null);
            gc.dispose();
            int r = 0, g = 0, b = 0, s = 0;
            for (int x = 0; x < this.icon.getWidth(); x++)
                for (int y = 0; y < this.icon.getHeight(); y++) {
                    Color c = new Color(this.icon.getRGB(x, y), true);
                    if (c.getAlpha() > 0) {
                        r += c.getRed();
                        g += c.getGreen();
                        b += c.getBlue();
                        s++;
                    }
                }
            r /= s;
            g /= s;
            b /= s;
            invbg = new Color(r, g, b);
            bg = new Color(255 - r, 255 - g, 255 - b);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setFont(UIManager.getFont("Label.font"));
            g.translate(0, -5);
            setSize(new Dimension(50 + g.getFontMetrics().stringWidth(text) + 22, 40));
            validate();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (selected) {
                g.setColor(getBackground().brighter());
                g.fillRoundRect(5, 4, getWidth() - 10, getHeight() - 8, 10, 10);
            }
            g.setColor(bg);
            g.fillRoundRect(14, getHeight() / 2 - icon.getHeight() / 2 - 2, icon.getWidth() + 4, icon.getHeight() + 4, 10, 10);
            g.drawImage(icon, 16, getHeight() / 2 - icon.getHeight() / 2, this);
            g.setColor(getForeground());
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawString(text, 20 + icon.getWidth() + 5, 6 + icon.getHeight());
            g.setColor(new Color(0x4D5056));
            g.drawLine(getWidth() - 12, getHeight() / 2 - 2, getWidth() - 12 - 4, getHeight() / 2 + 4 - 2);
            g.drawLine(getWidth() - 12 - 4, getHeight() / 2 + 4 - 2, getWidth() - 12 - 4 - 4, getHeight() / 2 - 2);
        }
    }

    private ProjectPanel getProjectPanel() {
        ProjectPanel projectPanel;
        try {
            projectPanel = new ProjectPanel(ImageIO.read(new File(project.getProjectLocation(), "icon.png")), project.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        projectPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                projectPanel.selected = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                projectPanel.selected = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();

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

                JLabel label = new JLabel("  Open editors");
                label.setForeground(new Color(108, 101, 119));

                menu.add(label);

                for (Editor editor : Assets.openedEditors) {

                    try {
                        menu.add(new AbstractAction(editor.project.getName() + " (" + editor.project.getProjectLocation().getPath() + ')', new ImageIcon(ImageIO.read(new File(editor.project.getProjectLocation(), "icon.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH))) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                editor.requestFocus();
                            }
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                menu.show(projectPanel, projectPanel.getLocation().x, projectPanel.getLocation().y + projectPanel.getHeight() - 10);
            }
        });
        projectPanel.setPreferredSize(new Dimension(150, 30));
        return projectPanel;
    }

    @Override
    public void recompileProject() {
        AtomicReference<StringBuilder> builder = new AtomicReference<>();
        OutputStream error = new OutputStream() {
            @Override
            public void write(int b) {
                builder.get().append((char) b);
            }
        };
        File core = new File(Utils.EDITOR_DIR.getPath() + "/installs/" + project.preferredInstall().install() + "/yield-core.jar");
        List<File> scripts = getFilesByExtension(new File(project.getProjectLocation(), "Scripts"), "java");

        for (File file : scripts) {
            builder.set(new StringBuilder());
            ToolProvider.getSystemJavaCompiler().run(null, null, error, "-cp", core.getPath(), "-d", ComponentProp.DEST.getPath(), file.getPath());
            if (!builder.get().isEmpty())
                Utils.errorNoStackTrace(Entry.splashDialog, new CompilationException(builder.toString()));
        }
        try {
            error.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private YieldTabbedPane createPaneWithTab(Component tab, String name) {
        YieldTabbedPane pane = new YieldTabbedPane(false, this);
        pane.addTab(name, tab);
        return pane;
    }

    public static void openSettings(Frame owner) {
        new PropsWindow(Assets.editorSettings, Projects::saveSettings, owner, "editor_settings");
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

        JMenuItem item = new JMenuItem(new AbstractAction("Save Project") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem(new AbstractAction("Settings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Assets.loadSettings();
                openSettings(Editor.this);
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        menu.add(item);
        menu.addSeparator();
        menu.add(new AbstractAction("Close") {
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
