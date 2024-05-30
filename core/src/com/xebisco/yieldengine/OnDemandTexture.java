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

package com.xebisco.yieldengine;

import com.xebisco.yieldengine.manager.FileIOManager;
import com.xebisco.yieldengine.manager.TextureManager;
import com.xebisco.yieldengine.texture.TextureFilter;

import java.io.IOException;

/**
 * The {@code OnDemandTexture} class extends {@link  AbstractTexture} and represents a texture that is loaded on demand.
 * It has a reference to a {@link ReturnTextureManager} which is used to load and unload the texture.
 */
public class OnDemandTexture extends AbstractTexture {
    private final ReturnTextureManager textureManager;
    private final ReturnIOManager ioManager;

    /**
     * The {@code ReturnTextureManager} interface provides a method to get the {@link TextureManager}.
     * It is used to get the {@link TextureManager} instance that is responsible for loading and unloading textures.
     */
    public interface ReturnTextureManager {
        /**
         * Returns the {@link TextureManager} instance.
         *
         * @return the {@link TextureManager} instance.
         */
        TextureManager textureManager();
    }

    /**
     * The {@code ReturnIOManager} interface provides a method to get the {@link FileIOManager}.
     * It is used to get the {@link FileIOManager} instance that is responsible for loading and unloading files.
     */
    public interface ReturnIOManager {
        /**
         * Returns the {@link FileIOManager} instance.
         *
         * @return the {@link FileIOManager} instance.
         */
        FileIOManager ioManager();
    }

    /**
     * Constructs a new {@code OnDemandTexture} instance with the specified path, filter, and {@link ReturnTextureManager}.
     *
     * @param path           the path of the texture file.
     * @param filter         the filter to be applied to the texture.
     * @param textureManager the {@link ReturnTextureManager} instance.
     */
    public OnDemandTexture(String path, TextureFilter filter, ReturnTextureManager textureManager, ReturnIOManager ioManager) {
        super(path, filter);
        this.textureManager = textureManager;
        this.ioManager = ioManager;
    }

    /**
     * Returns an array of string extensions that are supported by the {@link OnDemandTexture} class.
     *
     * @return an array of string extensions.
     */
    public static String[] extensions() {
        return new String[]{"png", "jpg", "jpeg"};
    }

    @Override
    public void close() {
        if (textureManager != null && imageRef != null)
            textureManager().textureManager().unloadTexture(this, ioManager.ioManager());
    }

    @Override
    public Object imageRef() {
        if (imageRef == null) {
            try {
                setImageRef(textureManager.textureManager().loadTexture(this, new Vector2D(), ioManager.ioManager()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.imageRef();
    }

    /**
     * Returns the {@link ReturnTextureManager} instance.
     *
     * @return the {@link ReturnTextureManager} instance.
     */
    public ReturnTextureManager textureManager() {
        return textureManager;
    }

    /**
     * Returns the {@link ReturnIOManager} instance.
     *
     * @return the {@link ReturnIOManager} instance.
     */
    public ReturnIOManager getIoManager() {
        return ioManager;
    }
}
