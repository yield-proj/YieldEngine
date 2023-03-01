package com.xebisco.yield;

import java.io.InputStream;

public class Font extends FileInput {

    private Object fontRef;
    private final double size;

    public Font(String relativePath, double size, FontLoader fontLoader) {
        super(relativePath);
        this.size = size;
        fontRef = fontLoader.loadFont(this);
    }

    public Font(InputStream inputStream, double size, FontLoader fontLoader) {
        super(inputStream);
        this.size = size;
        fontRef = fontLoader.loadFont(this);
    }

    public Object getFontRef() {
        return fontRef;
    }

    public void setFontRef(Object fontRef) {
        this.fontRef = fontRef;
    }

    public double getSize() {
        return size;
    }
}
