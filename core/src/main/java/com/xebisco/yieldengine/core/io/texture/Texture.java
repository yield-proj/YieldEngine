package com.xebisco.yieldengine.core.io.texture;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.ILoad;
import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public final class Texture implements Serializable, IDispose, ILoad {
    private transient Serializable imageReference;
    private transient int width, height;
    private final String path;

    public Texture(String path) {
        this.path = path;
    }

    private Texture(Serializable imageReference, int width, int height) {
        this.imageReference = imageReference;
        this.width = width;
        this.height = height;
        this.path = null;
    }

    @Override
    public void load() {
        Texture created = IO.getInstance().loadTexture(path);
        this.imageReference = created.imageReference;
        this.width = created.width;
        this.height = created.height;
    }

    @Override
    public void loadIfNull() {
        if(imageReference == null) {
            load();
        }
    }

    public static Texture create(Serializable imageReference, int width, int height) {
        return new Texture(imageReference, width, height);
    }

    public String getPath() {
        return path;
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
