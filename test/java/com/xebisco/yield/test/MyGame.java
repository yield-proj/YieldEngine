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
import com.xebisco.yield.colliders.RectCollider;
import com.xebisco.yield.components.PhysicsBody;
import com.xebisco.yield.components.Sprite;
import com.xebisco.yield.utils.Vector2;

public class MyGame extends YldGame
{
    Entity e, e1;
    @Override
    public void create()
    {
        e = instantiate((e) ->
        {
            e.addComponent(new Sprite());
            e.addComponent(new PhysicsBody());
            e.getComponent(PhysicsBody.class).setCollider(new RectCollider(64, 64));
            e.getComponent(PhysicsBody.class).addForce(new Vector2(99999, 0));
            e.getMaterial().setTexture(new Texture("/com/xebisco/yield/assets/yieldlogo.png"));
        });
        e1 = instantiate((e) ->
        {
            e.addComponent(new Sprite());
            e.addComponent(new PhysicsBody());
            e.getComponent(PhysicsBody.class).setCollider(new RectCollider(64, 64));
            e.getMaterial().setTexture(new Texture("/com/xebisco/yield/assets/yieldlogo.png"));
            e.getSelfTransform().translate(0, 100);
        });
    }

    @Override
    public void update(float delta)
    {

    }

    public static void main(String[] args)
    {
        new View(1280, 720);
        //View.getActView().setBgColor(Colors.WHITE);
        final GameConfiguration config = new GameConfiguration();
        config.hardwareAcceleration = true;
        launch(new MyGame(), config);
        /*Vec2 gravity = new Vec2(0,-10);
        World world = new World(gravity);
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, -10);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(50, 10);
        groundBody.createFixture(groundBox, 0);

        // Dynamic Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(0, 4);
        Body body = world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(1, 1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1;
        fixtureDef.friction = 0.3f;
        body.createFixture(fixtureDef);

        // Setup world
        float timeStep = 1.0f/60.0f;
        int velocityIterations = 6;
        int positionIterations = 2;

        // Run loop
        for (int i = 0; i < 99999999; ++i) {
            world.step(timeStep, velocityIterations, positionIterations);
            Vec2 position = body.getPosition();
            float angle = body.getAngle();
            System.out.printf("%4.2f %4.2f %4.2f\n", position.x, position.y, angle);
        }*/
    }
}