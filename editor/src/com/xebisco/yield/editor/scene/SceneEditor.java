package com.xebisco.yield.editor.scene;

import com.xebisco.yield.editor.Assets;
import com.xebisco.yield.editor.YieldToolBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class SceneEditor extends JPanel {

    enum Tool {
        SELECTOR, MOVE_OBJ, MOVE_VIEW
    }

    private SceneObject selectedObject;

    private Tool tool = Tool.SELECTOR;
    private int gridX = 16, gridY = 16;

    private final EditorScene scene;

    private SceneExplorer sceneExplorer;

    public SceneEditor(EditorScene scene, SceneExplorer sceneExplorer) {
        this.scene = scene;
        YieldToolBar toolBar = toolBar();
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        toolBar.add(new JLabel("Scene Editor", Assets.images.get("sceneIcon32.png"), JLabel.LEFT));
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(new JButton(new AbstractAction("", Assets.images.get("selectIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = Tool.SELECTOR;
            }
        }));
        toolBar.add(new JButton(new AbstractAction("", Assets.images.get("handIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = Tool.MOVE_VIEW;
            }
        }));
        add(new SceneView());
    }

    private YieldToolBar toolBar() {
        YieldToolBar toolBar = new YieldToolBar("Scene Editor ToolBar") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color startColor = new Color(50, 124, 236);
                Color endColor = new Color(43, 45, 48);

                GradientPaint gradient = new GradientPaint(-300, 0, startColor, getWidth() / 2f, getHeight(), endColor);
                ((Graphics2D) g).setPaint(gradient);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        toolBar.setBorderPainted(false);
        return toolBar;
    }

    class SceneView extends JPanel implements KeyListener {

        private Point startM = new Point();
        private double x, y;
        private float scale = 1;
        private final static float remScale = 0.9f, addScale = 1.1f;

        private Timer leftTimer = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x--;
                repaint();
            }
        }), rightTimer = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x++;
                repaint();
            }
        }), upTimer = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                y++;
                repaint();
            }
        }), downTimer = new Timer(16, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                y--;
                repaint();
            }
        });

        private JLabel scaleLabel = new JLabel(), xLabel = new JLabel(), yLabel = new JLabel();

        private double mx, my;

        private Point selectingSize, selectingStart;


        public SceneView() {
            setFocusable(true);
            addKeyListener(this);
            setLayout(new BorderLayout());
            add(new SceneGView());
            JPanel top = new JPanel();
            top.setLayout(new FlowLayout(FlowLayout.LEFT));
            top.add(scaleLabel);
            top.add(xLabel);
            top.add(yLabel);
            add(top, BorderLayout.NORTH);
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> leftTimer.start();
                case KeyEvent.VK_RIGHT -> rightTimer.start();
                case KeyEvent.VK_UP -> upTimer.start();
                case KeyEvent.VK_DOWN -> downTimer.start();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> leftTimer.stop();
                case KeyEvent.VK_RIGHT -> rightTimer.stop();
                case KeyEvent.VK_UP -> upTimer.stop();
                case KeyEvent.VK_DOWN -> downTimer.stop();
            }
        }

        class SceneGView extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {

            public SceneGView() {
                addMouseMotionListener(this);
                addMouseListener(this);
                addMouseWheelListener(this);
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(scene.getBackgroundColor());
                g.fillRect(0, 0, getWidth(), getHeight());

                Graphics2D g2 = (Graphics2D) g;

                if (scale <= 0.2) scale = 0.2f;
                if (scale >= 24) scale = 24;

                scaleLabel.setText(String.format("scale: %.0f%%", scale * 100f));
                xLabel.setText(String.format("x: %.1f", x));
                yLabel.setText(String.format("y: %.1f", y));

                g2.translate(getWidth() / 2., getHeight() / 2.);
                g2.scale(scale, scale);
                g2.translate(-getWidth() / 2., -getHeight() / 2.);
                g2.translate(-x, y);
                g2.setStroke(new BasicStroke(1 / scale));

                g.setColor(new Color(255 - scene.getBackgroundColor().getRed(), 255 - scene.getBackgroundColor().getGreen(), 255 - scene.getBackgroundColor().getBlue(), 100));

                int w = (int) (getWidth() / scale), h = (int) (getHeight() / scale);

                int sx = (int) -x;
                while (sx % gridX != 0) sx--;
                for (int i = -w / 2; i <= (getWidth() / 2 + w / 2) / gridX; i++)
                    g.drawLine(i * gridX - sx, -(int) y - h / 2, i * gridX - sx, getHeight() / 2 + h / 2 - (int) y);

                int sy = (int) y;
                while (sy % gridY != 0) sy--;
                for (int i = -h / 2; i <= (getHeight() / 2 + h / 2) / gridY; i++)
                    g.drawLine((int) x - w / 2, i * gridY - sy, getWidth() / 2 + w / 2 + (int) x, i * gridY - sy);



                //TODO DRAW OBJS

                if(selectingSize != null) {
                    g.setColor(new Color(20, 108, 231, 100));
                    g.fillRect(selectingStart.x, -selectingStart.y, selectingSize.x, -selectingSize.y);
                }


                if (selectedObject != null) {
                    drawArrows(g, selectedObject.getX(), selectedObject.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (tool == Tool.MOVE_VIEW) {
                    x -= (e.getX() - startM.getX()) / scale;
                    y += (e.getY() - startM.getY()) / scale;
                    startM = new Point(e.getPoint());
                } else if (tool == Tool.SELECTOR) {
                    mouseMoved(e);
                    selectingSize = new Point((int) (mx - selectingStart.x), (int) (my - selectingStart.y));
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mx = e.getX() / scale + x + getWidth() / 2. - (getWidth() / 2. / scale);
                my = -e.getY() / scale + y - getHeight() / 2. + (getHeight() / 2. / scale);
                repaint();
            }

            private static void drawArrows(Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                AffineTransform at = g2.getTransform();
                g2.setTransform(new AffineTransform());
                //X AXIS ARROW
                g.setColor(Color.RED);
                g.fillRect(x, y - 1, 100, 3);
                g2.fillPolygon(new int[]{x + 100, x + 110, x + 100}, new int[]{y + 5, y, y - 5}, 3);
                //Y AXIS ARROW
                g.setColor(Color.GREEN);
                g.fillRect(x - 1, y - 100, 3, 100);
                g2.fillPolygon(new int[]{x + 5, x, x - 5}, new int[]{y - 100, y - 110, y - 100}, 3);
                g2.setTransform(at);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedObject = null;
                for (SceneObject o : scene.getSceneObjects()) {
                    if (o.getX() - o.getWidth() / 2. >= mx && o.getX() + o.getWidth() / 2. <= mx && o.getY() - o.getHeight() / 2. >= my && o.getY() + o.getHeight() / 2. <= my) {
                        selectedObject = o;
                        break;
                    }
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (tool == Tool.MOVE_VIEW) {
                    startM = new Point(e.getPoint());
                }
                else if (tool == Tool.SELECTOR) {
                    selectingStart = new Point((int) (mx), (int) (my));
                    selectingSize = new Point(0, 0);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startM = null;
                if (tool == Tool.MOVE_VIEW)
                    selectingSize = null;
                else if (tool == Tool.SELECTOR)
                    selectingSize = new Point(0, 0);
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) scale *= addScale;
                else scale *= remScale;
                repaint();
            }
        }


    }
}
