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

    static boolean playerLife;

    static Vector2 playerPos = new Vector2();

    Prefab prefab = e -> {
        e.addComponent(new Sprite());
        e.addComponent(new Move());
        e.getMaterial().setTexture(scaleTexture(getYieldLogo(), new Vector2(10, 10)));
        e.center();
    };

    Prefab spawner = e -> {
        e.addComponent(new Spawner());
    };

    @Override
    public void start() {
        playerLife = true;
        view = new View(320, 720);
        instantiate(prefab);
        instantiate(spawner);
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        config.fps = 60;
        config.width = 320;
        launch(new MyGame(), config);
    }
}

class Move extends YldScript {
    @Override
    public void update(float delta) {
        if (input.isJustPressed(Key.RIGHT))
            transform.position.x += 64;
        if (input.isJustPressed(Key.LEFT))
            transform.position.x -= 64;
        if (transform.position.x < 32)
            transform.position.x = 32;
        if (transform.position.x > scene.getView().getWidth() - 32)
            transform.position.x = scene.getView().getWidth() - 32;
        MyGame.playerPos = transform.position.get();
    }
}

class Obstacle extends YldScript {
    @Override
    public void update(float delta) {
        transform.position.y += 8;
        if (MyGame.playerPos.x == transform.position.x) {
            if (MyGame.playerPos.y - 32 <= transform.position.y + 32 &&
                    MyGame.playerPos.y + 32 >= transform.position.y - 32) {
                MyGame.playerLife = false;
                game.setScene(MyGame.class);
            }
        }
    }
}

class Spawner extends YldScript {
    @Override
    public void update(float delta) {
        if (getFrames() % 60 == 0) {
            scene.instantiate(e -> {
                e.addComponent(new Rectangle());
                e.addComponent(new Obstacle());
                e.getSelfTransform().goTo(Yld.RAND.nextInt(5) * 64 + 32, 0);
            });
        }
    }
}