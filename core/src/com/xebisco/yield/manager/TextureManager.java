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

package com.xebisco.yield.manager;

import com.xebisco.yield.Vector2D;
import com.xebisco.yield.texture.SpritesheetTexture;
import com.xebisco.yield.texture.Texture;
import com.xebisco.yield.texture.TextureFilter;

import java.io.IOException;

/**
 * The TextureManager interface should be implemented by the specific platform running the application, it contains function for manipulation image files.
 */
public interface TextureManager {
    /**
     * This function loads a texture and returns the specific image reference.
     *
     * @param texture The "texture" parameter in the "loadTexture" method is an object of the "Texture" class.
     */
    Object loadTexture(Texture texture, Vector2D size) throws IOException;

    /**
     * The function unloads a texture.
     *
     * @param texture The parameter "texture" is a variable of type "Texture" that represents a texture object. This
     * function "unloadTexture" is used to free up memory and resources associated with the texture object.
     */
    void unloadTexture(Texture texture);

    Object loadSpritesheetTexture(SpritesheetTexture spritesheetTexture, Vector2D size) throws IOException;

    void unloadSpritesheetTexture(SpritesheetTexture spritesheetTexture);

    Texture getTextureFromRegion(int x, int y, int width, int height, TextureFilter filter, SpritesheetTexture spritesheetTexture);
}
