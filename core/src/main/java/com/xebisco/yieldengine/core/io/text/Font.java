package com.xebisco.yieldengine.core.io.text;

import java.io.Serializable;

public final class Font implements Serializable {
    private final String name;
    private final float size;
    private final Serializable fontReference;

    public Font(Serializable fontReference, String name, float size) {
        this.fontReference = fontReference;
        this.name = name;
        this.size = size;
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
