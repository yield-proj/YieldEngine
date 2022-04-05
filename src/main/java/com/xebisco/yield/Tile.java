/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import java.awt.image.BufferedImage;

public class Tile extends Texture
{
    private int layer;
    public Tile(BufferedImage image, int layer)
    {
        super(image);
        this.layer = layer;
    }

    public Tile(Texture texture, int layer)
    {
        super(texture);
        this.layer = layer;
    }

    public Tile(Texture texture, int x, int y, int width, int height, int layer)
    {
        super(texture, x, y, width, height);
        this.layer = layer;
    }

    public Tile(Texture texture, int x, int y, int width, int height, int imageType, int layer)
    {
        super(texture, x, y, width, height, imageType);
        this.layer = layer;
    }

    public Tile(String relativePath, int layer)
    {
        super(relativePath);
        this.layer = layer;
    }

    public int getLayer()
    {
        return layer;
    }

    public void setLayer(int layer)
    {
        this.layer = layer;
    }
}
