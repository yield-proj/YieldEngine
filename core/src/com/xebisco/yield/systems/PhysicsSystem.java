/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.systems;

import com.xebisco.yield.*;
import org.jbox2d.dynamics.World;

public class PhysicsSystem extends UpdateSystem implements SystemCreateMethod {

    private Vector2 gravity = new Vector2(0, 10);
    private World box2dWorld;
    private float physicsTime, physicsTimeStep;
    private int velocityIterations = 8, positionIterations = 3;

    @Override
    public void create() {
        box2dWorld = new World(Yld.toVec2(gravity));
        box2dWorld.setContactListener(new PhysicsContactListener());
        updateWorld(1000f / scene.getFrames());
    }

    @Override
    public void update(float delta) {
        updateWorld(delta);
    }

    public void updateWorld(float delta) {
        box2dWorld.step(delta, velocityIterations, positionIterations);
        box2dWorld.clearForces();
    }

    @Override
    public void destroy() {
        //box2dWorld = null;
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
