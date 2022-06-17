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

public class MyGame extends YldGame {

    static int count;
    static boolean over;

    @Override
    public void start() {
        view = new View(427, 240);
        Texture t = new Texture("com/xebisco/yield/assets/yieldlogo.png");
        loadTexture(t);

        loadFont("arial", 12, 0);

        instantiate((e) -> {
            e.addComponent(new Sprite());
            e.addComponent(new Clickable());
            e.getMaterial().setTexture(t);
            e.center();
        });

        instantiate((e) -> {
            e.addComponent(new Text());
            e.addComponent(new Counter());
            e.getSelfTransform().goTo(view.getWidth() / 2f, 10);
        });

    }

    @Override
    public void update(float delta) {

    }

    public static void main(String[] args) {
        Yld.debug = true;
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        config.resizable = true;
        launch(new MyGame(), config);
    }
}

class Clickable extends YldScript {

    Sprite sprite;
    float scaleV, scaleR, addScale;

    @Override
    public void start() {
        sprite = getComponent(Sprite.class);
    }

    @Override
    public void update(float delta) {
        transform.rotate(delta * 50);
        MyGame.over = input.isTouching(sprite);
        if (MyGame.over) {
            scaleR = 1.5f;
        } else {
            scaleR = 1;
        }
        scaleV += (scaleR - scaleV) / 8f;
        if (MyGame.over && (input.isJustPressed(Key.MOUSE_1) || input.isJustPressed(Key.MOUSE_3))) {
            addScale += .2f;
            MyGame.count++;
        }
        addScale -= addScale / 16;
        if (addScale < 0)
            addScale = 0;
        transform.scale.x = scaleV + addScale;
        transform.scale.y = scaleV + addScale;
    }
}

class Counter extends YldScript {

    Text text;

    @Override
    public void start() {
        text = getComponent(Text.class);
    }

    @Override
    public void update(float delta) {
        text.setContents(String.valueOf(MyGame.count));
    }
}