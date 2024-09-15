package com.xebisco.yieldengine.tilemapeditor.tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public interface Tile extends Serializable {
    Tile load();
    String getName();
    void setName(String name);
    String getEntityCreatorClassName();
    void setEntityCreatorClassName(String entityCreatorClassName);
    BufferedImage getImage();
    Image get100pImage();
    int getWidth();
    int getHeight();
}
