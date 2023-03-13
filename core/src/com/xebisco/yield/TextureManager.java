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

public interface TextureManager {
    Object loadTexture(Texture texture);
    void unloadTexture(Texture texture);

    void setPixels(Object imageRef, int[] pixels);

    int[] getPixels(Object imageRef);

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

    Texture cropTexture(Object imageRef, int x, int y, int w, int h);
}
