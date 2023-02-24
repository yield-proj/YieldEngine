/*
 * Copyright [2022-2023] [Xebisco]
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

import java.util.Objects;

/**
 * A color stored in a separated RGB values.
 */
public class Color {

    private double red, green, blue, alpha;

    public Color(long rgba) {
        alpha = ((rgba >> 24) & 0xFF) / 255f;
        red = ((rgba >> 16) & 0xFF) / 255f;
        green = ((rgba >> 8) & 0xFF) / 255f;
        blue = ((rgba) & 0xFF) / 255f;
    }

    public Color(Color toCopy) {
        this.red = toCopy.red;
        this.green = toCopy.green;
        this.blue = toCopy.blue;
        this.alpha = toCopy.alpha;
    }

    public Color(int rgb, double alpha) {
        red = ((rgb & 0xFF0000) >> 16) / 255f;
        green = ((rgb & 0xFF00) >> 8) / 255f;
        blue = (rgb & 0xFF) / 255f;
        alpha = Global.clamp(alpha, 0f, 1f);
    }

    public Color(double red, double green, double blue) {
        this.red = Global.clamp(red, 0, 1);
        this.green = Global.clamp(green, 0, 1);
        this.blue = Global.clamp(blue, 0, 1);
        this.alpha = 1f;
    }
    public Color(double red, double green, double blue, double alpha) {
        this.red = Global.clamp(red, 0, 1);
        this.green = Global.clamp(green, 0, 1);
        this.blue = Global.clamp(blue, 0, 1);
        this.alpha = Global.clamp(alpha, 0, 1);
    }

    /**
     * Invert the color by subtracting each component from 1.
     *
     * @return A new Color object with the inverted values of the original Color object.
     */
    public Color invert() {
        return new Color(1 - red, 1 - green, 1 - blue, alpha);
    }

    /**
     * Returns a new color with the same hue and saturation, but with a brighter value.
     *
     * @param value The amount to brighten the color by.
     * @return A new color with the same alpha value but with the red, green, and blue values increased by the value
     * parameter.
     */
    public Color brighter(double value) {
        return new Color(red + value, green + value, blue + value, alpha);
    }

    /**
     * If the color is already black, return black.
     *
     * @param value The amount to brighten or darken the color.
     * @return A new Color object with the same RGB values as the original, but with the brightness adjusted by the value.
     */
    public Color darker(double value) {
        return brighter(-value);
    }

    /**
     * Returns a new Color object that is a darker version of this Color object.
     *
     * @return A new Color object with the same RGB values as the original, but with the brightness reduced by 20%.
     */
    public Color darker() {
        return darker(.2f);
    }

    /**
     * Returns a new Color object that is a lighter version of this Color object.
     *
     * @return A new Color object with the same RGB values as the original, but with the brightness increased by 20%.
     */
    public Color brighter() {
        return brighter(.2);
    }

    /**
     * Getter of the red value of this Color.
     *
     * @return The 'red' variable.
     */
    public double getRed() {
        return red;
    }

    /**
     * Setter of the red value of this Color.
     */
    public void setRed(double red) {
        this.red = Global.clamp(red, 0, 1);
    }

    /**
     * Getter of the green value of this Color.
     *
     * @return The 'green' variable.
     */
    public double getGreen() {
        return green;
    }

    /**
     * Setter of the green value of this Color.
     */
    public void setGreen(double green) {
        this.green = Global.clamp(green, 0, 1);
    }

    /**
     * Getter of the blue value of this Color.
     *
     * @return The 'blue' variable.
     */
    public double getBlue() {
        return blue;
    }

    /**
     * Setter of the blue value of this Color.
     */
    public void setBlue(double blue) {
        this.blue = Global.clamp(blue, 0, 1);
    }

    /**
     * Getter of the alpha value of this Color.
     *
     * @return The 'alpha' variable.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Setter of the alpha value of this Color.
     */
    public void setAlpha(double alpha) {
        this.alpha = Global.clamp(alpha, 0, 1);
    }

    /**
     * It converts the RGB values of a color to a hexadecimal value, then converts that hexadecimal value to an integer
     *
     * @return The RGB value of the color.
     */
    public int getRGB() {
        String hex = String.format("%02X%02X%02X", (int) (red * 255.0), (int) (green * 255.0), (int) (blue * 255.0));
        return Integer.parseInt(hex, 16);
    }

    /**
     * It converts the color to a hexadecimal string, then converts that string to an integer
     *
     * @return The hexadecimal value of the color.
     */
    public int getRGBA() {
        String hex = String.format("%02X%02X%02X%02X", (int) (alpha * 255.0), (int) (red * 255.0), (int) (green * 255.0), (int) (blue * 255.0));
        return Integer.parseUnsignedInt(hex, 16);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Double.compare(color.red, red) == 0 && Double.compare(color.green, green) == 0 && Double.compare(color.blue, blue) == 0 && Double.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(red, green, blue, alpha);
    }

    @Override
    public String toString()
    {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", a=" + alpha +
                '}';
    }
}