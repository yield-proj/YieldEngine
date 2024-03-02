package com.xebisco.yield;

import com.xebisco.yield.texture.TextureFilter;

public abstract class AbstractTexture extends FileInput implements AutoCloseable {
    Object imageRef;

    private final TextureFilter filter;


    protected AbstractTexture(String path, TextureFilter filter) {
        super(path);
        this.filter = filter;
    }

    public abstract void close();

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
    public AbstractTexture setImageRef(Object imageRef) {
        this.imageRef = imageRef;
        return this;
    }

    public TextureFilter filter() {
        return filter;
    }
}
