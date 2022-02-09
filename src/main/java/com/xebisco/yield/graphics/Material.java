package com.xebisco.yield.graphics;

import java.awt.*;

public class Material {

    private Color color = Color.RED;
    private Texture texture;
    private boolean lined;
    private Shape shape = Shape.RECTANGLE;

    public enum Shape {
        RECTANGLE, OVAL
    }

    public Material() {
    }

    public Material(Texture texture) {
        this.texture = texture;
    }

    public Material(Shape shape) {
        this.shape = shape;
    }

    public Material(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
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

    public boolean isLined() {
        return lined;
    }

    public void setLined(boolean lined) {
        this.lined = lined;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
