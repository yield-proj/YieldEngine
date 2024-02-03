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

package com.xebisco.yield.texture;

import com.xebisco.yield.FileInput;
import com.xebisco.yield.ImmutableVector2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.manager.TextureManager;

import java.io.Closeable;
import java.io.IOException;

/**
 * The Texture class represents an image texture and provides methods for processing and manipulating it.
 */
public class Texture extends FileInput implements Closeable {
    private final ImmutableVector2D size;
    private final TextureManager textureManager;
    private Object imageRef;
    private final TextureFilter filter;

    public Texture(String path, TextureFilter filter, TextureManager textureManager) throws IOException {
        super(path);
        this.textureManager = textureManager;
        Vector2D size = new Vector2D();
        imageRef = textureManager.loadTexture(this, size);
        this.size = new ImmutableVector2D(size.width(), size.height());
        this.filter = filter;
    }

    public Texture(Object imageRef, Vector2D size, String path, TextureFilter filter, TextureManager textureManager) {
        super(path);
        this.textureManager = textureManager;
        this.imageRef = imageRef;
        this.size = new ImmutableVector2D(size.width(), size.height());
        this.filter = filter;
    }

    public static String[] extensions() {
        return new String[] {"png", "jpg", "jpeg"};
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void close() {
        if (textureManager != null)
            textureManager().unloadTexture(this);
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
    public Texture setImageRef(Object imageRef) {
        this.imageRef = imageRef;
        return this;
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

    public TextureFilter filter() {
        return filter;
    }
}
