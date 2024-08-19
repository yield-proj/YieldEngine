package com.xebisco.yieldengine.tilemapeditor.imagecutter;

import java.awt.*;

public class ImageCut {
    private Point point;
    private Dimension size;

    public ImageCut(Point point, Dimension size) {
        this.point = point;
        this.size = size;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }
}
