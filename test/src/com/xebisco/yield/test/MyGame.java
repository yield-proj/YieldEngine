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
    Texture texture;
    int sclState;
    Entity e;
    Text text;

    @Override
    public void start() {
        texture = new Texture("com/xebisco/yield/assets/yieldlogo.png");
        loadTexture(texture);
        float xv = 1f / texture.getWidth(), yv = 1f / texture.getHeight();
        texture.setFilter(p -> {
            p.setOutColor(p.getColor().darker((p.getIndexX() * xv + p.getIndexY() * yv) / 2f * (1 - (e.getSelfTransform().scale.x + 1.2f))));
            p.setOutLocation(p.getLocation());
        });
        e = graphics.img(texture);
        e.center();
        Entity t = instantiate(e -> {
            e.addComponent(text = new Text());
            e.setIndex(-1);
            e.center();
            e.getSelfTransform().position.y = 20;
        });
    }

    @Override
    public void update(float delta) {
        text.setContents(String.format("%.0f", time.getFps()));
        if (sclState == 0)
            e.getSelfTransform().scale.x = Yld.cos(getFrames() / time.getTargetFPS());
        if (e.getSelfTransform().scale.x > 1)
            sclState = 0;
        else if (e.getSelfTransform().scale.x < -1)
            sclState = 1;
        texture.processFilters();
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        Ini.file(new RelativeFile("com/xebisco/yield/test/assets/game.ini"), config);
        launch(new MyGame(), config);
    }
}