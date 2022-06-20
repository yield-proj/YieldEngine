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

    Entity player;

    @Override
    public void create() {
        player = instantiate((e) -> {
            e.addComponent(new Rectangle());
            e.center();
        });
    }

    @Override
    public void update(float delta) {
        player.getSelfTransform().translate(new Vector2(input.getAxis("Horizontal"), input.getAxis("Vertical")).mul(delta * 150f));
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        launch(new MyGame(), config);
    }
}