/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.test;

import com.xebisco.yield.*;
import io.github.synonware.sini4j.Ini;

public class MyGame extends YldGame
{
    @Override
    public void create()
    {
        Particle particle = new Particle(new Texture("/com/xebisco/yield/assets/yieldlogo.png"));
        instantiate((e) ->
        {
            e.addComponent(new ParticleSpawner());
            e.getSelfTransform().goTo(View.getActView().getWidth(), View.getActView().getHeight() / 2f);
            ParticleSpawner spawner = e.getComponent(ParticleSpawner.class);
            spawner.addParticle(particle);
            spawner.setEmitSpeed(new Vector2(-4, -4));
        });
        instantiate((e) ->
        {
            e.addComponent(new ParticleSpawner());
            e.getSelfTransform().goTo(0, View.getActView().getHeight() / 2f);
            ParticleSpawner spawner = e.getComponent(ParticleSpawner.class);
            spawner.addParticle(particle);
            spawner.setEmitSpeed(new Vector2(4, -4));
        });
    }

    public static void main(String[] args)
    {
        launch(new MyGame(), GameConfiguration.iniConfig(new Ini(MyGame.class.getResourceAsStream("/yieldconfig/game.ini"))));
    }
}