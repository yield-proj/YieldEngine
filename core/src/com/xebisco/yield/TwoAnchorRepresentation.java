package com.xebisco.yield;

public class TwoAnchorRepresentation {
    private double x, y;
    public TwoAnchorRepresentation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void sumLocal(TwoAnchorRepresentation a) {
        x += a.x;
        y += a.y;
    }

    public void subtractLocal(TwoAnchorRepresentation a) {
        x -= a.x;
        y -= a.y;
    }

    public void multiplyLocal(TwoAnchorRepresentation a) {
        x *= a.x;
        y *= a.y;
    }

    public void divideLocal(TwoAnchorRepresentation a) {
        x /= a.x;
        y /= a.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
