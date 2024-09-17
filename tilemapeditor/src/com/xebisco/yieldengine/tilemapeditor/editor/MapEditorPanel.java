package com.xebisco.yieldengine.tilemapeditor.editor;

import com.xebisco.yieldengine.tilemapeditor.map.TileMap;
import com.xebisco.yieldengine.uiutils.ZoomPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class MapEditorPanel extends ZoomPanel {

    private final TileMap tileMap;
    private boolean showGrid = true;

    private int mouseGridX, mouseGridY;

    public MapEditorPanel(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        processZoom((Graphics2D) g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, tileMap.getTileMapSize().getX() * tileMap.getTileMapGridSize().getX(), tileMap.getTileMapSize().getY() * tileMap.getTileMapGridSize().getY());

        if(mouseGridX != -1 && mouseGridY != -1) {
            g.setColor(new Color(4, 45, 255, 60));
            g.fillRect(mouseGridX * tileMap.getTileMapGridSize().getX(), mouseGridY * tileMap.getTileMapGridSize().getY(), tileMap.getTileMapGridSize().getX(), tileMap.getTileMapGridSize().getY());
        }

        if(showGrid) {
            Line2D.Float l = new Line2D.Float();

            float diff = (float) (1f / getZoomFactor()) / 2f;
            ((Graphics2D) g).setStroke(new BasicStroke(diff * 2));

            int gridSizeX = tileMap.getTileMapGridSize().getX(), width = tileMap.getTileMapSize().getX() * tileMap.getTileMapGridSize().getX();
            int gridSizeY = tileMap.getTileMapGridSize().getY(), height = tileMap.getTileMapSize().getY() * tileMap.getTileMapGridSize().getY();

            for (int x = 0; x < tileMap.getTileMapSize().getX(); x++) {
                g.setColor(new Color(255, 255, 255, 100));
                l.setLine(x * gridSizeX - diff, 0, x * gridSizeX, height - diff);
                ((Graphics2D) g).draw(l);

                g.setColor(new Color(0, 0, 0, 50));
                l.setLine(x * gridSizeX + diff, 0, x * gridSizeX + diff, height);
                ((Graphics2D) g).draw(l);
            }
            for (int y = 0; y < tileMap.getTileMapSize().getY(); y++) {
                g.setColor(new Color(255, 255, 255, 100));
                l.setLine(0, y * gridSizeY - diff, width, y * gridSizeY - diff);
                ((Graphics2D) g).draw(l);

                g.setColor(new Color(0, 0, 0, 50));
                l.setLine(0, y * gridSizeY + diff, width, y * gridSizeY + diff);
                ((Graphics2D) g).draw(l);
            }
        }


        drawInfo((Graphics2D) g);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        updateMouse();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        updateMouse();
    }

    private void updateMouse() {
        Point2D.Double m = zoomMouse();
        if(m ==  null) return;
        mouseGridX = Math.floorDiv((int) m.getX(), tileMap.getTileMapGridSize().getX());
        if(mouseGridX < 0 || mouseGridX >= tileMap.getTileMapSize().getX()) {
            mouseGridX = -1;
        }
        mouseGridY = Math.floorDiv((int) m.getY(), tileMap.getTileMapGridSize().getY());
        if(mouseGridY < 0 || mouseGridY >= tileMap.getTileMapSize().getY()) {
            mouseGridY = -1;
        }
        repaint();
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }
}
