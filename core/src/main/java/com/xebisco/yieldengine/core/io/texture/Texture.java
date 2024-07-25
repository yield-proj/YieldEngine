package com.xebisco.yieldengine.core.io.texture;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public final class Texture implements Serializable, IDispose {
    private final Serializable imageReference;
    private final int width, height;

    public Texture(String path) {
        Texture created = IO.getInstance().loadTexture(path);
        this.imageReference = created.imageReference;
        this.width = created.width;
        this.height = created.height;
    }

    private Texture(Serializable imageReference, int width, int height) {
        this.imageReference = imageReference;
        this.width = width;
        this.height = height;
    }

    public static Texture create(Serializable imageReference, int width, int height) {
        return new Texture(imageReference, width, height);
    }

    @Override
    public void dispose() {
        IO.getInstance().unloadTexture(this);
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
