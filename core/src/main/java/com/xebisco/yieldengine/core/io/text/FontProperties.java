package com.xebisco.yieldengine.core.io.text;

import java.util.Objects;

public class FontProperties implements java.io.Serializable {
    private final String path;
    private final float size;
    private final boolean antiAliasing;

    public FontProperties(String path, float size, boolean antiAliasing) {
        this.path = path;
        this.size = size;
        this.antiAliasing = antiAliasing;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        FontProperties that = (FontProperties) object;
        return Float.compare(size, that.size) == 0 && antiAliasing == that.antiAliasing && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, size, antiAliasing);
    }

    public String getPath() {
        return path;
    }

    public float getSize() {
        return size;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }
}
