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

public class Pixel {
    private Color color;
    private int index;

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
     * The function returns the value of the variable "index".
     *
     * @return The value of the `index` variable.
     */
    public int getIndex() {
        return index;
    }

    /**
     * This function sets the value of the "index" variable to the input parameter "index".
     *
     * @param index The parameter "index" is an integer value that is used to set the value of the instance variable.
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
