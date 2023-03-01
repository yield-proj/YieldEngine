package com.xebisco.yield;

public class ImmutableSize2D extends Size2D {
    public ImmutableSize2D(double width, double height) {
        super(width, height);
    }

    @Override
    public void setX(double x) {
        throw new ImmutableBreakException();
    }

    @Override
    public void setY(double y) {
        throw new ImmutableBreakException();
    }
}
