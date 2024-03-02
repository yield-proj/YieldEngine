package com.xebisco.yield;

import com.xebisco.yield.manager.TextureManager;
import com.xebisco.yield.texture.TextureFilter;

import java.io.IOException;

public class OnDemandTexture extends AbstractTexture {
    private final TextureManager textureManager;

    public OnDemandTexture(String path, TextureFilter filter, TextureManager textureManager) {
        super(path, filter);
        this.textureManager = textureManager;
    }

    public static String[] extensions() {
        return new String[]{"png", "jpg", "jpeg"};
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void close() {
        if (textureManager != null && imageRef != null)
            textureManager().unloadTexture(this);
    }

    @Override
    public Object imageRef() {
        if(imageRef == null) {
            try {
                setImageRef(textureManager.loadTexture(this, new Vector2D()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.imageRef();
    }

    /**
     * The function returns the texture manager object.
     *
     * @return The method is returning an object of type `TextureManager`.
     */
    public TextureManager textureManager() {
        return textureManager;
    }
}
