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

/**
 * The `Pixel` class represents a pixel with a color and an index.
 */
public class Pixel {
    private Color color;
    private Vector2D position;

    /**
     * This function returns the color.
     *
     * @return The method `getColor()` is returning a `Color` object.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This function sets the color of this pixel.
     *
     * @param color The color value to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * The function returns the position of an object as a Vector2D.
     *
     * @return A Vector2D object representing the position.
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * This function sets the position of an object to a given 2D vector.
     *
     * @param position position is a parameter of type Vector2D which represents the new position that we want to set for
     * the pixel.
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }
}
