package com.xebisco.yield.physics.colliders;

import com.xebisco.yield.physics.PhysicsSystem;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class CircleCollider2D extends Collider2D {
    private double radius;

    @Override
    public Shape createShape(double ppm) {
        CircleShape shape = new CircleShape();
        shape.setRadius((float) (radius / ppm));
        return shape;
    }

    @Override
    public void updateShape(Shape shape, double ppm) {
        shape.setRadius((float) (radius / ppm));
    }

    public double radius() {
        return radius;
    }

    public CircleCollider2D setRadius(double radius) {
        this.radius = radius;
        return this;
    }
}
