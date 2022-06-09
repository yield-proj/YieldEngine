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

    int index;

    @Override
    public void start() {
        timer(() -> {
            graphics.setColor(Colors.random());
            Entity e = graphics.text("Hello, World!");
            e.setVisible(false);
            e.setIndex(index);
            e.addComponent(new Move());
            index--;
        }, .05f, true);
    }

    public static void main(String[] args) {
        Yld.debug = true;
        GameConfiguration config = new GameConfiguration();
        config.fps = 60;
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        launch(new MyGame(), config);
    }
}

class Move extends YldScript {
    NonFillShape s;

    @Override
    public void start() {
        s = getComponent(Text.class);
        setVisible(true);
        transform.goTo(scene.getView().mid());
    }

    int vx = -1, vy = -1;

    @Override
    public void update(float delta) {
        transform.rotate(2);

        if (transform.position.x - s.getSize().x / 2 < 0) {
            tick();
            vx = 1;
        }
        if (transform.position.x + s.getSize().x / 2 > scene.getView().getWidth()) {
            tick();
            vx = -1;
        }
        if (transform.position.y - s.getSize().y / 2 < 0) {
            tick();
            vy = 1;
        }
        if (transform.position.y + s.getSize().y / 2 > scene.getView().getHeight()) {
            tick();
            vy = -1;
        }

        transform.translate(vx * 5, vy * 5);
    }

    public void tick() {
        s.setColor(Colors.random());
    }
}