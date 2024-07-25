package com.xebisco.yieldengine.core.io.texture;

public interface ITextureLoader {
    Texture loadTexture(String absolutePath, TextureFilter filter);
    void unloadTexture(Object imageReference);
}
