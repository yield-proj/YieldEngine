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

/**
 * A color stored in an separated RGB values.
 */
public class Color {
    private float r, g, b, a;
    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1f;
    }
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Getter of the red value of this Color.
     *
     * @return The 'r' variable.
     */
    public float getR() {
        return r;
    }

    /**
     * Setter of the red value of this Color.
     */
    public void setR(float r) {
        this.r = r;
    }

    /**
     * Getter of the green value of this Color.
     *
     * @return The 'g' variable.
     */
    public float getG() {
        return g;
    }

    /**
     * Setter of the green value of this Color.
     */
    public void setG(float g) {
        this.g = g;
    }

    /**
     * Getter of the blue value of this Color.
     *
     * @return The 'b' variable.
     */
    public float getB() {
        return b;
    }

    /**
     * Setter of the blue value of this Color.
     */
    public void setB(float b) {
        this.b = b;
    }

    /**
     * Getter of the alpha value of this Color.
     *
     * @return The 'a' variable.
     */
    public float getA() {
        return a;
    }

    /**
     * Setter of the alpha value of this Color.
     */
    public void setA(float a) {
        this.a = a;
    }
}
