/*
 * Copyright [2022] [Xebisco]
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

import com.xebisco.yield.render.VisualUtils;

/**
 * A Texture is an image for a game, can be added to graphical objects to display images.
 */
public class Texture extends RelativeFile
{
    private static int textures;
    private VisualUtils textureUtils;
    private final int textureID;

    public Texture(String relativePath) {
        super(relativePath);
        textures++;
        textureID = textures;
    }

    public static int getTextures() {
        return textures;
    }

    public static void setTextures(int textures) {
        Texture.textures = textures;
    }

    public int getTextureID() {
        return textureID;
    }

    public VisualUtils getTextureUtils() {
        return textureUtils;
    }

    public void setTextureUtils(VisualUtils textureUtils) {
        this.textureUtils = textureUtils;
    }

    public int getWidth() {
        return textureUtils.getTextureWidth(textureID);
    }

    public int getHeight() {
        return textureUtils.getTextureHeight(textureID);
    }
}
