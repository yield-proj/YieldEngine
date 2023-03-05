package com.xebisco.yield.physics;

import com.xebisco.yield.Global;
import com.xebisco.yield.SystemBehavior;
import com.xebisco.yield.Vector2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsSystem extends SystemBehavior {
    private World b2World;
    private Vec2 gravity;
    private int velocityIterations = 10;
    private int positionIterations = 8;

    @Override
    public void onStart() {
        gravity = new Vec2(0, -10);
        b2World = new World(gravity);
    }

    @Override
    public void onUpdate() {
        b2World.step((float) getScene().getApplication().getApplicationManager().getManagerContext().getContextTime().getDeltaTime(), velocityIterations, positionIterations);
    }

    public Vec2 getGravity() {
        return gravity;
    }

    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
    }

    public Vector2D getGravity2D() {
        return Global.toVector2D(gravity);
    }

    public void setGravity2D(Vector2D gravity) {
        this.gravity = Global.toVec2(gravity);
    }

    public World getB2World() {
        return b2World;
    }

    public void setB2World(World b2World) {
        this.b2World = b2World;
    }

    public int getVelocityIterations() {
        return velocityIterations;
    }

    public void setVelocityIterations(int velocityIterations) {
        this.velocityIterations = velocityIterations;
    }

    public int getPositionIterations() {
        return positionIterations;
    }

    public void setPositionIterations(int positionIterations) {
        this.positionIterations = positionIterations;
    }
}
