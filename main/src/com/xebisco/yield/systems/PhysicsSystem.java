package com.xebisco.yield.systems;

import com.xebisco.yield.*;
import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.engine.YldEngineAction;
import org.jbox2d.dynamics.World;

public class PhysicsSystem extends UpdateSystem implements SystemCreateMethod {

    private Vector2 gravity = new Vector2(0, 10);
    private World box2dWorld;
    private float physicsTime, physicsTimeStep;
    private int velocityIterations = 8, positionIterations = 3;

    @Override
    public void create() {
        box2dWorld = new World(Yld.toVec2(gravity), true);
        box2dWorld.setContactListener(new PhysicsContactListener());
        updateWorld(0);
    }

    @Override
    public void update(float delta) {
        updateWorld(delta);
    }

    public void updateWorld(float delta) {
        physicsTimeStep = 1f / scene.getTime().getTargetFPS();
        physicsTime += delta;
        if(physicsTime >= 0) {
            physicsTime -= physicsTimeStep;
            try {
                box2dWorld.step(physicsTimeStep, velocityIterations, positionIterations);
               // box2dWorld.clearForces();
            } catch (NullPointerException ignore) {

            }
        }
    }

    @Override
    public void destroy() {
        box2dWorld = null;
    }

    public Vector2 getGravity() {
        return gravity;
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
        if(box2dWorld != null) {
            box2dWorld.setGravity(Yld.toVec2(gravity));
        }
    }

    public World getBox2dWorld() {
        return box2dWorld;
    }

    public void setBox2dWorld(World box2dWorld) {
        this.box2dWorld = box2dWorld;
    }

    public float getPhysicsTime() {
        return physicsTime;
    }

    public void setPhysicsTime(float physicsTime) {
        this.physicsTime = physicsTime;
    }

    public float getPhysicsTimeStep() {
        return physicsTimeStep;
    }

    public void setPhysicsTimeStep(float physicsTimeStep) {
        this.physicsTimeStep = physicsTimeStep;
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
