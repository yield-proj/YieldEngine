package com.xebisco.yieldengine.tilemapeditor.tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public interface Tile extends Serializable {
    void load();
    String getName();
    String getEntityCreatorClassName();
    BufferedImage getImage();
    Image get100pImage();
    int getWidth();
    int getHeight();
}
