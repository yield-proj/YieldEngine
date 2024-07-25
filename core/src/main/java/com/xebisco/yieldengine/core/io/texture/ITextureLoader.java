package com.xebisco.yieldengine.core.io.texture;

import java.io.Serializable;

public interface ITextureLoader {
    Texture loadTexture(String absolutePath, TextureFilter filter);
    void unloadTexture(Serializable imageReference);
    TextureMap loadTextureMap(String absolutePath);
    void unloadTextureMap(Serializable imageReference);
    Texture loadTexture(int x, int y, int width, int height, TextureMap textureMap, TextureFilter filter);
}
