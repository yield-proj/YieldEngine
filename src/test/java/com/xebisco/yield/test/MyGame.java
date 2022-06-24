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

    Entity e;

    @Override
    public void create() {
        Texture map = new Texture("com/xebisco/yield/test/assets/testmap.png"),
                set = new Texture("com/xebisco/yield/test/assets/testtileset.png");
        loadTexture(map);
        loadTexture(set);

        TileSet tileSet = new TileSet(
                new YldPair<>(Colors.BLACK, new Tile(cutTexture(set, new Vector2(), new Vector2(16, 16)))),
                new YldPair<>(Colors.WHITE, new Tile(cutTexture(set, new Vector2(0, 16), new Vector2(16, 16))))
        );

        TileMap tileMap = TileMap.loadTileMap(getTextureColors(map), new TileMap(), tileSet);

        e = instantiate(e -> {
            e.addComponent(tileMap);
            e.center();
        });
        unloadTexture(map);
        unloadTexture(set);
    }

    @Override
    public void update(float delta) {
        e.getSelfTransform().translate(-1, -1);
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        launch(new MyGame(), config);
    }
}