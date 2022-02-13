package com.xebisco.yield.graphics;

import com.xebisco.yield.RelativeFile;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Texture extends RelativeFile {

    private Image image;

    public Texture(Image image) {
        super("");
        this.image = image;
    }

    public Texture(final String relativePath) {
        super(relativePath);
        this.image = new ImageIcon(Objects.requireNonNull(Texture.class.getResource(getPath()))).getImage();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
