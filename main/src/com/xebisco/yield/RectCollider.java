package com.xebisco.yield;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public class RectCollider extends Collider {
    private Vector2 size = new Vector2(32, 32), origin = new Vector2();
    @Override
    public Shape shape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2f, size.y / 2f, Yld.toVec2(getOrigin()), 0);
        return shape;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
}
