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
    @Override
    public void create() {
        Texture t = new Texture("com/xebisco/yield/assets/yieldlogo.png");
        loadTexture(t);
        instantiate( e -> {
            e.center();
            e.addComponent(new Move());
            e.addComponent(new Sprite(new Vector2(100, 100)));
            e.addComponent(new PhysicsBody());
           // e.getComponent(PhysicsBody.class).setFixedRotation(true);
            e.getMaterial().setTexture(t);
            e.addComponent(new RectCollider(new Vector2(100, 100)));
        });
        for(int i = 0; i < 20 ; i++)
            instantiate( e -> {
                e.center();
                e.addComponent(new Sprite(new Vector2(100, 100)));
                e.addComponent(new PhysicsBody());
                e.getMaterial().setTexture(t);
                e.addComponent(new RectCollider(new Vector2(100, 100)));
            }, this);
        instantiate( e -> {
            e.center();
            e.getSelfTransform().translate(0, 200);
            e.addComponent(new Sprite(new Vector2(100, 100)));
            e.getMaterial().setTexture(t);
            e.addComponent(new PhysicsBody(PhysicsBodyType.STATIC));
            e.addComponent(new RectCollider(new Vector2(100, 100)));
        });
    }

    public static void main(String[] args) {
        Yld.debug = true;
        GameConfiguration config = new GameConfiguration();
        Ini.file(new RelativeFile("com/xebisco/yield/test/assets/game.ini"), config);
        launch(new MyGame(), config);
    }
}

class Move extends YldScript {
    PhysicsBody body;

    @Override
    public void start() {
        body = getComponent(PhysicsBody.class);
    }

    @Override
    public void update(float delta) {
        if(input.isPressed(Key.UP)) {
            body.addLinearVelocity(new Vector2(0, -2000));
        }
        if(input.isPressed(Key.LEFT)) {
            body.addLinearVelocity(new Vector2(-2000, 0));
        }
        if(input.isPressed(Key.RIGHT)) {
            body.addLinearVelocity(new Vector2(2000, 0));
        }
    }
}