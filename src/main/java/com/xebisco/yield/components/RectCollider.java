package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

import java.util.Objects;

public class RectCollider extends Component {
    private Vector2 rectPosition = new Vector2();
    private int width = 64, height = 64;

    public Vector2 getRectPosition() {
        return rectPosition;
    }

    public void setRectPosition(Vector2 rectPosition) {
        this.rectPosition = rectPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "RectCollider{" +
                "rectPosition=" + rectPosition +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RectCollider collider = (RectCollider) o;
        return width == collider.width && height == collider.height && Objects.equals(rectPosition, collider.rectPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rectPosition, width, height);
    }
}
