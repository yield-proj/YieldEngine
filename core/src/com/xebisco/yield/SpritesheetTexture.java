/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import java.io.InputStream;

public class SpritesheetTexture extends FileInput implements Disposable {

    private final ImmutableSize2D size;
    private final SpritesheetTextureManager spritesheetTextureManager;
    private Object spritesheetImageRef;

    public SpritesheetTexture(String relativePath, SpritesheetTextureManager spritesheetTextureManager) {
        super(relativePath);
        this.spritesheetTextureManager = spritesheetTextureManager;
        spritesheetImageRef = spritesheetTextureManager.loadSpritesheetTexture(this);
        size = new ImmutableSize2D(spritesheetTextureManager.getSpritesheetImageWidth(spritesheetImageRef), spritesheetTextureManager.getSpritesheetImageHeight(spritesheetImageRef));
    }

    public SpritesheetTexture(InputStream inputStream, SpritesheetTextureManager spritesheetTextureManager) {
        super(inputStream);
        this.spritesheetTextureManager = spritesheetTextureManager;
        spritesheetImageRef = spritesheetTextureManager.loadSpritesheetTexture(this);
        size = new ImmutableSize2D(spritesheetTextureManager.getSpritesheetImageWidth(spritesheetImageRef), spritesheetTextureManager.getSpritesheetImageHeight(spritesheetImageRef));
    }

    public SpritesheetTexture(Object spritesheetImageRef, InputStream inputStream, SpritesheetTextureManager spritesheetTextureManager) {
        super(inputStream);
        this.spritesheetTextureManager = spritesheetTextureManager;
        this.spritesheetImageRef = spritesheetImageRef;
        size = new ImmutableSize2D(spritesheetTextureManager.getSpritesheetImageWidth(spritesheetImageRef), spritesheetTextureManager.getSpritesheetImageHeight(spritesheetImageRef));
    }

    /**
     * The function "getTextureFromRegion" takes in a position and size and retrieves a texture from a specified region.
     *
     * @param position The position parameter is a Vector2D object that represents the position of the region from which
     * the texture is to be retrieved. From the top-left point of the image.
     * @param size The size of the region from which to get the texture.
     */
    public Texture getTextureFromRegion(Vector2D position, Size2D size) {
        return spritesheetTextureManager.getTextureFromRegion((int) position.getX(), (int) position.getY(), (int) size.getWidth(), (int) size.getHeight(), this);
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void dispose() {
        getSpritesheetTextureManager().unloadSpritesheetTexture(this);
    }

    /**
     * The function returns the reference to an image object.
     *
     * @return The method is returning the value of the variable `spritesheetImageRef`, which is of type `Object`.
     */
    public Object getSpritesheetImageRef() {
        return spritesheetImageRef;
    }

    /**
     * This function sets the image reference for an object.
     *
     * @param spritesheetImageRef The parameter "spritesheetImageRef" is an object that represents a reference to an image.
     */
    public void setSpritesheetImageRef(Object spritesheetImageRef) {
        this.spritesheetImageRef = spritesheetImageRef;
    }

    /**
     * The function returns an immutable 2D size object.
     *
     * @return The method is returning an object of type `ImmutableSize2D`.
     */
    public ImmutableSize2D getSize() {
        return size;
    }

    public SpritesheetTextureManager getSpritesheetTextureManager() {
        return spritesheetTextureManager;
    }
}
