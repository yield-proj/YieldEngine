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

package com.xebisco.yield.components;

import com.xebisco.yield.Particle;
import com.xebisco.yield.Texture;

import java.util.ArrayList;

public class ParticleSpawner extends ParticleSystem
{
    private float emitTime = 2, counter;
    private boolean repeat = true;
    private ArrayList<Particle> particles = new ArrayList<>();

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (!repeat && isEmitted())
            return;
        counter += delta;
        if (counter >= emitTime)
        {
            counter = 0;
            getToEmit().addAll(particles);
            emit();
        }
    }

    public void addParticle(Particle particle)
    {
        particles.add(particle);
    }

    public void addParticleFromTexture(Texture texture)
    {
        if(texture != null)
            particles.add(new Particle(texture));
        else
            particles.add(new Particle());
    }

    public float getEmitTime()
    {
        return emitTime;
    }

    public void setEmitTime(float emitTime)
    {
        this.emitTime = emitTime;
    }

    public float getCounter()
    {
        return counter;
    }

    public void setCounter(float counter)
    {
        this.counter = counter;
    }

    public boolean isRepeat()
    {
        return repeat;
    }

    public void setRepeat(boolean repeat)
    {
        this.repeat = repeat;
    }

    public ArrayList<Particle> getParticles()
    {
        return particles;
    }

    public void setParticles(ArrayList<Particle> particles)
    {
        this.particles = particles;
    }
}
