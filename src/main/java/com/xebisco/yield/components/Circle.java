package com.xebisco.yield.components;

public class Circle extends Oval {
    private float radius = 64;
    @Override
    public void update(float delta) {
        super.update(delta);
        setWidth(radius);
        setHeight(radius);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
