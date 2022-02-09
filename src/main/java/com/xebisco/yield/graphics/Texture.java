package com.xebisco.yield.graphics;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Texture {

    private Image image;
    private final String internalPath;

    public Texture(Image image) {
        this.image = image;
        internalPath = "";
    }

    public Texture(String internalPath) {
        this.internalPath = internalPath;
        this.image = new ImageIcon(Objects.requireNonNull(Texture.class.getResource(internalPath))).getImage();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getInternalPath() {
        return internalPath;
    }
}
