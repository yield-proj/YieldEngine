package com.xebisco.yieldengine.tilemapeditor.tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TileSet {
    private final List<Tile> tiles = new ArrayList<>();

    private BufferedImage imageSheet;
    private File imageSheetFile;

    public void load() throws IOException {
        if (imageSheetFile != null)
            imageSheet = ImageIO.read(imageSheetFile);
        else imageSheet = null;
        tiles.forEach(Tile::load);
    }

    public BufferedImage getImageSheet() {
        return imageSheet;
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
