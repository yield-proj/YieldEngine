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

/**
 * A particle is a graphical object that can be used to enhance the player experience when playing a game.
 */
public class Particle
{
    private Vector2 position, size = new Vector2(100, 100);
    private Texture texture;
    private static Texture defaultTexture = new Texture("/com/xebisco/yield/assets/default-particle.png");

    public Particle()
    {
        this.texture = new Texture(defaultTexture);
    }

    public Particle(Texture texture)
    {
        this.texture = texture;
    }

    public Particle(Texture texture, Vector2 size)
    {
        this.texture = texture;
        this.size = size;
    }

    public Particle(Vector2 size)
    {
        this.size = size;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }

    public Vector2 getSize()
    {
        return size;
    }

    public void setSize(Vector2 size)
    {
        this.size = size;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public static Texture getDefaultTexture()
    {
        return defaultTexture;
    }

    public static void setDefaultTexture(Texture defaultTexture)
    {
        Particle.defaultTexture = defaultTexture;
    }
}
