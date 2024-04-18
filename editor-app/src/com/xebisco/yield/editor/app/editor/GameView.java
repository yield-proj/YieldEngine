/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yield.editor.app.editor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class GameView extends JPanel {

    private Point2D.Float gridSize = new Point2D.Float(16, 16);

    private float gridOpacity = 15;

    private boolean dragging, moving;
    private Point lastDraggingPosition = new Point();
    private double viewPositionX, viewPositionY, mousePositionX, mousePositionY;

    private final JLabel xLabel, yLabel, zoomLabel, gridLabel;
    private final EditorScene scene;
    private final ScenePanel scenePanel;

    private final ScenePanel.EntitiesTree entitiesTree;

    private final Timer RIGHT_TIMER = new Timer(16, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewPositionX += 1 / zoom * 2;
            repaint();
        }
    }), LEFT_TIMER = new Timer(16, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewPositionX -= 1 / zoom * 2;
            repaint();
        }
    }), UP_TIMER = new Timer(16, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewPositionY -= 1 / zoom * 2;
            repaint();
        }
    }), DOWN_TIMER = new Timer(16, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            viewPositionY += 1 / zoom * 2;
            repaint();
        }
    });

    private double zoom = 1;

    private EditorEntity selectedEntity;

    private boolean placeInGrid, xArrowSelected, yArrowSelected;

    private EntitySelectedFx entitySelectedFx = null;

    private static class EntitySelectedFx {
        int f = 30;
        EditorEntity entity;
    }

    public GameView(ScenePanel scenePanel, ScenePanel.EntitiesTree entitiesTree) {
        super(new BorderLayout());
        this.xLabel = new JLabel();
        this.yLabel = new JLabel();
        this.zoomLabel = new JLabel();
        this.gridLabel = new JLabel();
        this.scene = scenePanel.scene();
        this.scenePanel = scenePanel;

        JToolBar toolBar = new JToolBar();
        toolBar.addSeparator();
        toolBar.add(xLabel);
        toolBar.addSeparator();
        toolBar.add(yLabel);
        toolBar.addSeparator();
        toolBar.add(zoomLabel);
        toolBar.addSeparator();
        toolBar.add(gridLabel);

        add(toolBar, BorderLayout.NORTH);

        Intern intern = new Intern();

        intern.setMinimumSize(new Dimension(600, 400));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesTree, intern);

        add(splitPane);

        this.entitiesTree = entitiesTree;
    }

    class Intern extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

        private void updateLabels() {
            xLabel.setText(String.format("x: %.1f", viewPositionX));
            yLabel.setText(String.format("y: %.1f", viewPositionY));
            zoomLabel.setText(String.format("zoom: %.1f", zoom));
            gridLabel.setText(String.format("grid: (%.2f; %.2f)", gridSize.x, gridSize.y));
        }

        public Intern() {
            addMouseWheelListener(this);
            addMouseMotionListener(this);
            addMouseListener(this);
            addKeyListener(this);
            setFocusable(true);
            requestFocus();
            updateLabels();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (scene != null) {
                g.setColor(scene.backgroundColor());
                g.fillRect(0, 0, getWidth(), getHeight());
                Graphics2D g2 = (Graphics2D) g;

                double viewPositionX = GameView.this.viewPositionX - getWidth() / 2., viewPositionY = GameView.this.viewPositionY - getHeight() / 2.;
                double mousePositionX = GameView.this.mousePositionX - getWidth() / 2., mousePositionY = GameView.this.mousePositionY - getHeight() / 2.;

                if (zoom < 1) zoom = 1;
                g2.translate(getWidth() / 2., getHeight() / 2.);
                g2.scale(zoom, zoom);
                g2.translate(-getWidth() / 2., -getHeight() / 2.);
                g2.translate(-viewPositionX, -viewPositionY);

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                updateLabels();

                //TODO draw objs

                for (EditorEntity e : scene().entities()) {
                    e.draw(g2);
                    g2.setColor(new Color(213, 213, 213, 65));
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Point2D.Double position = e.realPosition();
                    g2.fill(new Arc2D.Double(position.x - (8 / zoom / 2), -position.y - (8 / zoom / 2), 8 / zoom, 8 / zoom, 0, 360, Arc2D.CHORD));
                }

                g2.setStroke(new BasicStroke((float) (1 / zoom)));

                g2.setColor(new Color(255 - scene.backgroundColor().getRed(), 255 - scene.backgroundColor().getGreen(), 255 - scene.backgroundColor().getBlue(), (int) ((gridOpacity / 100f) * 255)).brighter());

                int startX = (int) viewPositionX;
                while (startX % gridSize.x != 0) startX++;

                for (int i = 0; i < getWidth() / gridSize.x + 1; i++) {
                    g2.draw(new Line2D.Double(i * gridSize.x + startX, viewPositionY, i * gridSize.x + startX, getHeight() + viewPositionY));
                }

                int startY = (int) viewPositionY;
                while (startY % gridSize.y != 0) startY++;

                for (int i = 0; i < getHeight() / gridSize.y + 1; i++) {
                    g2.draw(new Line2D.Double(viewPositionX, i * gridSize.y + startY, getWidth() + viewPositionX, i * gridSize.y + startY));
                }

                //Y
                g.setColor(new Color(10, 255, 10, 100));
                g.drawLine(0, (int) viewPositionY, 0, (int) (getHeight() + viewPositionY));


                //X
                g.setColor(new Color(255 - 100, 10, 10, 100));
                g.drawLine((int) viewPositionX, 0, (int) (getWidth() + viewPositionX), 0);

                if (placeInGrid) {
                    Point loc = closerGridLocationTo(new Point2D.Double(mousePositionX, mousePositionY));
                    double ballSize = 12 / zoom;
                    Shape circle = new Arc2D.Double(loc.x - ballSize / 2, loc.y - ballSize / 2, ballSize, ballSize, 0, 360, Arc2D.CHORD);
                    g2.fill(circle);
                }

                if (entitySelectedFx != null) {
                    g2.setColor(new Color(0, 41, 253, 174));
                    g2.setColor(new Color(66, 213, 255, 174));
                }

                if (selectedEntity != null) {
                    Point2D.Double ePosition = selectedEntity.realPosition();
                    drawArrows(g2, ePosition.x, -ePosition.y);
                }
            } else {
                g.setColor(getBackground().darker());
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(getForeground());
                String noSceneLoaded = "No Scene Loaded";
                int s = g.getFontMetrics().stringWidth(noSceneLoaded);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.drawString(noSceneLoaded, getWidth() / 2 - s / 2, getHeight() / 2 + g.getFont().getSize() / 2);
            }
        }

        private Point closerGridLocationTo(Point2D.Double point) {
            boolean negativeX = point.x < 0, negativeY = point.y < 0;
            Point p = new Point((int) ((((int) Math.abs(point.getX()) + (int) gridSize.x / 2) / (int) gridSize.x) * (int) gridSize.x), (int) ((((int) Math.abs(point.getY()) + (int) gridSize.y / 2) / (int) gridSize.y) * (int) gridSize.y));
            if (negativeX) p.x *= -1;
            if (negativeY) p.y *= -1;
            return p;
        }

        private void drawArrows(Graphics2D g, double x, double y) {

            g.setStroke(new BasicStroke((float) (2 / zoom)));
            g.setColor(new Color(100, 100, 255, 120));
            g.draw(new Rectangle2D.Double(x, y - 14 / zoom, 14 / zoom, 14 / zoom));

            double mousePositionX = GameView.this.mousePositionX - getWidth() / 2., mousePositionY = GameView.this.mousePositionY - getHeight() / 2.;

            //X Arrow
            if (!moving)
                if (mousePositionX >= x && mousePositionX <= x + 120. / zoom && mousePositionY >= y - 14 / zoom && mousePositionY <= y + 7 / zoom) {
                    xArrowSelected = true;
                } else {
                    xArrowSelected = false;
                }
            if (xArrowSelected) g.setColor(Color.YELLOW);
            else g.setColor(Color.RED);

            g.setStroke(new BasicStroke((float) (4 / zoom)));
            g.draw(new Line2D.Double(x, y, x + 98. / zoom, y));
            g.draw(new Line2D.Double(x + 100. / zoom - 8. / zoom, y - 8. / zoom, x + 100. / zoom, y));
            g.draw(new Line2D.Double(x + 100. / zoom - 8. / zoom, y + 8. / zoom, x + 100. / zoom, y));


            //Y Arrow
            if (!moving)
                if (mousePositionY <= y && mousePositionY >= y - 120. / zoom && mousePositionX >= x - 7 / zoom && mousePositionX <= x + 14 / zoom) {
                    yArrowSelected = true;
                } else {
                    yArrowSelected = false;
                }
            if (yArrowSelected) g.setColor(Color.YELLOW);
            else g.setColor(Color.GREEN);

            g.draw(new Line2D.Double(x, y, x, y - 98. / zoom));
            g.draw(new Line2D.Double(x - 8. / zoom, y - 100. / zoom + 8. / zoom, x, y - 100. / zoom));
            g.draw(new Line2D.Double(x + 8. / zoom, y - 100. / zoom + 8. / zoom, x, y - 100. / zoom));
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (scene == null) return;
            double nzoom = zoom - e.getPreciseWheelRotation();
            if (nzoom < 1) nzoom = 1;
            zoom = nzoom;
            mouseMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (scene == null) return;

            double mousePositionX = GameView.this.mousePositionX - getWidth() / 2., mousePositionY = GameView.this.mousePositionY - getHeight() / 2.;

            if (dragging) {
                viewPositionX += ((double) (lastDraggingPosition.x - e.getLocationOnScreen().x) / zoom);
                viewPositionY += ((double) (lastDraggingPosition.y - e.getLocationOnScreen().y) / zoom);
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
                repaint();
            }

            if (moving) {
                mouseMoved(e);
                if (selectedEntity != null) {
                    if (xArrowSelected) {
                        if (placeInGrid) {
                            Point gridPosition = closerGridLocationTo(new Point2D.Double(mousePositionX, mousePositionY));
                            selectedEntity.setPosition(gridPosition.x, selectedEntity.position().y);
                        } else
                            selectedEntity.setPosition(selectedEntity.position().x - ((double) (lastDraggingPosition.x - e.getLocationOnScreen().x) / zoom), selectedEntity.position().y);
                    }
                    if (yArrowSelected) {
                        if (placeInGrid) {
                            Point gridPosition = closerGridLocationTo(new Point2D.Double(mousePositionX, mousePositionY));
                            selectedEntity.setPosition(selectedEntity.position().x, -gridPosition.y);
                        } else
                            selectedEntity.setPosition(selectedEntity.position().x, selectedEntity.position().y + ((double) (lastDraggingPosition.y - e.getLocationOnScreen().y) / zoom));
                    }
                    lastDraggingPosition.setLocation(e.getLocationOnScreen());
                    scenePanel.saveSceneEntity(selectedEntity, false);
                }
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (scene == null) return;
            mousePositionX = e.getX() / zoom + viewPositionX + getWidth() / 2. - (getWidth() / 2. / zoom);
            mousePositionY = e.getY() / zoom + viewPositionY + getHeight() / 2. - (getHeight() / 2. / zoom);
            repaint();

            setToolTipText("");

            for (EditorEntity entity : scene.entities()) {
                Dimension d = entity.size();
                Point2D.Double p = entity.realPosition();
                if (d.width < 8) d.width = 8;
                if (d.height < 8) d.height = 8;
                if (p.x - d.width / 2. < mousePositionX && p.x + d.width / 2. > mousePositionX && -p.y - d.height / 2. < mousePositionY && -p.y + d.height / 2. > mousePositionY) {
                    setToolTipText(entity.entityName());
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (scene == null) return;
            EditorEntity lastSelected = selectedEntity;
            if (!moving) selectedEntity = null;
            for (EditorEntity entity : scene.entities()) {
                if (entity.inside(new Point2D.Double(mousePositionX - getWidth() / 2., mousePositionY - getHeight() / 2.))) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (lastSelected != null && lastSelected.isChildOf(entity)) {
                            selectedEntity = lastSelected;
                            break;
                        }
                        selectedEntity = entity;
                        entitiesTree.tree.clearSelection();
                        for (int i = 0; i < entitiesTree.tree.getRowCount(); i++) {
                            Object o = ((DefaultMutableTreeNode) entitiesTree.tree.getPathForRow(i).getLastPathComponent()).getUserObject();
                            if (o instanceof ScenePanel.EntitiesTree.Tree.EntityNode e1) {
                                if (e1.editorEntity().equals(selectedEntity)) {
                                    entitiesTree.tree.addSelectionPath(entitiesTree.tree.getPathForRow(i));
                                    break;
                                }
                            }
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON2) {
                        scenePanel.openEntity(entity, null);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        scenePanel.entityPopupPanel(this, entity);
                        if (!scenePanel.entityExists(entity)) selectedEntity = null;
                    }
                }
            }

            if (e.getButton() == MouseEvent.BUTTON3 && selectedEntity == null) {
                if (this.getMousePosition() != null)
                    scenePanel.rootEntityPopupPanel(this, new Point2D.Double(mousePositionX - getWidth() / 2., -mousePositionY + getHeight() / 2.));
            }

            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            requestFocus();
            if (scene == null) return;
            if (e.getButton() == MouseEvent.BUTTON2) {
                dragging = true;
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
            }

            if (e.getButton() == MouseEvent.BUTTON1) {
                moving = true;
                lastDraggingPosition.setLocation(e.getLocationOnScreen());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragging = false;
            moving = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_EQUALS && gridSize.x < 2000 && gridSize.y < 2000)
                gridSize.setLocation(new Point2D.Float(gridSize.x * 2, gridSize.y * 2));
            if (e.getKeyCode() == KeyEvent.VK_MINUS && gridSize.x > 2 && gridSize.y > 2)
                gridSize.setLocation(new Point2D.Float(gridSize.x / 2, gridSize.y / 2));
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) placeInGrid = !placeInGrid;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) RIGHT_TIMER.start();
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) LEFT_TIMER.start();
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) UP_TIMER.start();
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) DOWN_TIMER.start();

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //sif (e.getKeyCode() == KeyEvent.VK_CONTROL) placeInGrid = false;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) RIGHT_TIMER.stop();
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) LEFT_TIMER.stop();
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) UP_TIMER.stop();
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) DOWN_TIMER.stop();

            repaint();
        }
    }

    public Point2D.Float gridSize() {
        return gridSize;
    }

    public GameView setGridSize(Point2D.Float gridSize) {
        this.gridSize = gridSize;
        return this;
    }

    public boolean dragging() {
        return dragging;
    }

    public GameView setDragging(boolean dragging) {
        this.dragging = dragging;
        return this;
    }

    public boolean moving() {
        return moving;
    }

    public GameView setMoving(boolean moving) {
        this.moving = moving;
        return this;
    }

    public Point lastDraggingPosition() {
        return lastDraggingPosition;
    }

    public GameView setLastDraggingPosition(Point lastDraggingPosition) {
        this.lastDraggingPosition = lastDraggingPosition;
        return this;
    }

    public double viewPositionX() {
        return viewPositionX;
    }

    public GameView setViewPositionX(double viewPositionX) {
        this.viewPositionX = viewPositionX;
        return this;
    }

    public double viewPositionY() {
        return viewPositionY;
    }

    public GameView setViewPositionY(double viewPositionY) {
        this.viewPositionY = viewPositionY;
        return this;
    }

    public double mousePositionX() {
        return mousePositionX;
    }

    public GameView setMousePositionX(double mousePositionX) {
        this.mousePositionX = mousePositionX;
        return this;
    }

    public double mousePositionY() {
        return mousePositionY;
    }

    public GameView setMousePositionY(double mousePositionY) {
        this.mousePositionY = mousePositionY;
        return this;
    }

    public JLabel xLabel() {
        return xLabel;
    }

    public JLabel yLabel() {
        return yLabel;
    }

    public JLabel zoomLabel() {
        return zoomLabel;
    }

    public JLabel gridLabel() {
        return gridLabel;
    }

    public EditorScene scene() {
        return scene;
    }

    public Timer RIGHT_TIMER() {
        return RIGHT_TIMER;
    }

    public Timer LEFT_TIMER() {
        return LEFT_TIMER;
    }

    public Timer UP_TIMER() {
        return UP_TIMER;
    }

    public Timer DOWN_TIMER() {
        return DOWN_TIMER;
    }

    public double zoom() {
        return zoom;
    }

    public GameView setZoom(double zoom) {
        this.zoom = zoom;
        return this;
    }

    public EditorEntity selectedEntity() {
        return selectedEntity;
    }

    public GameView setSelectedEntity(EditorEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
        return this;
    }

    public boolean placeInGrid() {
        return placeInGrid;
    }

    public GameView setPlaceInGrid(boolean placeInGrid) {
        this.placeInGrid = placeInGrid;
        return this;
    }

    public boolean xArrowSelected() {
        return xArrowSelected;
    }

    public GameView setxArrowSelected(boolean xArrowSelected) {
        this.xArrowSelected = xArrowSelected;
        return this;
    }

    public boolean yArrowSelected() {
        return yArrowSelected;
    }

    public GameView setyArrowSelected(boolean yArrowSelected) {
        this.yArrowSelected = yArrowSelected;
        return this;
    }

    public EntitySelectedFx entitySelectedFx() {
        return entitySelectedFx;
    }

    public GameView setEntitySelectedFx(EntitySelectedFx entitySelectedFx) {
        this.entitySelectedFx = entitySelectedFx;
        return this;
    }
}