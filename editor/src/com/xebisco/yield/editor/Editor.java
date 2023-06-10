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

import com.xebisco.yield.ini.Ini;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Editor extends JFrame {

    private EditorScene openedScene;
    private final Project project;
    private int gridX = 10, gridY = 10;

    public Editor(Project project) {
        this.project = project;
        setTitle("Yield 5 Editor | " + project.getName());
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
        setJMenuBar(menuBar());
        setIconImage(Assets.images.get("yieldIcon.png").getImage());
        JSplitPane mainAndInfo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createPaneWithTab(new SceneScroll(new SceneView(1)), "Scene View"), createPaneWithTab(new JPanel(), "Info"));
        mainAndInfo.setResizeWeight(1);
        mainAndInfo.setDividerLocation(getSize().width - 650);
        JSplitPane sourceAndConsole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createPaneWithTab(new JPanel(), "Source"), createPaneWithTab(new JPanel(), "Console"));
        sourceAndConsole.setResizeWeight(1);
        sourceAndConsole.setDividerLocation(getSize().height - 300);
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceAndConsole, mainAndInfo);
        mainSplit.setDividerLocation(300);
        add(mainSplit);
        setLocationRelativeTo(null);
        setVisible(true);
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
                try {
                    File file = File.createTempFile("newScene", "ini");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.append(
                                "[Scene Creation]\n" +
                                        "Name(STR) = Empty Scene\n"
                        );
                    }
                    Ini ini = new Ini();
                    new PropsWindow(ini, file, () -> {
                        EditorScene scene = new EditorScene();
                        scene.setName(ini.getSections().get("Scene Creation").getProperty("Name(STR)"));
                        project.getScenes().put(scene.getName(), scene);
                        openedScene = scene;
                        repaint();
                    }, Editor.this);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
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
                if (!isVisible())
                    Entry.openProjects();
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

    class SceneScroll extends JScrollPane {
        public SceneScroll(Component view) {
            super(view);
            setBorder(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    class SceneView extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
        private double zoom = 1.0;
        public static final double SCALE_STEP = 0.1d;
        private Dimension initialSize;
        private Point origin;
        private double previousZoom = zoom;
        private double scrollX = 0d;
        private double scrollY = 0d;
        private float hexSize = 3f;

        public SceneView(double zoom) {
            this.zoom = zoom;
            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
            setAutoscrolls(true);
        }

        private String noSceneLoadedText = "No scenes loaded.";

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (openedScene != null) {
                g2d.fillRect(0, 0, getWidth(), getHeight());
                if (openedScene.getBackgroundColor() != Color.WHITE)
                    g.setColor(openedScene.getBackgroundColor().brighter());
                else g.setColor(Color.BLACK);

                g2d.scale(zoom, zoom);

                Rectangle size = getBounds();
                double tx = ((size.getWidth() - getWidth() * zoom) / 2) / zoom;
                double ty = ((size.getHeight() - getHeight() * zoom) / 2) / zoom;
                g2d.translate(tx, ty);

                int gridX = Editor.this.gridX, gridY = Editor.this.gridY;
                while (gridX * zoom < 50) {
                    gridX *= 2;

                }
                while (gridY * zoom < 50) {
                    gridY *= 2;
                }
                for (int i = 0; i < getWidth() / gridX / 2 + 1; i++) {
                    int x = getWidth() / 2 + gridX * i;
                    g.drawLine(x, 0, x, getHeight());
                }
                for (int i = 1; i < getWidth() / gridX / 2 + 1; i++) {
                    int x = getWidth() / 2 + gridX * -i;
                    g.drawLine(x, 0, x, getHeight());
                }

                for (int i = 0; i < getHeight() / gridY / 2 + 1; i++) {
                    int y = getHeight() / 2 + gridX * i;
                    g.drawLine(0, y, getWidth(), y);
                }
                for (int i = 1; i < getHeight() / gridY / 2 + 1; i++) {
                    int y = getHeight() / 2 + gridX * -i;
                    g.drawLine(0, y, getWidth(), y);
                }
            } else {
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawString(noSceneLoadedText, getWidth() / 2 - g.getFontMetrics().stringWidth(noSceneLoadedText) / 2, getHeight() / 2 + g.getFont().getSize() / 2);
            }


            g.dispose();
        }

        @Override
        public void setSize(Dimension size) {
            super.setSize(size);
            if (initialSize == null) {
                this.initialSize = size;
            }
        }

        @Override
        public void setPreferredSize(Dimension preferredSize) {
            super.setPreferredSize(preferredSize);
            if (initialSize == null) {
                this.initialSize = preferredSize;
            }
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (openedScene != null) {
                double zoomFactor = -SCALE_STEP * e.getPreciseWheelRotation() * zoom;
                zoom = Math.abs(zoom + zoomFactor);
                //Here we calculate new size of canvas relative to zoom.
                Dimension d = new Dimension(
                        (int) (initialSize.width * zoom),
                        (int) (initialSize.height * zoom));
                if (getParent().getSize().width > d.width || getParent().getSize().height > d.height) {

                    //TODO fix
                    d = new Dimension(
                            getParent().getSize().width,
                            getParent().getSize().height);
                }
                setPreferredSize(d);
                setSize(d);
                validate();
                followMouseOrCenter(e.getPoint());
                previousZoom = zoom;
            }
        }

        public void followMouseOrCenter(Point point) {
            Rectangle size = getBounds();
            Rectangle visibleRect = getVisibleRect();
            scrollX = size.getCenterX();
            scrollY = size.getCenterY();
            if (point != null) {
                scrollX = point.getX() / previousZoom * zoom - (point.getX() - visibleRect.getX());
                scrollY = point.getY() / previousZoom * zoom - (point.getY() - visibleRect.getY());
            }

            visibleRect.setRect(scrollX, scrollY, visibleRect.getWidth(), visibleRect.getHeight());
            scrollRectToVisible(visibleRect);
        }

        public void mouseDragged(MouseEvent e) {
            if (origin != null && openedScene != null) {
                int deltaX = origin.x - e.getX();
                int deltaY = origin.y - e.getY();
                Rectangle view = getVisibleRect();
                view.x += deltaX;
                view.y += deltaY;
                scrollRectToVisible(view);
            }
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (openedScene != null)
                origin = new Point(e.getPoint());
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

    }
}
//getWidth() - zoomPointX / zoom + zoomPointX - getWidth()

/*
class SceneView extends JPanel implements MouseWheelListener {

        private String noSceneLoadedText = "No scenes loaded.";
        private double zoom = 1, zoomPointX, zoomPointY;

        public SceneView() {
            addMouseWheelListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            //if (openedScene != null) {
            //}
            AffineTransform at = g2.getTransform();
            at.translate(zoomPointX, zoomPointY);
            at.scale(zoom, zoom);
            at.translate(-zoomPointX, -zoomPointY);
            g2.setTransform(at);
            g.setColor(Color.BLACK);
            for(int i = 0; i < getWidth() / gridX / 2; i++) {
                int x = getWidth() / 2 + gridX * i;
                g.drawLine(x, (int) (getHeight() - getHeight() * zoom), x, (int) (getHeight() * zoom));
            }
            g.setColor(Color.RED);
            //g.fillRect((int) (getWidth() - zoomPointX / zoom + zoomPointX - getWidth()), (int) (getHeight() - zoomPointY / zoom + zoomPointY - getHeight()), 10, 10);
            System.out.println((int) getWidth() + (-getWidth() - zoomPointX / zoom + zoomPointX - getWidth()));
            g.fillRect((int) (-getWidth() - zoomPointX / zoom + zoomPointX - getWidth()) - 10, (int) (getHeight() - zoomPointY / zoom + zoomPointY - getHeight()) , 10, 10);
            if (openedScene != null) {
                g.setColor(openedScene.getBackgroundColor());
                g.fillRect(0, 0, getWidth(), getHeight());
                if(!openedScene.getBackgroundColor().equals(Color.WHITE)) {
                    g.setColor(openedScene.getBackgroundColor().brighter());
                } else {
                    g.setColor(Color.BLACK);
                }

            } else {
                g.setColor(Color.WHITE);
                g.drawString(noSceneLoadedText, getWidth() / 2 - g.getFontMetrics().stringWidth(noSceneLoadedText) / 2, getHeight() / 2 + g.getFont().getSize() / 2);
            }
        }



        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            zoomPointX = e.getX();
            zoomPointY = e.getY();
            if (-e.getPreciseWheelRotation() < 0) {
                zoom -= 0.1;
            } else {
                zoom += 0.1;
            }
            if (zoom < 0.01) {
                zoom = 0.01;
            }
            repaint();
        }
    }
 */
