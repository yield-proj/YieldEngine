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

/**
 * It's a 2D array of pixels that wraps around the edges
 */
public class PixelGrid {
    private final Pixel[][] pixels;

    public PixelGrid(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    /**
     * If the x or y value is outside the bounds of the array, then subtract or add the length of the array until it is
     * within the bounds.
     *
     * @param x The x coordinate of the pixel you want to get.
     * @param y The y coordinate of the pixel
     * @return A pixel from the array of pixels.
     */
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

    /**
     * Given a pixel index, return the pixel at that index.
     *
     * @param px The x coordinate of the pixel in the image.
     * @param py The y coordinate of the pixel in the image
     * @param x The x coordinate of the pixel to get.
     * @param y The y coordinate of the pixel to get.
     * @return A pixel from the image.
     */
    public Pixel pixelFromIndex(int px, int py, int x, int y) {
        return pixelFromIndex(px + x, py + y);
    }

    /**
     * Given a pixel, return the pixel at the given x and y offset from that pixel.
     *
     * @param pixel The pixel to start from
     * @param x The x index of the pixel
     * @param y The y index of the pixel
     * @return A pixel object.
     */
    public Pixel pixelFromIndex(Pixel pixel, int x, int y) {
        return pixelFromIndex(pixel.getIndexX(), pixel.getIndexY(), x, y);
    }



    /**
     * This function returns the pixels array.
     *
     * @return The pixels array.
     */
    public Pixel[][] getPixels() {
        return pixels;
    }
}
