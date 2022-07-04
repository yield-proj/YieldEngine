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

public class TileGen {

    private Tile tile;
    private Tile[] tiles;

    private MappedTiles mappedTiles, comparingMapped;

    private Texture[] textures;

    public TileGen(MappedTiles mappedTiles) {
        this.mappedTiles = mappedTiles;
    }

    public TileGen(Texture texture) {
        tile = new Tile(texture);
    }

    public TileGen(Tile tile) {
        this.tile = tile;
    }

    public TileGen(Texture[] textures) {
        this.textures = textures;
    }

    public TileGen(Tile[] tiles) {
        this.tiles = tiles;
    }

    public Tile getTile() {
        if (mappedTiles == null) {
            if (tiles == null) {
                if (textures == null)
                    return tile;
                else {
                    return new Tile(textures[Yld.RAND.nextInt(textures.length)]);
                }
            } else {
                return tiles[Yld.RAND.nextInt(tiles.length)];
            }
        } else {
            if (comparingMapped != null) {
                if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget() && comparingMapped.getRight() == mappedTiles.getTarget() && comparingMapped.getTop() == mappedTiles.getTarget())
                    return mappedTiles.getAll();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget() && comparingMapped.getRight() == mappedTiles.getTarget())
                    return mappedTiles.getLessTop();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getTop() == mappedTiles.getTarget() && comparingMapped.getRight() == mappedTiles.getTarget())
                    return mappedTiles.getLessLeft();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getTop() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget())
                    return mappedTiles.getLessRight();
                else if (comparingMapped.getRight() == mappedTiles.getTarget() && comparingMapped.getTop() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget())
                    return mappedTiles.getLessDown();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget())
                    return mappedTiles.getBottomAndLeft();
                else if (comparingMapped.getTop() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget())
                    return mappedTiles.getLeftAndTop();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getRight() == mappedTiles.getTarget())
                    return mappedTiles.getRightAndBottom();
                else if (comparingMapped.getTop() == mappedTiles.getTarget() && comparingMapped.getRight() == mappedTiles.getTarget())
                    return mappedTiles.getTopAndRight();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget() && comparingMapped.getTop() == mappedTiles.getTarget())
                    return mappedTiles.getTopAndDown();
                else if (comparingMapped.getRight() == mappedTiles.getTarget() && comparingMapped.getLeft() == mappedTiles.getTarget())
                    return mappedTiles.getRightAndLeft();
                else if (comparingMapped.getBottom() == mappedTiles.getTarget())
                    return mappedTiles.getBottom();
                else if (comparingMapped.getLeft() == mappedTiles.getTarget())
                    return mappedTiles.getLeft();
                else if (comparingMapped.getRight() == mappedTiles.getTarget())
                    return mappedTiles.getRight();
                else if (comparingMapped.getTop() == mappedTiles.getTarget())
                    return mappedTiles.getTop();
                else if (comparingMapped.getOnlyTopLeft() == mappedTiles.getTarget())
                    return mappedTiles.getOnlyTopLeft();
                else if (comparingMapped.getOnlyTopRight() == mappedTiles.getTarget())
                    return mappedTiles.getOnlyTopRight();
                else if (comparingMapped.getOnlyBottomLeft() == mappedTiles.getTarget())
                    return mappedTiles.getOnlyBottomLeft();
                else if (comparingMapped.getOnlyRightBottom() == mappedTiles.getTarget())
                    return mappedTiles.getOnlyRightBottom();
                else
                    return mappedTiles.getMiddle();
            } else {
                return mappedTiles.getMiddle();
            }
        }
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public MappedTiles getComparingMapped() {
        return comparingMapped;
    }

    public void setComparingMapped(MappedTiles comparingMapped) {
        this.comparingMapped = comparingMapped;
    }
}
