package com.xebisco.yieldengine.tileseteditor.tile;

import com.xebisco.yieldengine.uiutils.ImageCache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SubImageTile implements Tile {
    private transient BufferedImage image;
    private transient Image scaledImage;
    private final File imageSheetFile;
    private transient BufferedImage imageSheet;
    private String name, entityCreatorClassName;
    private com.xebisco.yieldengine.uiutils.Point<Integer> point, size;

    public SubImageTile(File imageSheetFile, String name, String entityCreatorClassName, com.xebisco.yieldengine.uiutils.Point<Integer> point, com.xebisco.yieldengine.uiutils.Point<Integer> size) {
        this.imageSheetFile = imageSheetFile;
        this.name = name;
        this.entityCreatorClassName = entityCreatorClassName;
        this.point = point;
        this.size = size;
    }

    @Override
    public SubImageTile load() {
        imageSheet = ImageCache.get(imageSheetFile);
        image = imageSheet.getSubimage(point.getX(), point.getY(), size.getX(), size.getY());

        int w = image.getWidth(), h = image.getHeight();
        if(w > h) {
            w = 100;
            h = -1;
        } else {
            w = -1;
            h = 100;
        }

        scaledImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return this;
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

    public com.xebisco.yieldengine.uiutils.Point<Integer> getPoint() {
        return point;
    }

    public void setPoint(com.xebisco.yieldengine.uiutils.Point<Integer> point) {
        this.point = point;
    }

    public com.xebisco.yieldengine.uiutils.Point<Integer> getSize() {
        return size;
    }

    public void setSize(com.xebisco.yieldengine.uiutils.Point<Integer> size) {
        this.size = size;
    }
}
