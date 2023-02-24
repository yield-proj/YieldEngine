package com.xebisco.yield;

public class Transform2D {
    private final Point2D position = new Point2D();
    private double zRotation;

    public Point2D getPosition() {
        return position;
    }

    public double getzRotation() {
        return zRotation;
    }

    public void setzRotation(double zRotation) {
        this.zRotation = zRotation;
    }
}
