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

    }


    @Override
    public void start() {
        instantiate((e) -> {
            e.instantiate((e1) -> {
                e1.addComponent(new Rectangle());
                e1.getComponent(Rectangle.class).setColor(Colors.BLACK);
            });
            e.instantiate((e1) -> {
                e1.addComponent(new Text());
                e1.addComponent(new FPSCounter());
            });
            e.getSelfTransform().goTo(new Vector2(30, 30));
        });


    }

    @Override
    public void update(float delta) {
        Yld.log(Yld.MEMORY() + " - " + getMasterEntity().getChildren().size());
    }

    public static void main(String[] args) {
        Yld.debug = false;
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        config.resizable = true;
        int fps = 60;
        if(args.length > 0)
            fps = Integer.parseInt(args[0]);
        config.fps = fps;
        launch(new MyGame(), config);
    }
}

class FPSCounter extends YldScript {
    Text text;

    @Override
    public void start() {
        text = getComponent(Text.class);
    }

    @Override
    public void update(float delta) {
        text.setContents(String.format("%.0f", time.getRenderFps()));

    }
}