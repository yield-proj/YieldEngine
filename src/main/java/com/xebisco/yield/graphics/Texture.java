package com.xebisco.yield.graphics;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Texture {

    private Image image;
    private Material material;
    private boolean img;

    public Texture(Image image) {
        this.image = image;
        img = true;
    }

    public Texture(String internalPath) {
        this.image = new ImageIcon(Objects.requireNonNull(Texture.class.getResource(internalPath))).getImage();
        img = true;
    }

    public Texture(Material material) {
        this.material = material;
        img = false;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isImg() {
        return img;
    }

    public void setImg(boolean img) {
        this.img = img;
    }
}
