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

public class MyGame extends YldGame
{
    Entity e;
    @Override
    public void start() {
        view = new View(1000, 1000);
        Texture t = new Texture("com/xebisco/yield/assets/icon.png");
        load(t);
        e = instantiate((e) -> {
            e.getMaterial().setTexture(t);
            e.addComponent(new Sprite());
            e.getComponent(Sprite.class).setSize(new Vector2(100, 100));
            e.getSelfTransform().goTo(50, 50);
        });
    }

    @Override
    public void update(float delta) {
        Yld.log(Yld.MEMORY());
        if(input.isPressed(Key.RIGHT)) {
            e.getSelfTransform().translate(100, 0);
        }
        if(input.isPressed(Key.LEFT)) {
            e.getSelfTransform().translate(-100, 0);
        }
        if(input.isPressed(Key.UP)) {
            e.getSelfTransform().translate(0, -100);
        }
        if(input.isPressed(Key.DOWN)) {
            e.getSelfTransform().translate(0, 100);
        }
        view.setBgColor(new Color(Yld.RAND.nextInt(255) / 255f, Yld.RAND.nextInt(255) / 255f,Yld.RAND.nextInt(255) / 255f, 1));
    }

    public static void main(String[] args)
    {
        Yld.debug = true;
        GameConfiguration config = new GameConfiguration();
        config.width = 500;
        config.height = 500;
        config.resizable = true;
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        launch(new MyGame(), config);
    }
}