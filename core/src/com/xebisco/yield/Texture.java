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
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * The Texture class represents an image texture and provides methods for processing and manipulating it.
 */
public class Texture extends FileInput implements Disposable {

    private final ImmutableSize2D size;
    private final TextureManager textureManager;
    private Object imageRef;

    public Texture(String relativePath, TextureManager textureManager) {
        super(relativePath);
        this.textureManager = textureManager;
        imageRef = textureManager.loadTexture(this);
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
    }

    public Texture(InputStream inputStream, TextureManager textureManager) {
        super(inputStream);
        this.textureManager = textureManager;
        imageRef = textureManager.loadTexture(this);
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
    }

    public Texture(Object imageRef, InputStream inputStream, TextureManager textureManager) {
        super(inputStream);
        this.textureManager = textureManager;
        this.imageRef = imageRef;
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void dispose() {
        getTextureManager().unloadTexture(this);
    }

    /**
     * The function returns the reference to an image object.
     *
     * @return The method is returning the value of the variable `imageRef`, which is of type `Object`.
     */
    public Object getImageRef() {
        return imageRef;
    }

    /**
     * This function sets the image reference for an object.
     *
     * @param imageRef The parameter "imageRef" is an object that represents a reference to an image.
     */
    public void setImageRef(Object imageRef) {
        this.imageRef = imageRef;
    }

    /**
     * The function returns an immutable 2D size object.
     *
     * @return The method is returning an object of type `ImmutableSize2D`.
     */
    public ImmutableSize2D getSize() {
        return size;
    }

    /**
     * The function returns the texture manager object.
     *
     * @return The method is returning an object of type `TextureManager`.
     */
    public TextureManager getTextureManager() {
        return textureManager;
    }

}
