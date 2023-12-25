package com.xebisco.yield.physics.colliders;

import com.xebisco.yield.ComponentBehavior;
import org.jbox2d.collision.shapes.Shape;

public abstract class Collider2D extends ComponentBehavior {
    private double density = 1, restitution = 0, friction = 0.2;
    private int collisionMask = 0;
    private int[] excludedMasks = new int[0];


    public abstract Shape createShape(double ppm);
    public abstract void updateShape(Shape shape, double ppm);

    public double density() {
        return density;
    }

    public Collider2D setDensity(double density) {
        this.density = density;
        return this;
    }

    public double restitution() {
        return restitution;
    }

    public Collider2D setRestitution(double restitution) {
        this.restitution = restitution;
        return this;
    }

    public double friction() {
        return friction;
    }

    public Collider2D setFriction(double friction) {
        this.friction = friction;
        return this;
    }

    public int collisionMask() {
        return collisionMask;
    }

    public Collider2D setCollisionMask(int collisionMask) {
        this.collisionMask = collisionMask;
        return this;
    }

    public int[] excludedMasks() {
        return excludedMasks;
    }

    public Collider2D setExcludedMasks(int[] excludedMasks) {
        this.excludedMasks = excludedMasks;
        return this;
    }
}
