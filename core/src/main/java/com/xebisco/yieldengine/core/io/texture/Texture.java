package com.xebisco.yieldengine.core.io.texture;

import java.io.Serializable;

public final class Texture implements Serializable {
    private final Serializable imageReference;
    private final int width, height;

    public Texture(Serializable imageReference, int width, int height) {
        this.imageReference = imageReference;
        this.width = width;
        this.height = height;
    }

    public Serializable getImageReference() {
        return imageReference;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
