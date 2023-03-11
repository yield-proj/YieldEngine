package com.xebisco.yield.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class CircleCollider extends Collider {
    private double radius = 50;
    @Override
    public Shape getShape() {
        CircleShape s = new CircleShape();
        s.setRadius((float) radius);
        return s;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
