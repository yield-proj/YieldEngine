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

package com.xebisco.yield.colliders;

import org.dyn4j.collision.Filter;
import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;

//RECTANGLE, CIRCLE, CAPSULE, ELLIPSE, TRIANGLE

public abstract class Collider
{
    private float density = 1, friction = .3f;
    private boolean sensor;
    public abstract Convex convex();

    public void processFixture(BodyFixture fixture)
    {
        fixture.setDensity(density);
        fixture.setFriction(friction);
        fixture.setSensor(sensor);
        fixture.setFilter(Filter.DEFAULT_FILTER);
    }

    public float getDensity()
    {
        return density;
    }

    public void setDensity(float density)
    {
        this.density = density;
    }

    public float getFriction()
    {
        return friction;
    }

    public void setFriction(float friction)
    {
        this.friction = friction;
    }

    public boolean isSensor()
    {
        return sensor;
    }

    public void setSensor(boolean sensor)
    {
        this.sensor = sensor;
    }
}
