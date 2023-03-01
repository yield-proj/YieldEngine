package com.xebisco.yield;

import java.io.InputStream;

public class Font extends FileInput {

    private final Object fontRef;

    public Font(String relativePath, FontLoader fontLoader) {
        super(relativePath);
        fontRef = fontLoader.loadFont(this);
    }

    public Font(InputStream inputStream, FontLoader fontLoader) {
        super(inputStream);
        fontRef = fontLoader.loadFont(this);
    }

    public Object getFontRef() {
        return fontRef;
    }
}
