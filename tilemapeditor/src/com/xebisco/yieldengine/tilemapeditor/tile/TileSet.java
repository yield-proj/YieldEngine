package com.xebisco.yieldengine.tilemapeditor.tile;

import com.xebisco.yieldengine.uiutils.ImageCache;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class TileSet implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Tile> tiles = new ArrayList<>();

    private File imageSheetFile;

    public void load() throws IOException {
        tiles.forEach(Tile::load);
    }

    public BufferedImage getImageSheet() {
        return ImageCache.get(imageSheetFile);
    }

    public File getImageSheetFile() {
        return imageSheetFile;
    }

    public void setImageSheetFile(File imageSheetFile) {
        this.imageSheetFile = imageSheetFile;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
