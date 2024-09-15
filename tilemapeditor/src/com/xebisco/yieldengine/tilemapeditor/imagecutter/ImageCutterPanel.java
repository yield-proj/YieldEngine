package com.xebisco.yieldengine.tilemapeditor.imagecutter;

import com.xebisco.yieldengine.tilemapeditor.tile.SubImageTile;
import com.xebisco.yieldengine.tilemapeditor.tile.Tile;
import com.xebisco.yieldengine.tilemapeditor.tile.TileSet;
import com.xebisco.yieldengine.uiutils.ColliderRender;
import com.xebisco.yieldengine.uiutils.Point;
import com.xebisco.yieldengine.uiutils.Utils;
import com.xebisco.yieldengine.uiutils.ZoomPanel;
import com.xebisco.yieldengine.uiutils.fields.PointFieldPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;

public class ImageCutterPanel extends JPanel {

    private boolean drawGrid;
    private Point<Integer> gridSize = new Point<>(16, 16);

    private final TileSet tileSet;

    private ZoomPanel imagePanel = new ZoomPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            processZoom((Graphics2D) g);
            g.drawImage(tileSet.getImageSheet(), 0, 0, this);

            if (drawGrid) {

                Line2D.Float l = new Line2D.Float();

                float diff = (float) (1f / getZoomFactor()) / 2f;
                ((Graphics2D) g).setStroke(new BasicStroke(diff * 2));


                for (int x = 0; x < tileSet.getImageSheet().getWidth() / gridSize.getX(); x++) {
                    g.setColor(new Color(255, 255, 255, 100));
                    l.setLine(x * gridSize.getX() - diff, 0, x * gridSize.getX(), tileSet.getImageSheet().getHeight() - diff);
                    ((Graphics2D) g).draw(l);

                    g.setColor(new Color(0, 0, 0, 50));
                    l.setLine(x * gridSize.getX() + diff, 0, x * gridSize.getX() + diff, tileSet.getImageSheet().getHeight());
                    ((Graphics2D) g).draw(l);
                }
                for (int y = 0; y < tileSet.getImageSheet().getHeight() / gridSize.getY(); y++) {
                    g.setColor(new Color(255, 255, 255, 100));
                    l.setLine(0, y * gridSize.getY() - diff, tileSet.getImageSheet().getWidth(), y * gridSize.getY() - diff);
                    ((Graphics2D) g).draw(l);

                    g.setColor(new Color(0, 0, 0, 50));
                    l.setLine(0, y * gridSize.getY() + diff, tileSet.getImageSheet().getWidth(), y * gridSize.getY() + diff);
                    ((Graphics2D) g).draw(l);
                }
            }

            for (Tile tile : tileSet.getTiles()) {
                if (tile instanceof SubImageTile)
                    new ColliderRender(((SubImageTile) tile).getPoint(), ((SubImageTile) tile).getSize()).draw((Graphics2D) g, (float) (1 / getZoomFactor()), true, tile.getName());
            }

            drawInfo((Graphics2D) g);
        }

        public void addTile(Point<Integer> position) {
            String name = JOptionPane.showInputDialog("Tile name");
            if (name == null) return;
            /*ColliderRender colliderRender = new ColliderRender(
                    position,
                    new Point<>(gridSize.getX(), gridSize.getY())
            );*/
            tileSet.getTiles().add(new SubImageTile(tileSet.getImageSheetFile(), name, "", position, new Point<>(gridSize.getX(), gridSize.getY())).load());
            repaint();
        }

        @Override
        public JPopupMenu getComponentPopupMenu() {
            JPopupMenu popupMenu = new JPopupMenu();
            Point<Integer> p = new Point<>((int) ((getMousePosition().getX() - getxOffset()) / getZoomFactor()), (int) ((getMousePosition().getY() - getyOffset()) / getZoomFactor()));

            popupMenu.add(new AbstractAction("Add tile") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addTile(p);
                }
            });

            popupMenu.add(new AbstractAction("Add tile to grid") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addTile(new Point<>(Math.floorDiv(p.getX(), gridSize.getX()) * gridSize.getX(), Math.floorDiv(p.getY(), gridSize.getY()) * gridSize.getY()));
                }
            });

            return popupMenu;
        }

        boolean pressing, toRep;

        boolean sizeSelected;
        ColliderRender selectedTile;

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            if (selectedTile != null) {
                Point2D.Double p = new Point2D.Double((e.getX() - getxOffset()) / getZoomFactor(), (e.getY() - getyOffset()) / getZoomFactor());

                if (sizeSelected) {
                    selectedTile.getSize().setX((int) (p.getX() - selectedTile.getPosition().getX()));
                    selectedTile.getSize().setY((int) (p.getY() - selectedTile.getPosition().getY()));

                } else {
                    selectedTile.getPosition().setX((int) p.getX());
                    selectedTile.getPosition().setY((int) p.getY());
                }
                if (selectedTile.getSize().getX() > tileSet.getImageSheet().getWidth())
                    selectedTile.getSize().setX(Math.max(1, tileSet.getImageSheet().getWidth() - 100));
                if (selectedTile.getSize().getY() > tileSet.getImageSheet().getHeight())
                    selectedTile.getSize().setY(Math.max(1, tileSet.getImageSheet().getHeight() - 100));
                if (selectedTile.getPosition().getX() + selectedTile.getSize().getX() > tileSet.getImageSheet().getWidth()) {
                    selectedTile.getPosition().setX(tileSet.getImageSheet().getWidth() - selectedTile.getSize().getX());
                }
                if (selectedTile.getPosition().getY() + selectedTile.getSize().getY() > tileSet.getImageSheet().getHeight()) {
                    selectedTile.getPosition().setY(tileSet.getImageSheet().getHeight() - selectedTile.getSize().getY());
                }
                if (selectedTile.getPosition().getX() < 0) selectedTile.getPosition().setX(0);
                if (selectedTile.getPosition().getY() < 0) selectedTile.getPosition().setY(0);
            }

            updateTiles(e);
        }

        private void updateTiles(MouseEvent e) {
            Point2D.Double p = new Point2D.Double((e.getX() - getxOffset()) / getZoomFactor(), (e.getY() - getyOffset()) / getZoomFactor());

            boolean rep = false;

            selectedTile = null;

            for (Tile tileP : tileSet.getTiles()) {
                if (tileP instanceof SubImageTile) {
                    ColliderRender tile = new ColliderRender(((SubImageTile) tileP).getPoint(), ((SubImageTile) tileP).getSize());
                    boolean r = tile.positionB(p, (float) (1 / getZoomFactor()));
                    sizeSelected = false;
                    if (!r) {
                        r = tile.sizeB(p, (float) (1 / getZoomFactor()));
                        sizeSelected = true;
                    }
                    if (r) {
                        if (pressing) selectedTile = tile;
                        rep = true;
                        break;
                    }
                }
            }

            if (rep) toRep = true;

            if (toRep) {
                if (!rep) {
                    toRep = false;
                }
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);

            updateTiles(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                pressing = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                pressing = false;
            }
        }
    };

    public ImageCutterPanel(TileSet tileSet) {
        this.tileSet = tileSet;
        setLayout(new BorderLayout());

        add(imagePanel, BorderLayout.CENTER);


        JMenu gridMenu = new JMenu("Grid");
        gridMenu.setMnemonic(KeyEvent.VK_G);

        JMenuItem populateWithTiles = new JMenuItem(new AbstractAction("Populate") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(ImageCutterPanel.this, "Populate with tiles?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int i = 0;
                    for (int y = 0; y < Math.floorDiv(tileSet.getImageSheet().getHeight(), gridSize.getY()); y++) {
                        for (int x = 0; x < Math.floorDiv(tileSet.getImageSheet().getWidth(), gridSize.getX()); x++) {
                            tileSet.getTiles().add(new SubImageTile(tileSet.getImageSheetFile(), String.valueOf(i++), "", new Point<>(x * gridSize.getX(), y * gridSize.getY()), new Point<>(gridSize.getX(), gridSize.getY())).load());
                        }
                    }
                }
            }
        });
        gridMenu.add(populateWithTiles);

        JMenuItem setGridSize = new JMenuItem(new AbstractAction("Set Grid Size...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, Serializable> values = Utils.showOptions("Set Grid Size", SwingUtilities.getWindowAncestor(ImageCutterPanel.this), true, new PointFieldPanel<>("size", Integer.class, new Point<>(gridSize.getX(), gridSize.getY()), false, true, true));

                //noinspection unchecked
                gridSize = (Point<Integer>) values.get("size");
                imagePanel.repaint();
            }
        });
        gridMenu.add(setGridSize);

        JCheckBox showGridButton = new JCheckBox("Show Grid");
        showGridButton.addActionListener(e -> {
            drawGrid = showGridButton.isSelected();
            imagePanel.repaint();
        });
        gridMenu.add(showGridButton);

        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);

        //toolBar.add(gridMenu);
        imagePanel.addViewMenu(toolBar);


        toolBar.add(Utils.menuItemButton("Grid", KeyEvent.VK_G, populateWithTiles, setGridSize, showGridButton));
    }

    public boolean isDrawGrid() {
        return drawGrid;
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    public Point<Integer> getGridSize() {
        return gridSize;
    }

    public void setGridSize(Point<Integer> gridSize) {
        this.gridSize = gridSize;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public ZoomPanel getImagePanel() {
        return imagePanel;
    }

    public void setImagePanel(ZoomPanel imagePanel) {
        this.imagePanel = imagePanel;
    }
}
