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

import java.util.Arrays;

public class Test2 extends YldGame {

    @Override
    public void start() {
        graphics.img(getAssets().getTexture("yield.png")).center();
        addScene(new Test());
        addScene(new Loading());

    }

    @Override
    public void update(float delta) {
        if (getFrames() == 100) {
            setScene(Test.class, Loading.class);
        }
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        Ini.file(new RelativeFile("game.ini"), config);
        launch(new Test2(), config);
    }
}

class Loading extends YldProgressScene {

    Rectangle t;
    float w;

    @Override
    public void create() {
        instantiate(e -> {
            e.addComponent(t = new Rectangle());
            t.setSize(new Vector2(0, 64));
            e.center();
        });
    }

    @Override
    public void update(float delta) {
        w = getProgress() * view.getWidth();
        t.getSize().x += (w - t.getSize().x) / 8f;
    }
}

class Test extends YldScene {

    @Override
    public void start() {
        graphics.img(getAssets().getTexture("img.png")).center();
        graphics.img(getAssets().getTexture("yield.png")).center();
        log(Arrays.toString(getAssets().getTextFile("test2.txt").getContents()));
        log(Arrays.toString(getAssets().getTextFile("test.txt").getContents()));
        log(Arrays.toString(getAssets().getTextFile("test3.txt").getContents()));
    }
}