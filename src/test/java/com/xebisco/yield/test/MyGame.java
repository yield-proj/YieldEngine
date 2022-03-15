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
import com.xebisco.yield.components.Sprite;

public class MyGame extends YldGame
{
    @Override
    public void create()
    {
        instantiate((e) ->
        {
            e.addComponent(new Sprite());
            e.addComponent(new RotateScript());
            e.setMaterial(new Material(new Texture("/com/xebisco/yield/assets/yieldlogo.png")));
        });
    }

    public static void main(String[] args)
    {
        new View(1280, 720);
        View.getActView().setBgColor(Colors.WHITE);
        final GameConfiguration config = new GameConfiguration();
        config.hardwareAcceleration = true;
        launch(new MyGame(), config);
    }
}

class RotateScript extends YldScript
{
    @Override
    public void start()
    {
        transform.goTo(View.mid());
    }

    @Override
    public void update(float delta)
    {
        transform.rotate(100 * delta);
        transform.scale(delta, delta);
    }
}