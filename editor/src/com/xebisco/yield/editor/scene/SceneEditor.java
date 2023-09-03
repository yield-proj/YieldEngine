package com.xebisco.yield.editor.scene;

import com.xebisco.yield.editor.Assets;
import com.xebisco.yield.editor.YieldToolBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SceneEditor extends JPanel {

    enum Tool {
        SELECTOR,
        MOVE_OBJ,
        MOVE_VIEW
    }

    private Tool tool = Tool.SELECTOR;
    private SceneView sceneView;
    private double gridX = 16, gridY = 16;

    public SceneEditor() {
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

        sceneView = new SceneView();
        add(new SceneScrollPane());
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

    class SceneView extends JPanel {
        private double zoom = 1;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(zoom, zoom);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            Color gridColor = getBackground().brighter();
            g.setColor(gridColor);
            g2.setStroke(new BasicStroke((float) (1 / zoom)));
            for (double i = gridX; i < getPreferredSize().width; i += gridX) {
                g.drawLine((int) i, 0, (int) i, getPreferredSize().height);
            }
            for (double i = gridY; i < getPreferredSize().height; i += gridY) {
                g.drawLine(0, (int) i, getPreferredSize().width, (int) i);
            }
        }

        @Override
        public void validate() {
            super.validate();
            setPreferredSize(new Dimension((int) (100 * zoom), (int) (100 * zoom)));
        }
    }

    class SceneScrollPane extends JScrollPane implements MouseWheelListener, MouseMotionListener, MouseListener {

        public SceneScrollPane() {
            super(sceneView);
            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
        }

        public void zoomOut(Point point) {
            sceneView.zoom = sceneView.zoom * 0.9;
            Point pos = this.getViewport().getViewPosition();

            int newX = (int) (point.x * (0.9f - 1f) + 0.9f * pos.x);
            int newY = (int) (point.y * (0.9f - 1f) + 0.9f * pos.y);
            this.getViewport().setViewPosition(new Point(newX, newY));

            revalidate();
            repaint();
        }

        public void zoomIn(Point point) {
            sceneView.zoom = sceneView.zoom * 1.1;
            Point pos = this.getViewport().getViewPosition();

            int newX = (int) (point.x * (1.1f - 1f) + 1.1f * pos.x);
            int newY = (int) (point.y * (1.1f - 1f) + 1.1f * pos.y);
            this.getViewport().setViewPosition(new Point(newX, newY));

            revalidate();
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() > 0) zoomOut(e.getPoint());
            else zoomIn(e.getPoint());
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
