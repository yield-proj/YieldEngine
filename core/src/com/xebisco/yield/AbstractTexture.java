/*
 * Copyright [2022-2024] [Xebisco]
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

import com.xebisco.yield.texture.TextureFilter;

/**
 * This abstract class represents a texture in the application.
 */
public abstract class AbstractTexture extends FileInput implements AutoCloseable {
    Object imageRef;

    private final TextureFilter filter;

    /**
     * Constructs a new AbstractTexture object with the given path and filter.
     *
     * @param path The path to the texture file.
     * @param filter The filter to apply to the texture.
     */
    protected AbstractTexture(String path, TextureFilter filter) {
        super(path);
        this.filter = filter;
    }

    /**
     * Closes the texture and releases any associated resources.
     */
    public abstract void close();

    /**
     * Returns the reference to the image object.
     *
     * @return The reference to the image object.
     */
    public Object imageRef() {
        return imageRef;
    }

    /**
     * Sets the reference to the image object.
     *
     * @param imageRef The new reference to the image object.
     * @return The current AbstractTexture object for method chaining.
     */
    public AbstractTexture setImageRef(Object imageRef) {
        this.imageRef = imageRef;
        return this;
    }

    /**
     * Returns the filter applied to the texture.
     *
     * @return The filter applied to the texture.
     */
    public TextureFilter filter() {
        return filter;
    }
}
