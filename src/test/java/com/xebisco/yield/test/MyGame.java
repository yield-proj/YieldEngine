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

    Texture texture = new Texture("com/xebisco/yield/test/assets/fox.png");

    Prefab block = e -> {
        NonFillShape rect = new Sprite();
        e.addComponent(rect);
        e.addComponent(new Rotate());
        e.getMaterial().setTexture(texture);
        rect.setSize(new Vector2(1280, 720));
        e.center();
    };

    @Override
    public void create() {
        //view = new View(64, 36);
        loadTexture(texture);
        texture.setFilter(p -> {
            p.getColor().setR(p.getColor().getR() + .05f);
            p.getColor().setB(p.getColor().getB() + .05f);
        });
        processFilters(texture);
        instantiate(block);
    }

    @Override
    public void update(float delta) {
        //Yld.log(Yld.MEMORY());
        view.setRotation(view.getRotation() + 1);
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        config.resizable = true;
        launch(new MyGame(), config);
    }
}

class Rotate extends YldScript {
    @Override
    public void update(float delta) {
        //transform.rotate(45 * delta);
    }
}