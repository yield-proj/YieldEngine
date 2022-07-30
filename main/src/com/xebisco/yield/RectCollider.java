package com.xebisco.yield;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public class RectCollider extends Collider {
    private Vector2 size = new Vector2(32, 32);

    public RectCollider(Vector2 size) {
        this.size = size;
    }

    public RectCollider() {
    }

    @Override
    public Shape shape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / scene.getPpm() / 2f, size.y / scene.getPpm() / 2f, Yld.toVec2(getOffset().div(scene.getPpm())), 0);
        return shape;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
}
