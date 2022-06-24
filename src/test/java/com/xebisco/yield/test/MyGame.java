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
        view = new View(1080 / 2, 720 / 2);
        Texture tileMapTexture = new Texture("com/xebisco/yield/test/assets/testmap.png");
        Texture tileSetTexture = new Texture("com/xebisco/yield/test/assets/testtileset.png");
        loadTexture(tileMapTexture);
        loadTexture(tileSetTexture);
        TileSet tileSet = new TileSet(new Vector2(16, 16),
                new YldPair<>(Colors.BLACK, new Tile(cutTexture(tileSetTexture, new Vector2(), new Vector2(16, 16)))),
                new YldPair<>(Colors.WHITE, new Tile(cutTexture(tileSetTexture, new Vector2(0, 16), new Vector2(16, 16)))));
        TileMap tileMap = TileMap.loadTileMap(getTextureColors(tileMapTexture), new TileMap(), tileSet, new Vector2(16, 16));
        e = instantiate((e) -> {
            e.addComponent(tileMap);
            e.center();
        });
    }

    @Override
    public void update(float delta) {
        view.getCamera().getPosition().x += delta * 25f;
        view.getCamera().getPosition().y += delta * 50f;
        Yld.log(Yld.MEMORY());
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMasterName = "com.xebisco.yield.render.swing.SwingYield";
        launch(new MyGame(), config);
    }
}