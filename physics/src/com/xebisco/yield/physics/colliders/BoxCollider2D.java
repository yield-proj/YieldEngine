package com.xebisco.yield.physics.colliders;

import com.xebisco.yield.Vector2D;
import com.xebisco.yield.physics.PhysicsSystem;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public class BoxCollider2D extends Collider2D {

    private final Vector2D size = new Vector2D(100, 100);

    @Override
    public Shape createShape(double ppm) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) (size.width() / ppm), (float) (size.height() / ppm));
        return shape;
    }

    @Override
    public void updateShape(Shape shape, double ppm) {
        ((PolygonShape) shape).setAsBox((float) (size.width() / ppm), (float) (size.height() / ppm));
    }
}
