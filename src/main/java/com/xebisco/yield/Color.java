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

import java.util.Objects;

/**
 * A color stored in an separated RGB values.
 */
public class Color {
    
    private float r, g, b, a;

    public Color(int rgb) {
        r = ((rgb & 0xFF0000) >> 16) / 255f;
        g = ((rgb & 0xFF00) >> 8) / 255f;
        b = (rgb & 0xFF) / 255f;
        a = 1f;
    }

    public Color(int rgb, float alpha) {
        r = ((rgb & 0xFF0000) >> 16) / 255f;
        g = ((rgb & 0xFF00) >> 8) / 255f;
        b = (rgb & 0xFF) / 255f;
        a = Yld.clamp(alpha, 0f, 1f);
    }

    public Color(float r, float g, float b) {
        this.r = Yld.clamp(r, 0, 1);
        this.g = Yld.clamp(g, 0, 1);
        this.b = Yld.clamp(b, 0, 1);
        this.a = 1f;
    }
    public Color(float r, float g, float b, float a) {
        this.r = Yld.clamp(r, 0, 1);
        this.g = Yld.clamp(g, 0, 1);
        this.b = Yld.clamp(b, 0, 1);
        this.a = Yld.clamp(a, 0, 1);
    }

    public Color brighter(float value) {
        return new Color(r + value, g + value, b + value, a);
    }

    public Color darker(float value) {
        return brighter(-value);
    }

    public Color darker() {
        return darker(.2f);
    }

    public Color brighter() {
        return brighter(.2f);
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
        this.r = Yld.clamp(r, 0f, 1f);
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
        this.g = Yld.clamp(g, 0f, 1f);
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
        this.b = Yld.clamp(b, 0f, 1f);
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
        this.a = Yld.clamp(a, 0f, 1f);
    }

    public int getRGB() {
        String hex = String.format("%02X%02X%02X", (int) (r * 255), (int) (g * 255), (int) (b * 255));
        return Integer.parseInt(hex, 16);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Float.compare(color.r, r) == 0 && Float.compare(color.g, g) == 0 && Float.compare(color.b, b) == 0 && Float.compare(color.a, a) == 0;
    }

    public Color get() {
        return new Color(getRGB(), getA());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(r, g, b, a);
    }

    @Override
    public String toString()
    {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
