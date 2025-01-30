package com.xebisco.yieldengine.core.io.text;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.ILoad;
import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public final class Font implements Serializable, IDispose, ILoad {
    private transient Serializable fontReference;
    private final FontProperties properties;

    public Font(FontProperties properties) {
        this.properties = properties;
    }

    private Font(Serializable fontReference, float size) {
        this.fontReference = fontReference;
        properties = new FontProperties(null, size, false);
    }

    @Override
    public void load() {
        Font created = IO.getInstance().loadFont(properties.getPath(), properties.getSize(), properties.isAntiAliasing());
        this.fontReference = created.fontReference;
    }

    @Override
    public void loadIfNull() {
        if(fontReference == null) {
            load();
        }
    }

    public static Font create(Serializable fontReference, float size) {
        return new Font(fontReference, size);
    }

    @Override
    public void dispose() {
        IO.getInstance().unloadFont(this);
    }

    public FontProperties getProperties() {
        return properties;
    }

    public Serializable getFontReference() {
        return fontReference;
    }
}
