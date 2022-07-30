package com.xebisco.yield.systems;

import com.xebisco.yield.*;
import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.engine.YldEngineAction;
import org.jbox2d.dynamics.World;

public class PhysicsSystem extends SimpleSystem implements SystemCreateMethod {

    private Vector2 gravity = new Vector2(0, 600);
    private World box2dWorld;
    private float physicsTime, physicsTimeStep;
    private int velocityIterations = 8, positionIterations = 3;

    private Engine physicsEngine;

    @Override
    public void create() {
        box2dWorld = new World(Yld.toVec2(gravity), true);
        box2dWorld.setContactListener(new PhysicsContactListener());
        box2dWorld.setAutoClearForces(true);
        physicsEngine = new Engine(null);
        physicsEngine.setStop(EngineStop.INTERRUPT_ON_END);
        physicsEngine.setTargetTime(8);
        physicsEngine.getThread().start();
        physicsEngine.getTodoList().add(new YldEngineAction(() -> {
            updateWorld((physicsEngine.getActual() - physicsEngine.getLast()) / 1000f);
        }, 0, true, Yld.RAND.nextLong()));
        updateWorld(0);
    }

    public void updateWorld(float delta) {
        physicsTimeStep = 1f / getTargetFps();
        physicsTime += delta;
        if(physicsTime >= 0) {
            physicsTime -= physicsTimeStep;
            box2dWorld.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    @Override
    public void destroy() {
        physicsEngine.setRunning(false);
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

    public void setTargetFps(int fps) {
        physicsEngine.setTargetTime(1000 / fps);
    }

    public int getFps() {
        return physicsEngine.getFpsCount();
    }

    public int getTargetFps() {
        return 1000 / physicsEngine.getTargetTime();
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
