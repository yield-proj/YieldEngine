package com.xebisco.yield.graphics;

import java.awt.*;

public class Material {

    private Color color = Color.RED;

    public Material() {
    }

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
