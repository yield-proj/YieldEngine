package com.xebisco.yieldengine.tileseteditor.tile;

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
    Image get60pImage();
    int getWidth();
    int getHeight();
}
