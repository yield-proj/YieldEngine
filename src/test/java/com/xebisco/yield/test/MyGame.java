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

import java.awt.*;

public class MyGame extends YldGame
{
    Entity text;
    @Override
    public void create()
    {
        text = instantiate((e) -> {
            e.addComponent(new Text("Hello, Yield!"));
            e.getComponent(Text.class).setFont(new Font("arial", Font.PLAIN, 40));
            e.getSelfTransform().goTo(View.mid());
        });
    }

    @Override
    public void update(float delta)
    {
        text.getSelfTransform().rotate(delta * 100);
    }

    public static void main(String[] args)
    {
        launch(new MyGame());
    }
}