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

public class MyGame extends YldGame
{
    @Override
    public void create()
    {
        //new View(427, 240);
        Yld.message("test");
        Texture tileSetImage = new Texture("/com/xebisco/yield/test/assets/testtileset.png"), tileMapImage = new Texture("/com/xebisco/yield/test/assets/testmap.png");
        TileSet tileSet = new TileSet(new TileID(new Tile(tileSetImage, 0, 0, 16, 16, 0), new IntColor(0, 0, 0)), new TileID(new Tile(tileSetImage, 0, 16, 16, 16, 1), new IntColor(255, 255, 255)));
        instantiate((e) -> {
            e.addComponent(new TileMap(tileSet));
            e.getComponent(TileMap.class).loadMap(tileMapImage, new Vector2(16, 16));
        });
    }

    @Override
    public void update(float delta)
    {
        if(input.isPressing(Key.RIGHT)) {
            View.getActView().getCamera().getPosition().x++;
        }
        if(input.isPressing(Key.LEFT)) {
            View.getActView().getCamera().getPosition().x--;
        }
        if(input.isPressing(Key.DOWN)) {
            View.getActView().getCamera().getPosition().y++;
        }
        if(input.isPressing(Key.UP)) {
            View.getActView().getCamera().getPosition().y--;
        }
        System.out.println(time.getFps());
    }

    public static void main(String[] args)
    {
        launch(new MyGame());
    }
}