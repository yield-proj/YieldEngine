package com.xebisco.yield.physics;

import com.xebisco.yield.Point2D;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

public class EdgeCollider extends Collider {

    private Point2D point1 = new Point2D(-50, 0), point2 = new Point2D(50, 0);

    @Override
    public Shape getShape() {
        EdgeShape s = new EdgeShape();
        s.set(new Vec2((float) point1.getX(), (float) point1.getY()), new Vec2((float) point2.getX(), (float) point2.getY()));
        return s;
    }

    public Point2D getPoint1() {
        return point1;
    }

    public void setPoint1(Point2D point1) {
        this.point1 = point1;
    }

    public Point2D getPoint2() {
        return point2;
    }

    public void setPoint2(Point2D point2) {
        this.point2 = point2;
    }
}
