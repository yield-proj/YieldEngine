package com.xebisco.yield;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Texture extends RelativeFile {

    private Image image;
    public static int imageType = BufferedImage.SCALE_SMOOTH;

    public Texture(Image image) {
        super("");
        this.image = image;
    }

    public Texture(Texture texture, int x, int y, int width, int height) {
        super(texture.getRelativePath());
        final BufferedImage img = new BufferedImage(image.getWidth(null), image.getHeight(null), imageType);
        img.getGraphics().drawImage(texture.image, 0, 0, null);
        this.image = img.getSubimage(x, y, width, height);
    }

    public Texture(Texture texture, int x, int y, int width, int height, int imageType) {
        super(texture.getRelativePath());
        final BufferedImage img = new BufferedImage(image.getWidth(null), image.getHeight(null), imageType);
        img.getGraphics().drawImage(texture.image, 0, 0, null);
        this.image = img.getSubimage(x, y, width, height);
    }

    public Texture(final String relativePath) {
        super(relativePath);
        this.image = new ImageIcon(Objects.requireNonNull(Yld.class.getResource(relativePath))).getImage();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
