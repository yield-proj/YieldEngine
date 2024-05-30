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

package com.xebisco.yieldengine.manager;

import com.xebisco.yieldengine.AbstractTexture;
import com.xebisco.yieldengine.Vector2D;
import com.xebisco.yieldengine.texture.SpritesheetTexture;
import com.xebisco.yieldengine.texture.Texture;
import com.xebisco.yieldengine.texture.TextureFilter;

import java.io.IOException;

/**
 * The {@code TextureManager} interface should be implemented by the specific platform running the application, it contains function for manipulation image files.
 */
public interface TextureManager {
    Object loadTexture(AbstractTexture texture, Vector2D size, FileIOManager ioManager) throws IOException;

    void unloadTexture(AbstractTexture texture, FileIOManager ioManager);

    Object loadSpritesheetTexture(SpritesheetTexture spritesheetTexture, Vector2D size, FileIOManager ioManager) throws IOException;

    void unloadSpritesheetTexture(SpritesheetTexture spritesheetTexture, FileIOManager ioManager);

    Texture getTextureFromRegion(int x, int y, int width, int height, TextureFilter filter, SpritesheetTexture spritesheetTexture);
}
