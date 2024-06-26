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

package com.xebisco.yieldengine.texture;

import com.xebisco.yieldengine.FileInput;
import com.xebisco.yieldengine.ImmutableVector2D;
import com.xebisco.yieldengine.Vector2D;
import com.xebisco.yieldengine.manager.FileIOManager;
import com.xebisco.yieldengine.manager.TextureManager;

import java.io.Closeable;
import java.io.IOException;

/**
 * A {@code SpritesheetTexture} represents a texture loaded from a spritesheet.
 * It extends {@link FileInput} and implements {@link Closeable}.
 */
public class SpritesheetTexture extends FileInput implements Closeable {
    private final ImmutableVector2D size;
    private final TextureManager textureManager;
    private Object imageRef;
    private final FileIOManager ioManager;

    /**
     * Constructs a {@link SpritesheetTexture} from a given path and {@link TextureManager}.
     *
     * @param path           The path to the spritesheet file.
     * @param textureManager The {@link TextureManager} responsible for loading and unloading textures.
     * @throws IOException If an error occurs while reading the spritesheet file.
     */
    public SpritesheetTexture(String path, TextureManager textureManager, FileIOManager ioManager) throws IOException {
        super(path);
        this.textureManager = textureManager;
        this.ioManager = ioManager;
        Vector2D size = new Vector2D();
        imageRef = textureManager.loadSpritesheetTexture(this, size, ioManager);
        this.size = new ImmutableVector2D(size.width(), size.height());
    }

    @Override
    public void close() {
        textureManager().unloadSpritesheetTexture(this, ioManager);
    }

    /**
     * Returns the reference to the image object associated with this texture.
     *
     * @return The reference to the image object.
     */
    public Object imageRef() {
        return imageRef;
    }

    /**
     * Sets the image reference for this texture.
     *
     * @param imageRef The reference to the image object.
     * @return This instance of {@link SpritesheetTexture} for method chaining.
     */
    public SpritesheetTexture setImageRef(Object imageRef) {
        this.imageRef = imageRef;
        return this;
    }

    /**
     * Returns the size of the spritesheet texture.
     *
     * @return The size of the spritesheet texture.
     */
    public ImmutableVector2D size() {
        return size;
    }

    /**
     * Returns the {@link TextureManager} responsible for loading and unloading textures.
     *
     * @return The {@link TextureManager}.
     */
    public TextureManager textureManager() {
        return textureManager;
    }

    /**
     * Returns the {@link FileIOManager} responsible for reading and writing files.
     *
     * @return The {@link FileIOManager}.
     */
    public FileIOManager getIoManager() {
        return ioManager;
    }
}
