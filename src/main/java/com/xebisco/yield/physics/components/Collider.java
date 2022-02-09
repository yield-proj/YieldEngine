package com.xebisco.yield.physics.components;

import com.xebisco.yield.Component;
import jdk.jfr.Experimental;

import java.util.Objects;

/**
 * THIS COMPONENT IS NOT FINISHED
 */
@Experimental
public abstract class Collider extends Component {
    private float x1, x2, y1, y2;
    private boolean colliding, inside, trigger;

    public Collider(float x1, float x2, float y1, float y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public void update(float delta) {

    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collider collider = (Collider) o;
        return Float.compare(collider.x1, x1) == 0 && Float.compare(collider.x2, x2) == 0 && Float.compare(collider.y1, y1) == 0 && Float.compare(collider.y2, y2) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, x2, y1, y2);
    }

    public boolean isColliding() {
        return colliding;
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }
}
