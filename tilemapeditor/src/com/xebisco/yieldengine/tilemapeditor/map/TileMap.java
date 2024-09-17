package com.xebisco.yieldengine.tilemapeditor.map;

import com.xebisco.yieldengine.uiutils.Point;

import java.io.Serializable;

public class TileMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private Point<Integer> tileMapSize;
    private Point<Integer> tileMapGridSize;

    private int[][] tileMap;

    public TileMap(Point<Integer> tileMapSize, Point<Integer> tileMapGridSize) {
        set(tileMapSize, tileMapGridSize);
    }

    public void set(Point<Integer> tileMapSize, Point<Integer> tileMapGridSize) {
        this.tileMapSize = tileMapSize;
        this.tileMapGridSize = tileMapGridSize;
        tileMap = new int[tileMapSize.getX()][tileMapSize.getY()];
        for(int x = 0; x < tileMapSize.getX(); x++) {
            for(int y = 0; y < tileMapSize.getY(); y++) {
                tileMap[x][y] = -1;
            }
        }
    }

    public Point<Integer> getTileMapSize() {
        return tileMapSize;
    }

    private void setTileMapSize(Point<Integer> tileMapSize) {
        this.tileMapSize = tileMapSize;
    }

    public Point<Integer> getTileMapGridSize() {
        return tileMapGridSize;
    }

    private void setTileMapGridSize(Point<Integer> tileMapGridSize) {
        this.tileMapGridSize = tileMapGridSize;
    }

    public int[][] getTileMap() {
        return tileMap;
    }

    private void setTileMap(int[][] tileMap) {
        this.tileMap = tileMap;
    }
}
