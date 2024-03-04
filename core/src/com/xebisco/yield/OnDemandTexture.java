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

import com.xebisco.yield.manager.TextureManager;
import com.xebisco.yield.texture.TextureFilter;

import java.io.IOException;

public class OnDemandTexture extends AbstractTexture {
    private final ReturnTextureManager textureManager;

    public interface ReturnTextureManager {
        TextureManager textureManager();
    }

    public OnDemandTexture(String path, TextureFilter filter, ReturnTextureManager textureManager) {
        super(path, filter);
        this.textureManager = textureManager;
    }

    public static String[] extensions() {
        return new String[]{"png", "jpg", "jpeg"};
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void close() {
        if (textureManager != null && imageRef != null)
            textureManager().textureManager().unloadTexture(this);
    }

    @Override
    public Object imageRef() {
        if(imageRef == null) {
            try {
                setImageRef(textureManager.textureManager().loadTexture(this, new Vector2D()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.imageRef();
    }

    public ReturnTextureManager textureManager() {
        return textureManager;
    }
}
