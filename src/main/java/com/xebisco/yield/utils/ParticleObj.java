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

package com.xebisco.yield.utils;

import com.xebisco.yield.Obj;
import com.xebisco.yield.Particle;
import com.xebisco.yield.Vector2;

public class ParticleObj
{
    private final Particle particle;
    private final Vector2 speed = new Vector2();
    private float life;
    private Obj graphicalObject;

    public ParticleObj(Particle particle)
    {
        this.particle = particle;
    }

    public Particle getParticle()
    {
        return particle;
    }

    public Vector2 getSpeed()
    {
        return speed;
    }

    public Obj getGraphicalObject()
    {
        return graphicalObject;
    }

    public void setGraphicalObject(Obj graphicalObject)
    {
        this.graphicalObject = graphicalObject;
    }

    public float getLife()
    {
        return life;
    }

    public void setLife(float life)
    {
        this.life = life;
    }
}
