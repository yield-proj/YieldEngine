package com.xebisco.yieldengine.core.io.texture;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public class TextureMap implements Serializable, IDispose {
    private final Serializable imageReference;
    private final int width, height;

    public TextureMap(String path) {
        TextureMap created = IO.getInstance().loadTextureMap(path);
        this.imageReference = created.imageReference;
        this.width = created.width;
        this.height = created.height;
    }

    private TextureMap(Serializable imageReference, int width, int height) {
        this.imageReference = imageReference;
        this.width = width;
        this.height = height;
    }

    public static TextureMap create(Serializable imageReference, int width, int height) {
        return new TextureMap(imageReference, width, height);
    }

    public Texture getTexture(int x, int y, int width, int height, TextureFilter filter) {
        return IO.getInstance().getTextureLoader().loadTexture(x, y, width, height, this, filter);
    }

    public Texture getTexture(int x, int y, int width, int height) {
        return getTexture(x, y, width, height, TextureFilter.NEAREST);
    }

    @Override
    public void dispose() {
        IO.getInstance().unloadTextureMap(this);
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
