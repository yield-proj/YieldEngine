package com.xebisco.yieldengine.tileseteditor.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FillerTile implements Tile {
    @Override
    public FillerTile load() {
        return this;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getEntityCreatorClassName() {
        return null;
    }

    @Override
    public void setEntityCreatorClassName(String entityCreatorClassName) {

    }

    @Override
    public BufferedImage getImage() {
        return null;
    }

    @Override
    public Image get60pImage() {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
