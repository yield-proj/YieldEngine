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

import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;
import com.xebisco.yield.editor.scene.EditorScene;
import com.xebisco.yield.editor.scene.SceneObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Editor extends JFrame {

    private EditorScene openedScene;
    private final Project project;
    private int gridX = 10;
    private int gridY = 10;

    private final YieldTabbedPane central;
    private final YieldTabbedPane northwest;
    private final YieldTabbedPane southwest;
    private final YieldTabbedPane east;

    private final String titleBase;

    private final JTextArea console = new JTextArea();

    public Editor(Project project) {
        this.project = project;
        titleBase = "Yield 5 Editor | " + project.getName();
        setTitle(titleBase);
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int opt = JOptionPane.showConfirmDialog(Editor.this, "Be sure to check if you saved all your work!", "Confirm Close", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    Projects.saveProjects();
                    dispose();
                }
            }
        });
        setIconImage(Assets.images.get("yieldIcon.png").getImage());
        JSplitPane mainAndInfo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, central = new YieldTabbedPane(), east = new YieldTabbedPane());

        mainAndInfo.setResizeWeight(1);
        mainAndInfo.setDividerLocation(getSize().width - 650);
        JSplitPane sourceAndConsole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northwest = new YieldTabbedPane(), southwest = new YieldTabbedPane());
        sourceAndConsole.setResizeWeight(1);
        sourceAndConsole.setDividerLocation(getSize().height - 300);
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceAndConsole, mainAndInfo);
        mainSplit.setDividerLocation(300);
        add(mainSplit);

        console.setEditable(false);
        console.setText("Yield Editor Console: " + new Date());


        console.setBackground(console.getBackground().brighter());

        setJMenuBar(menuBar());


        setLocationRelativeTo(null);
        //setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void printToConsole(String s) {
        console.setText(console.getText() + '\n' + s);
    }

    private YieldTabbedPane createPaneWithTab(Component tab, String name) {
        YieldTabbedPane pane = new YieldTabbedPane();
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
                sections.put("New Scene", Props.newScene());
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
        menu.add(new AbstractAction("Back to Projects") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
                if (!isVisible()) Entry.openProjects();
            }
        });
        menu.add(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        menu = new JMenu("Window");

        central.addTab("Scene View", new SceneView());
        east.addTab("Info", new JPanel());
        northwest.addTab("Source", new JPanel());
        southwest.addTab("Console", console);

        menu.add(new AbstractAction("Scene View") {
            @Override
            public void actionPerformed(ActionEvent e) {
                central.addTab("Scene View", new SceneView());
            }
        });
        menu.add(new AbstractAction("Info") {
            @Override
            public void actionPerformed(ActionEvent e) {
                east.addTab("Info", new JPanel());
            }
        });
        menu.add(new AbstractAction("Source") {
            @Override
            public void actionPerformed(ActionEvent e) {
                northwest.addTab("Source", new JPanel());
            }
        });
        menu.add(new AbstractAction("Console") {
            @Override
            public void actionPerformed(ActionEvent e) {
                southwest.addTab("Console", console);
            }
        });
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

    class SceneView extends JPanel {

        private double zoom = 1.0;
        private double posX = 0d;
        private double posY = 0d;

        private final JTextField scaleField = new JTextField();
        private final JTextField posXField = new JTextField();
        private final JTextField posYField = new JTextField();

        public SceneView() {
            setLayout(new BorderLayout());
            add(new SceneViewProps(), BorderLayout.NORTH);
            add(new SceneGameView());
        }

        class SceneViewProps extends JPanel {
            public SceneViewProps() {
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(new JLabel("View Scale: "));
                add(scaleField);
                scaleField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            zoom = Double.parseDouble(scaleField.getText());
                            if (zoom < 0.5) zoom = 0.5;
                        } catch (NumberFormatException ignore) {

                        }
                        SceneView.this.repaint();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            zoom = Double.parseDouble(scaleField.getText());
                            if (zoom < 0.5) zoom = 0.5;
                        } catch (NumberFormatException ignore) {

                        }
                        SceneView.this.repaint();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });
                add(new JLabel("View Position: "));
                add(posXField);
                posXField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            posX = Double.parseDouble(posXField.getText());
                        } catch (NumberFormatException ignore) {

                        }
                        SceneView.this.repaint();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            posX = Double.parseDouble(posXField.getText());
                        } catch (NumberFormatException ignore) {

                        }
                        SceneView.this.repaint();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });
                add(new JLabel("x"));
                add(posYField);
                posYField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            posY = Double.parseDouble(posYField.getText());
                        } catch (NumberFormatException ignore) {

                        }
                        SceneView.this.repaint();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            posY = Double.parseDouble(posYField.getText());
                        } catch (NumberFormatException ignore) {

                        }
                        SceneView.this.repaint();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });
            }
        }

        class SceneGameView extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {

            private Point moveStart = new Point();
            private SceneObject selectedObject;
            private int x, y;

            public SceneGameView() {
                addMouseWheelListener(this);
                addMouseMotionListener(this);
                addMouseListener(this);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (zoom > 24)
                    zoom = 24;
                if (!scaleField.hasFocus())
                    scaleField.setText(String.format("%.1f", zoom));
                if (!posXField.hasFocus())
                    posXField.setText(String.format("%.1f", posX));
                if (!posYField.hasFocus())
                    posYField.setText(String.format("%.1f", posY));
                Graphics2D g2d = (Graphics2D) g;
                if (openedScene != null) {
                    g.setColor(openedScene.getBackgroundColor());
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    g2d.scale(zoom, zoom);

                    Rectangle size = getBounds();
                    double tx = ((size.getWidth() - getWidth() * zoom) / 2) / zoom;
                    double ty = ((size.getHeight() - getHeight() * zoom) / 2) / zoom;
                    g2d.translate(tx, ty);

                    g2d.translate(posX, posY);

                    /*g.setColor(Color.RED);
                    g.fillRect(x, y, 10, 10);*/

                    float str = (float) (1 / zoom);
                    if (str < 0.05) str = 0.05f;
                    g2d.setStroke(new BasicStroke(str));

                    for (SceneObject object : openedScene.getSceneObjects()) {
                        object.render(g);
                    }

                    if (openedScene.getBackgroundColor() != Color.WHITE) {
                        Color osc = openedScene.getBackgroundColor().brighter();
                        g.setColor(new Color(osc.getRed(), osc.getGreen(), osc.getBlue(), 50));
                    } else g.setColor(new Color(0, 0, 0, 50));

                    int w = getWidth();
                    if (zoom < 1) w /= zoom;

                    int h = getHeight();
                    if (zoom < 1) h /= zoom;

                    int lw = (int) -posX;

                    int lh = (int) -posY;


                    for (int i = 0; i < (lw + w) / gridX + 1; i++) {
                        int x = gridX * i;
                        g.drawLine(x, lh + h, x, lh - (h - getHeight()));
                    }
                    for (int i = 1; i < (getWidth() / 2 - lw + (w - getWidth())) / gridX + 1; i++) {
                        int x = gridX * -i;
                        g.drawLine(x, lh + h, x, lh - (h - getHeight()));
                    }

                    for (int i = 0; i < (lh + h) / gridY + 1; i++) {
                        int y = gridX * i;
                        g.drawLine(lw + w, y, lw - (w - getWidth()), y);
                    }
                    for (int i = 1; i < (getHeight() / 2 - lh + (h - getHeight())) / gridY + 1; i++) {
                        int y = gridX * -i;
                        g.drawLine(lw + w, y, lw - (w - getWidth()), y);
                    }

                    if (selectedObject != null) {

                        g2d.drawImage(Assets.images.get("yarrow.png").getImage(), 100, 100, (int) (Assets.images.get("yarrow.png").getIconWidth() / zoom), (int) (Assets.images.get("yarrow.png").getIconHeight() / zoom), null);
                        g2d.drawImage(Assets.images.get("xarrow.png").getImage(), 100, 100, (int) (Assets.images.get("xarrow.png").getIconWidth() / zoom), (int) (Assets.images.get("xarrow.png").getIconHeight() / zoom), null);

                    }
                } else {
                    g2d.setColor(getBackground());
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.WHITE);
                    String noSceneLoadedText = "No scenes loaded.";
                    g.drawString(noSceneLoadedText, getWidth() / 2 - g.getFontMetrics().stringWidth(noSceneLoadedText) / 2, getHeight() / 2 + g.getFont().getSize() / 2);
                }


                g.dispose();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (openedScene != null) {
                    if (-e.getPreciseWheelRotation() > 0) {
                        zoom += .1;
                    } else if (zoom > 0.5) {
                        zoom -= .1;
                    }

                    if (zoom < 0.5) zoom = 0.5;

                    SceneView.this.repaint();
                }
            }

            private boolean mouseZooming;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (openedScene != null) {
                    if (mouseZooming) {
                        zoom -= (e.getY() - moveStart.y) / 100.0;
                        if (zoom < 0.5) zoom = 0.5;
                    } else {
                        posX += (e.getX() - moveStart.x) / zoom;
                        posY += (e.getY() - moveStart.y) / zoom;
                    }
                    moveStart = e.getPoint();
                    SceneView.this.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println((getWidth() - getWidth() / zoom) / 2);
                x = (int) (((e.getX() - posX) + (getWidth() - getWidth() / zoom) / 2));
                y = (int) (((e.getY() - posY) + (getHeight() - getHeight() / zoom) / 2));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (openedScene != null) {
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        mouseZooming = true;
                    }
                    moveStart = e.getPoint();
                    requestFocus();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (openedScene != null) {
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        mouseZooming = false;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }
    }

    public EditorScene getOpenedScene() {
        return openedScene;
    }

    public void setOpenedScene(EditorScene openedScene) {
        setTitle(titleBase + " | " + openedScene.getName());
        this.openedScene = openedScene;
    }

    public Project getProject() {
        return project;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public YieldTabbedPane getCentral() {
        return central;
    }

    public YieldTabbedPane getNorthwest() {
        return northwest;
    }

    public YieldTabbedPane getSouthwest() {
        return southwest;
    }

    public YieldTabbedPane getEast() {
        return east;
    }

    public JTextArea getConsole() {
        return console;
    }
}
