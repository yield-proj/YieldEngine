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

public class PixelGrid {
    private final Pixel[][] pixels;

    public PixelGrid(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Pixel pixelFromIndex(int x, int y) {
        while (x > pixels.length - 1)
            x -= pixels.length;
        while (x < 0)
            x += pixels.length;
        while (y > pixels[0].length - 1)
            y -= pixels[0].length;
        while (y < 0)
            y += pixels[0].length;
        return pixels[x][y];
    }

    public Pixel pixelFromIndex(int px, int py, int x, int y) {
        return pixelFromIndex(px + x, py + y);
    }

    public Pixel pixelFromIndex(Pixel pixel, int x, int y) {
        return pixelFromIndex(pixel.getIndexX(), pixel.getIndexY(), x, y);
    }



    public Pixel[][] getPixels() {
        return pixels;
    }
}
