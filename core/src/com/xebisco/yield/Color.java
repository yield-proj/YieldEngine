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

import java.util.Objects;

/**
 * A color stored in a separated RGB values.
 */
public class Color implements Cloneable {

    private double red, green, blue, alpha;

    public Color(long color) {
        this(color, Format.ARGB);
    }

    public Color(long color, Format format) {
        switch (format) {
            case RGB:
                red = ((color >> 16) & 0xFF) / 255f;
                green = ((color >> 8) & 0xFF) / 255f;
                blue = ((color) & 0xFF) / 255f;
                alpha = 1;
                break;
            case RGBA:
                red = ((color >> 24) & 0xFF) / 255f;
                green = ((color >> 16) & 0xFF) / 255f;
                blue = ((color >> 8) & 0xFF) / 255f;
                alpha = ((color) & 0xFF) / 255f;
                break;
            case ARGB:
                alpha = ((color >> 24) & 0xFF) / 255f;
                red = ((color >> 16) & 0xFF) / 255f;
                green = ((color >> 8) & 0xFF) / 255f;
                blue = ((color) & 0xFF) / 255f;
                break;
        }

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
    public double red() {
        return red;
    }

    /**
     * Setter of the red value of this Color.
     */
    public Color setRed(double red) {
        this.red = Global.clamp(red, 0, 1);
        return this;
    }

    /**
     * Getter of the green value of this Color.
     *
     * @return The 'green' variable.
     */
    public double green() {
        return green;
    }

    /**
     * Setter of the green value of this Color.
     */
    public Color setGreen(double green) {
        this.green = Global.clamp(green, 0, 1);
        return this;
    }

    /**
     * Getter of the blue value of this Color.
     *
     * @return The 'blue' variable.
     */
    public double blue() {
        return blue;
    }

    /**
     * Setter of the blue value of this Color.
     */
    public Color setBlue(double blue) {
        this.blue = Global.clamp(blue, 0, 1);
        return this;
    }

    /**
     * Getter of the alpha value of this Color.
     *
     * @return The 'alpha' variable.
     */
    public double alpha() {
        return alpha;
    }

    /**
     * Setter of the alpha value of this Color.
     */
    public Color setAlpha(double alpha) {
        this.alpha = Global.clamp(alpha, 0, 1);
        return this;
    }

    /**
     * The function returns an integer representation of the RGB color values by converting the red, green, and blue values
     * to their corresponding 8-bit values and combining them.
     *
     * @return The method is returning an integer value that represents the RGB color of the object. The RGB value is
     * calculated by multiplying the red, green, and blue values by 255 and then shifting them to the appropriate positions
     * in the integer value.
     */
    public int rgb() {
        int rgb = (int) (red * 255.0);
        rgb = (rgb << 8) + (int) (green * 255.0);
        rgb = (rgb << 8) + (int) (blue * 255.0);
        return rgb;
    }

    /**
     * The function returns an integer value representing the ARGB color code based on the alpha, red, green, and blue
     * values.
     *
     * @return The method is returning an integer value that represents the ARGB (Alpha, Red, Green, Blue) color code of
     * the color object.
     */
    public int argb() {
        return colorValue(alpha, red, green, blue);
    }

    private static int colorValue(double alpha, double red, double green, double blue) {
        return ((int) (alpha * 255)) << 24 + ((int) (red * 255)) << 16 + ((int) (green * 255)) << 8 + ((int) (blue * 255));
    }

    /**
     * The function returns an integer value representing the RGBA color code based on the given red, green, blue, and
     * alpha values.
     *
     * @return The method is returning an integer value that represents the RGBA color value of the object. The value is
     * obtained by shifting the red, green, blue, and alpha values to their respective positions in the 8-bit integer and
     * then combining them using bitwise operations.
     */
    public int rgba() {
        return colorValue(red, green, blue, alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Double.compare(color.red, red) == 0 && Double.compare(color.green, green) == 0 && Double.compare(color.blue, blue) == 0 && Double.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", a=" + alpha +
                '}';
    }

    @Override
    public Color clone() {
        try {
            return (Color) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * This enumeration is used to specify the
     * format of the color value when creating a new `Color` object.
     */
    public enum Format {
        RGB, RGBA, ARGB
    }
}