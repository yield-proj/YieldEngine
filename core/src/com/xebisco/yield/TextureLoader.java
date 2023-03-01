package com.xebisco.yield;

public interface TextureLoader {
    Object loadTexture(Texture texture);
    void unloadTexture(Texture texture);

    /**
     * Calculates the texture width.
     * @param imageRef The image.
     * @return The texture width.
     */
    int getImageWidth(Object imageRef);

    /**
     * Calculates the texture height.
     * @param imageRef The image.
     * @return The texture height.
     */
    int getImageHeight(Object imageRef);
}
