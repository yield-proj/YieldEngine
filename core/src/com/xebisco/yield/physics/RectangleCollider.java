package com.xebisco.yield.physics;

import com.xebisco.yield.Size2D;
import com.xebisco.yield.editoruse.VisibleOnInspector;
import org.jbox2d.collision.shapes.*;

public class RectangleCollider extends Collider {
    @VisibleOnInspector
    private Size2D size = new Size2D(100, 100);
    @Override
    public Shape getShape() {
        PolygonShape s = new PolygonShape();
        s.setAsBox((float) (size.getWidth() * getTransform().getScale().getX() / getApplication().getPhysicsPpm() / 2.0), (float) (size.getHeight() * getTransform().getScale().getY() / getApplication().getPhysicsPpm() / 2.0));
        return s;
    }

    public Size2D getSize() {
        return size;
    }

    public void setSize(Size2D size) {
        this.size = size;
    }
}
