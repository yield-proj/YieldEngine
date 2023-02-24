package com.xebisco.yield;

public class Size2D extends TwoAnchorRepresentation {

    public Size2D(double width, double height) {
        super(width, height);
    }

    public double getWidth() {
        return getX();
    }

    public double getHeight() {
        return getX();
    }
}
