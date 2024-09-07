package com.xebisco.yieldengine.tilemapeditor.imagecutter;

import com.xebisco.yieldengine.uiutils.Utils;
import com.xebisco.yieldengine.uiutils.fields.FieldsPanel;
import com.xebisco.yieldengine.uiutils.fields.NumberFieldPanel;
import com.xebisco.yieldengine.uiutils.fields.PointFieldPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Map;
import javax.swing.*;

public class ZoomPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener, KeyListener {
    private double zoomFactor = 1;
    protected double prevZoomFactor = 1;
    private boolean dragging;
    private double xOffset = 0;
    private double yOffset = 0;
    private Point startPoint;

    public ZoomPanel() {
        initComponent();
    }

    private void initComponent() {
        setFocusable(true);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
    }

    private AffineTransform at = new AffineTransform();

    public void processZoom(Graphics2D g2) {
        /*
        if (zoomer) {
            AffineTransform at = new AffineTransform();

            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

            double zoomDiv = zoomFactor / prevZoomFactor;

            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

            at.translate(xOffset, yOffset);
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;
            g2.transform(at);
            zoomer = false;
        }

        if (dragger) {
            AffineTransform at = new AffineTransform();
            at.translate(xOffset + xDiff, yOffset + yDiff);
            at.scale(zoomFactor, zoomFactor);
            g2.transform(at);

            if (released) {
                xOffset += xDiff;
                yOffset += yDiff;
                dragger = false;
            }

        }
        */

        g2.setTransform(new AffineTransform(at));
    }

    public void drawInfo(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));

        AffineTransform at = g2.getTransform();

        g2.setTransform(new AffineTransform());

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD));

        String text = String.format("zoom(%.2f); offset(%.1f; %.1f)", zoomFactor, xOffset / zoomFactor, yOffset / zoomFactor);

        int width = g2.getFontMetrics().stringWidth(text) + 6, height = g2.getFont().getSize() + 6;

        g2.setColor(new Color(0, 0, 0, 100));

        g2.fillRoundRect(getWidth() - width - 5, getHeight() - 40 - height, width, height, 10, 10);

        g2.setColor(new Color(255, 255, 255, 200));

        g2.drawString(text, getWidth() - width + 3 - 5, getHeight() - 40 - 3);

        g2.drawRoundRect(getWidth() - width - 5, getHeight() - 40 - height, width, height, 10, 10);

        at.setTransform(at);
    }

    public Point2D.Double zoomMouse() {
        Point mouse = getMousePosition();
        if (mouse != null) {
            return new Point2D.Double(((mouse.x - getxOffset()) / getZoomFactor()), ((mouse.y - getyOffset()) / getZoomFactor()));
        }
        return null;
    }

    public JMenuBar addViewMenu(JMenuBar menuBar) {

        JMenu viewMenu = new JMenu("View");

        JMenuItem resetButton = new JMenuItem(new AbstractAction("Reset") {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomFactor = 1;
                updateZoom();
                xOffset = 0;
                yOffset = 0;
                updateOffset();
                repaint();
            }
        });
        resetButton.setMnemonic('R');
        viewMenu.add(resetButton);

        JMenuItem gotoButton = new JMenuItem(new AbstractAction("Go To") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Serializable> values = Utils.showOptions(SwingUtilities.getWindowAncestor(ZoomPanel.this),
                        new NumberFieldPanel<>("zoom factor", zoomFactor, true, true),
                        new PointFieldPanel<>("point", Double.class, new com.xebisco.yieldengine.uiutils.Point<>((xOffset / zoomFactor), (yOffset / zoomFactor)), true, false, true)
                );
                zoomFactor = Double.parseDouble((String) values.get("zoom factor"));
                if (zoomFactor < .1) zoomFactor = .1;
                if (zoomFactor > 10) zoomFactor = 10;
                updateZoom();

                //noinspection unchecked
                com.xebisco.yieldengine.uiutils.Point<Double> offset = (com.xebisco.yieldengine.uiutils.Point<Double>) values.get("point");
                xOffset = offset.getX() * zoomFactor;
                yOffset = offset.getY() * zoomFactor;
                updateOffset();
            }
        });
        gotoButton.setMnemonic('T');
        viewMenu.add(gotoButton);
        menuBar.add(viewMenu);

        return menuBar;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            if (zoomFactor > 10) zoomFactor = 10;
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
            if (zoomFactor < .1) zoomFactor = .1;
        }

        double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

        double zoomDiv = zoomFactor / prevZoomFactor;

        xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
        yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

        prevZoomFactor = zoomFactor;

        updateZoom();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            Point curPoint = e.getLocationOnScreen();
            xOffset += curPoint.x - startPoint.x;
            yOffset += curPoint.y - startPoint.y;
            startPoint = curPoint;

            updateOffset();

            repaint();
        }

    }

    private void updateZoom() {
        at = new AffineTransform();

        at.translate(xOffset, yOffset);
        at.scale(zoomFactor, zoomFactor);
    }

    private void updateOffset() {
        at = new AffineTransform();
        at.translate(xOffset, yOffset);
        at.scale(zoomFactor, zoomFactor);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = MouseInfo.getPointerInfo().getLocation();
        if (e.getButton() == MouseEvent.BUTTON2) dragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("aaaa");

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("bbbb");

    }
}