package com.xebisco.yieldengine.tileseteditor.tile;

import com.xebisco.yieldengine.uiutils.fields.FileExtensions;
import com.xebisco.yieldengine.uiutils.fields.Visible;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTile implements Tile {
    private transient BufferedImage image;
    private transient Image scaledImage;
    @Visible
    @FileExtensions(extensions = {"PNG", "JPG", "JPEG", "BMP", "WBMP", "GIF"}, description = "Image Files")
    private File imagePath;
    @Visible
    private String name, entityCreatorClassName;

    public ImageTile(File imagePath, String name, String entityCreatorClassName) {
        this.imagePath = imagePath;
        this.name = name;
        this.entityCreatorClassName = entityCreatorClassName;
    }

    @Override
    public ImageTile load() {
        try {
            image = ImageIO.read(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public File getImagePath() {
        return imagePath;
    }

    public void setImagePath(File imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEntityCreatorClassName(String entityCreatorClassName) {
        this.entityCreatorClassName = entityCreatorClassName;
    }
}
