package com.xebisco.yield;

import java.io.InputStream;

public class Texture extends FileInput {

    private Object imageRef;
    private final ImmutableSize2D size;

    public Texture(String relativePath, TextureLoader textureLoader) {
        super(relativePath);
        imageRef = textureLoader.loadTexture(this);
        size = new ImmutableSize2D(textureLoader.getImageWidth(this), textureLoader.getImageHeight(this));
    }

    public Texture(InputStream inputStream, TextureLoader textureLoader) {
        super(inputStream);
        imageRef = textureLoader.loadTexture(this);
        size = new ImmutableSize2D(textureLoader.getImageWidth(this), textureLoader.getImageHeight(this));
    }

    public Object getImageRef() {
        return imageRef;
    }

    public void setImageRef(Object imageRef) {
        this.imageRef = imageRef;
    }

    public ImmutableSize2D getSize() {
        return size;
    }
}
