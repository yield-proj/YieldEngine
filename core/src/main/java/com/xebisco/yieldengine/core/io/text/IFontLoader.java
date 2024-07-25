package com.xebisco.yieldengine.core.io.text;

public interface IFontLoader {
    Font loadFont(String absolutePath, float size, boolean antiAliasing);
    void unloadFont(Object fontReference);
}
