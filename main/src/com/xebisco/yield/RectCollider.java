package com.xebisco.yield;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * It's a rectangle collider
 */
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

    /**
     * This function returns the size of the object.
     *
     * @return The size of the object.
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * This function sets the size of the object to the size passed in.
     *
     * @param size The size of the object.
     */
    public void setSize(Vector2 size) {
        this.size = size;
    }
}
