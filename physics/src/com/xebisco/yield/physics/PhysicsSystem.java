package com.xebisco.yield.physics;

import com.xebisco.yield.ContextTime;
import com.xebisco.yield.SystemBehavior;
import com.xebisco.yield.Vector2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.io.IOException;

public class PhysicsSystem extends SystemBehavior {
    private final World b2World = new World(new Vec2(0, -10));

    private Vector2D gravity = new Vector2D(0, -10);

    private int velocityIterations = 6, positionIterations = 2;
    private double ppm = 32;

    @Override
    public void onStart() {
        b2World.setContactListener(new WorldContactListener());
    }

    @Override
    public void onUpdate(ContextTime time) {
        super.onUpdate(time);
        b2World.setGravity(Utils.toVec2(gravity.divide(ppm)));
        b2World.step((float) time.deltaTime(), velocityIterations, positionIterations);
    }

    public World b2World() {
        return b2World;
    }

    public Vector2D gravity() {
        return gravity;
    }

    public PhysicsSystem setGravity(Vector2D gravity) {
        this.gravity = gravity;
        return this;
    }

    public int velocityIterations() {
        return velocityIterations;
    }

    public PhysicsSystem setVelocityIterations(int velocityIterations) {
        this.velocityIterations = velocityIterations;
        return this;
    }

    public int positionIterations() {
        return positionIterations;
    }

    public PhysicsSystem setPositionIterations(int positionIterations) {
        this.positionIterations = positionIterations;
        return this;
    }

    public double ppm() {
        return ppm;
    }

    public PhysicsSystem setPpm(double ppm) {
        this.ppm = ppm;
        return this;
    }
}
