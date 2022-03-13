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

package com.xebisco.yield;

import com.xebisco.yield.utils.Vector2;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class YldWorld
{
    private Vector2 gravity = new Vector2(0, 10);
    private World world = new World(new Vec2(gravity.x, -gravity.y));

    public World getWorld()
    {
        return world;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public Vector2 getGravity()
    {
        return gravity;
    }

    public void setGravity(Vector2 gravity)
    {
        this.gravity = gravity;
        world.setGravity(new Vec2(gravity.x, -gravity.y));
    }
}
