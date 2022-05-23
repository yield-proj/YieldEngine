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
import java.util.Arrays;
import java.util.HashSet;

public class TileSet
{
    private ArrayList<TileID> tiles = new ArrayList<>();

    public TileSet(TileID... tiles)
    {
        if (tiles != null)
            this.tiles.addAll(Arrays.asList(tiles));
    }

    public Tile addTile(TileID tile) {
        tiles.add(tile);
        return tile.getTile();
    }

    public void addTiles(TileID... tiles) {
        this.tiles.addAll(Arrays.asList(tiles));
    }

    public ArrayList<TileID> getTiles()
    {
        return tiles;
    }

    public void setTiles(ArrayList<TileID> tiles)
    {
        this.tiles = tiles;
    }
}
