package com.xebisco.yield;

import java.io.InputStream;

public interface FontLoader {
    Object loadFont(Font font);
    void unloadFont(Font font);
}
