/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileMap extends SimpleRenderable {
    private List<YldPair<Vector2, Tile>> tiles;

    private boolean dynamicProcessing = true;

    private Vector2 grid = new Vector2(32, 32);

    public static TileMap loadTileMap(Color[][] colors, TileMap tileMap, TileSet tileSet) {
        tileMap.tiles = new ArrayList<>();
        if (colors.length > 0) {
            for (int x = 0; x < colors.length; x++) {
                for (int y = 0; y < colors[0].length; y++) {
                    tileMap.tiles.add(new YldPair<>(new Vector2(x * tileMap.grid.x, y * tileMap.grid.y), tileSet.getTiles().get(colors[x][y])));
                }
            }
        }
        return tileMap;
    }

    @Override
    public void render(SampleGraphics graphics) {
        super.render(graphics);
        for (int i = 0; i < tiles.size(); i++) {
            YldPair<Vector2, Tile> pair = tiles.get(i);
            Tile tile = pair.getSecond();
            if (tile != null) {
                Vector2 pos = pair.getFirst().sum(getTransform().position).sum(tile.getOffSet()).subt(scene.getView().getCamera().getPosition()).subt(tile.getSize().mul(getTransform().scale).div(2f)),
                        size = tile.getSize().mul(getTransform().scale);
                boolean render = true;
                if (dynamicProcessing && (transform.rotation % 360 == 0 || transform.rotation == 0)) {
                    if (!(pos.x + size.x / 2f >= 0 &&
                            pos.x - size.x / 2f < scene.getView().getWidth() &&
                            pos.y + size.y / 2f >= 0 &&
                            pos.y - size.y / 2f < scene.getView().getHeight())) {
                        render = false;
                    }
                }
                if (render) {
                    graphics.drawTexture(tile.getTexture(), pos, size);
                    getEntity().transmit("processTile", pos, tile.getLayer());
                }
            }
        }
    }

    public List<YldPair<Vector2, Tile>> getTiles() {
        return tiles;
    }

    public void setTiles(List<YldPair<Vector2, Tile>> tiles) {
        this.tiles = tiles;
    }

    public Vector2 getGrid() {
        return grid;
    }

    public void setGrid(Vector2 grid) {
        this.grid = grid;
    }

    public boolean isDynamicProcessing() {
        return dynamicProcessing;
    }

    public void setDynamicProcessing(boolean dynamicProcessing) {
        this.dynamicProcessing = dynamicProcessing;
    }
}
