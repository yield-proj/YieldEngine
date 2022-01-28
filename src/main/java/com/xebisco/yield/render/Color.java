package com.xebisco.yield.render;

public class Color {
    private final float red, green, blue, alpha;

    public Color(float red, float green, float blue) {
        if (red < 0 || red > 1 || green < 0 || green > 1 || blue < 0 || blue > 1)
            throw new IllegalArgumentException("an argument its greater than 1 or lower than 0");
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1f;
    }

    public Color(float red, float green, float blue, float alpha) {
        if (red < 0 || red > 1 || green < 0 || green > 1 || blue < 0 || blue > 1 || alpha < 0 || alpha > 1)
            throw new IllegalArgumentException("an argument its greater than 1 or lower than 0");
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }
}
