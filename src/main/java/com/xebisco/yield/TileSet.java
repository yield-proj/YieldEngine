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

import java.util.HashMap;
import java.util.Map;

public class TileSet {
    private final Map<Color, Tile> tiles = new HashMap<>();
    private Vector2 defaultSize = new Vector2(32, 32);

    @SafeVarargs
    public TileSet(YldPair<Color, Tile>... tiles) {
        for (YldPair<Color, Tile> tile : tiles) {
            addTile(tile.getFirst(), tile.getSecond());
        }
    }

    @SafeVarargs
    public TileSet(Vector2 defaultSize, YldPair<Color, Tile>... tiles) {
        this.defaultSize = defaultSize;
        for (YldPair<Color, Tile> tile : tiles) {
            addTile(tile.getFirst(), tile.getSecond());
        }
    }

    public Tile addTile(Color color, Tile tile) {
        if (tile.getSize() == null) {
            tile.setSize(defaultSize.get());
        }
        this.tiles.put(color, tile);
        return tile;
    }

    public Map<Color, Tile> getTiles() {
        return tiles;
    }
}
