package com.xebisco.yieldengine.tilemapeditor.tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SubImageTile implements Tile {
    private transient BufferedImage image;
    private transient Image scaledImage;
    private final transient BufferedImage imageSheet;
    private String name, entityCreatorClassName;
    private Point point;
    private Dimension size;

    public SubImageTile(BufferedImage imageSheet, String name, String entityCreatorClassName, Point point, Dimension size) {
        this.imageSheet = imageSheet;
        this.name = name;
        this.entityCreatorClassName = entityCreatorClassName;
        this.point = point;
        this.size = size;
    }

    @Override
    public void load() {
        image = imageSheet.getSubimage(point.x, point.y, size.width, size.height);

        int w = image.getWidth(), h = image.getHeight();
        if(w > h) {
            w = 100;
            h = -1;
        } else {
            w = -1;
            h = 100;
        }

        scaledImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEntityCreatorClassName() {
        return entityCreatorClassName;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public Image get100pImage() {
        return scaledImage;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImageSheet() {
        return imageSheet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEntityCreatorClassName(String entityCreatorClassName) {
        this.entityCreatorClassName = entityCreatorClassName;
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
