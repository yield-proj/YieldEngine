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
    public void start() {
        Texture t = new Texture("com/xebisco/yield/assets/yieldlogo.png");
        loadTexture(t);
        instantiate( e -> {
            e.center();
            e.addComponent(new Move());
            e.addComponent(new Sprite(new Vector2(100, 100)));
            e.addComponent(new PhysicsBody());
            e.getComponent(PhysicsBody.class).setContinuousCollision(true);
           // e.getComponent(PhysicsBody.class).setFixedRotation(true);
            e.getMaterial().setTexture(t);
            e.addComponent(new RectCollider(new Vector2(100, 100)));
        });
        /*for(int i = 0; i < 100; i++) {
            int finalI = i;
            instantiate(e -> {
                e.getSelfTransform().goTo(Yld.RAND.nextInt(view.getWidth()), -finalI * 50);
                e.addComponent(new Sprite(new Vector2(100, 100)));
                e.addComponent(new PhysicsBody());
                e.getMaterial().setTexture(t);
                e.addComponent(new RectCollider(new Vector2(100, 100)));
            });}*/
        instantiate( e -> {
            e.center();
            e.getSelfTransform().translate(0, 200);
            e.addComponent(new Rectangle(new Vector2(view.getWidth() - 160, 100)));
            e.addComponent(new PhysicsBody(PhysicsBodyType.STATIC));
            e.addComponent(new RectOffEdges(new Vector2(view.getWidth() - 160, 100)));
        });
    }

    @Override
    public void update(float delta) {
        Yld.log(Yld.MEMORY());
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
        if(input.isJustPressed(Key.UP)) {
            body.addLinearVelocity(new Vector2(0, -20));
        }
        if(input.isJustPressed(Key.DOWN)) {
            body.addLinearVelocity(new Vector2(0, 20));
        }
        if(input.isJustPressed(Key.LEFT)) {
            body.addLinearVelocity(new Vector2(-20, 0));
        }
        if(input.isJustPressed(Key.RIGHT)) {
            body.addLinearVelocity(new Vector2(20, 0));
        }
    }
}