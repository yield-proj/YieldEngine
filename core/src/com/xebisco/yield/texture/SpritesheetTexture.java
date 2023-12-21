package com.xebisco.yield.texture;

import com.xebisco.yield.FileInput;
import com.xebisco.yield.ImmutableVector2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.manager.TextureManager;

import java.io.Closeable;
import java.io.IOException;

public class SpritesheetTexture extends FileInput implements Closeable {
    private final ImmutableVector2D size;
    private final TextureManager textureManager;
    private Object imageRef;

    public SpritesheetTexture(String path, TextureManager textureManager) throws IOException {
        super(path);
        this.textureManager = textureManager;
        Vector2D size = new Vector2D();
        imageRef = textureManager.loadSpritesheetTexture(this, size);
        this.size = new ImmutableVector2D(size.width(), size.height());
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void close() {
        textureManager().unloadSpritesheetTexture(this);
    }

    /**
     * The function returns the reference to an image object.
     *
     * @return The method is returning the value of the variable `imageRef`, which is of type `Object`.
     */
    public Object imageRef() {
        return imageRef;
    }

    /**
     * This function sets the image reference for an object.
     *
     * @param imageRef The parameter "imageRef" is an object that represents a reference to an image.
     */
    public SpritesheetTexture setImageRef(Object imageRef) {
        this.imageRef = imageRef;return this;
    }

    /**
     * The function returns an immutable 2D size object.
     *
     * @return The method is returning an object of type `ImmutableSize2D`.
     */
    public ImmutableVector2D size() {
        return size;
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
