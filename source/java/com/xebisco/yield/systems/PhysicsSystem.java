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

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.UpdateSystem;
import com.xebisco.yield.YldWorld;
import com.xebisco.yield.components.PhysicsBody;

public class PhysicsSystem extends UpdateSystem
{
    private int velocityIterations = 6, positionIterations = 2;
    private YldWorld world = new YldWorld();

    @Override
    public void update(float delta)
    {
        world.getBox2dWorld().step(1f / scene.getTime().getTargetFPS(), velocityIterations, positionIterations);
        
    }

    public int getVelocityIterations()
    {
        return velocityIterations;
    }

    public void setVelocityIterations(int velocityIterations)
    {
        this.velocityIterations = velocityIterations;
    }

    public int getPositionIterations()
    {
        return positionIterations;
    }

    public void setPositionIterations(int positionIterations)
    {
        this.positionIterations = positionIterations;
    }

    public YldWorld getWorld()
    {
        return world;
    }

    public void setWorld(YldWorld world)
    {
        this.world = world;
    }
}
