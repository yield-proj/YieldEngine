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

/**
 * It's a getter for common tiles.
 */
public class TileSet {
    private final Map<Integer, TileGen> tiles = new HashMap<>();

    @SafeVarargs
    public TileSet(YldPair<Color, TileGen>... tiles) {
        for (YldPair<Color, TileGen> tile : tiles) {
            addTile(tile.getFirst(), tile.getSecond());
        }
    }

    /**
     * It adds a tile to the list of tiles
     *
     * @param color The color of the tile.
     * @param tile The tile to add.
     */
    public void addTile(Color color, TileGen tile) {
        this.tiles.put(color.getRGB(), tile);
    }

    /**
     * Add a tile to the map with the given color and tile.
     *
     * @param color The color of the tile.
     * @param tile The tile to be added.
     */
    public void addTile(Color color, Tile tile) {
        addTile(color, new TileGen(tile));
    }

    /**
     * This function returns a map of integers to TileGen objects.
     *
     * @return A map of integers and TileGen objects.
     */
    public Map<Integer, TileGen> getTiles() {
        return tiles;
    }
}
