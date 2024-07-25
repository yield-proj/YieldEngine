package com.xebisco.yieldengine.core.io.text;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public final class Font implements Serializable, IDispose {
    private final String name;
    private final float size;
    private final Serializable fontReference;

    public Font(String path, float size, boolean antiAliasing) {
        Font created = IO.getInstance().loadFont(path, size, antiAliasing);
        this.fontReference = created.fontReference;
        this.name = created.name;
        this.size = created.size;
    }

    public Font(String path, float size) {
        this(path, size, true);
    }

    private Font(Serializable fontReference, String name, float size) {
        this.fontReference = fontReference;
        this.name = name;
        this.size = size;
    }

    public static Font create(Serializable fontReference, String name, float size) {
        return new Font(fontReference, name, size);
    }

    @Override
    public void dispose() {
        IO.getInstance().unloadFont(this);
    }

    public String getName() {
        return name;
    }

    public float getSize() {
        return size;
    }

    public Serializable getFontReference() {
        return fontReference;
    }
}
