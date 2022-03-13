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

import com.xebisco.yield.UpdateSystem;
import org.dyn4j.dynamics.Body;
import org.dyn4j.world.World;

public class PhysicsSystem extends UpdateSystem
{
    private World<Body> world = new World<>();

    public PhysicsSystem()
    {
       // world.setGravity();
    }

    @Override
    public void update(float delta)
    {
        world.update(delta);
    }

    public World<Body> getWorld()
    {
        return world;
    }

    public void setWorld(World<Body> world)
    {
        this.world = world;
    }
}
