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
 * It's a class that represents a pixel
 */
public class Pixel
{
    private String cache;
    private Vector2 outLocation, location;
    private Color outColor, color;

    private final int indexX, indexY;

    private final PixelGrid pixelGrid;

    public Pixel(int indexX, int indexY, PixelGrid pixelGrid) {
        this.pixelGrid = pixelGrid;
        this.indexX = indexX;
        this.indexY = indexY;
    }

    /**
     * Getter of the location variable.
     * @return The location variable.
     */
    public Vector2 getLocation()
    {
        return location;
    }

    /**
     * Setter of the location variable.
     */
    public void setLocation(Vector2 location)
    {
        this.location = location;
    }

    /**
     * Getter of the outColor variable.
     * @return The outColor variable.
     */
    public Color getOutColor()
    {
        return outColor;
    }

    /**
     * Setter of the outColor variable.
     */
    public void setOutColor(Color outColor)
    {
        this.outColor = outColor;
    }

    /**
     * This function returns the cache
     *
     * @return The cache variable is being returned.
     */
    public String getCache() {
        return cache;
    }

    /**
     * This function sets the cache variable to the value of the cache parameter.
     *
     * @param cache The cache value to set.
     */
    public void setCache(String cache) {
        this.cache = cache;
    }

    /**
     * This function returns the color of the object.
     *
     * @return The color of the car.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This function sets the color of the object to the color passed in as a parameter.
     *
     * @param color The color of the text.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * This function returns the location of the outLocation variable
     *
     * @return The outLocation variable is being returned.
     */
    public Vector2 getOutLocation() {
        return outLocation;
    }

    /**
     * This function sets the outLocation variable to the value of the outLocation parameter
     *
     * @param outLocation The location of the output.
     */
    public void setOutLocation(Vector2 outLocation) {
        this.outLocation = outLocation;
    }

    /**
     * This function returns the indexX variable.
     *
     * @return The indexX variable is being returned.
     */
    public int getIndexX() {
        return indexX;
    }

    /**
     * This function returns the indexY variable.
     *
     * @return The indexY variable is being returned.
     */
    public int getIndexY() {
        return indexY;
    }

    /**
     * This function returns the pixel grid.
     *
     * @return The pixelGrid object.
     */
    public PixelGrid getPixelGrid() {
        return pixelGrid;
    }
}
