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

import javax.swing.*;

public class MyGame extends YldGame {

    static boolean playerLife, canDie;

    static Vector2 playerPos = new Vector2();

    Prefab prefab = e -> {
        e.addComponent(new Sprite());
        e.addComponent(new Move());
        e.setIndex(-1);
        e.center();
        e.getSelfTransform().position.y = view.getHeight() - 128;
    };

    Prefab spawner = e -> {
        e.addComponent(new Spawner());
    };

    @Override
    public void start() {
        playerLife = true;
        view = new View(64 * 3, 720);
        addSystem(new ESCExitSystem());
        instantiate(prefab);
        instantiate(spawner);
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.swingyield.SwingYield";
        config.fps = 60;
        config.width = 64 * 3;
        launch(new MyGame(), config);
    }
}

class Move extends YldScript {

    float toJump;

    @Override
    public void update(float delta) {
        toJump -= delta;
        if(toJump < 0)
            toJump = 0;
        if (input.isJustPressed(Key.RIGHT) || input.isJustPressed(Key.D))
            transform.position.x += 64;
        if (input.isJustPressed(Key.LEFT) || input.isJustPressed(Key.A))
            transform.position.x -= 64;
        if (transform.position.x < 32)
            transform.position.x = 32;
        if (transform.position.x > scene.getView().getWidth() - 32)
            transform.position.x = scene.getView().getWidth() - 32;
        if (input.isJustPressed(Key.SPACE) && toJump == 0) {
            toJump = 4;
            transform.scale(new Vector2(.3f, .3f));
        }
        if(transform.scale.x > 1)
            transform.scale.x -= delta / 3;
        if(transform.scale.y > 1)
            transform.scale.y -= delta / 3;
        MyGame.canDie = transform.scale.x <= 1 || transform.scale.y <= 1;
        MyGame.playerPos = transform.position.get();
    }
}

class Obstacle extends YldScript {
    @Override
    public void update(float delta) {
        transform.position.y += 8;
        if (MyGame.playerPos.x == transform.position.x && MyGame.canDie) {
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
        if (getFrames() % (Yld.RAND.nextInt(200) + 1) == 0) {
            scene.instantiate(e -> {
                e.addComponent(new Rectangle());
                e.addComponent(new Obstacle());
                e.getSelfTransform().goTo(Yld.RAND.nextInt(3) * 64 + 32, -32);
            });
        }
    }
}