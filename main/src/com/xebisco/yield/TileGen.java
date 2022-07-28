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

    private MappedTiles mappedTiles;

    private TilesToCompare tilesToCompare;

    private Texture[] textures;

    private int layer = -1;

    private Prefab standardPrefab = null;

    public TileGen(MappedTiles mappedTiles) {
        this.mappedTiles = mappedTiles;
    }

    public TileGen(MappedTiles mappedTiles, int layer) {
        this.mappedTiles = mappedTiles;
        this.layer = layer;
    }

    public TileGen(Texture texture) {
        tile = new Tile(texture);
    }

    public TileGen(Texture texture, int layer) {
        tile = new Tile(texture);
        this.layer = layer;
    }

    public TileGen(Texture texture, Prefab prefab) {
        tile = new Tile(texture, prefab);
    }

    public TileGen(Texture texture, Prefab prefab, int layer) {
        tile = new Tile(texture, prefab);
        this.layer = layer;
    }

    public TileGen(Tile tile) {
        this.tile = tile;
    }

    public TileGen(Texture[] textures) {
        this.textures = textures;
    }

    public TileGen(Texture[] textures, int layer) {
        this.textures = textures;
        this.layer = layer;
    }

    public TileGen(Texture[] textures, Prefab standardPrefab) {
        this.textures = textures;
        this.standardPrefab = standardPrefab;
    }

    public TileGen(Texture[] textures, Prefab standardPrefab, int layer) {
        this.textures = textures;
        this.standardPrefab = standardPrefab;
        this.layer = layer;
    }

    public TileGen(Tile[] tiles) {
        this.tiles = tiles;
    }

    public TileGen(Tile[] tiles, int layer) {
        this.tiles = tiles;
        this.layer = layer;
    }

    public Tile getTile() {
        Tile toReturn;
        if (mappedTiles == null) {
            if (tiles == null) {
                if (textures == null)
                    toReturn = tile;
                else {
                    toReturn = new Tile(textures[Yld.RAND.nextInt(textures.length)]);
                }
            } else {
                toReturn = tiles[Yld.RAND.nextInt(tiles.length)];
            }
        } else {
            if (tilesToCompare != null) {
                if (mappedTiles.getTargetLayer() == -1) {
                    Tile target = mappedTiles.getTarget().getTile();
                    if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getLeft().getTile() == target && tilesToCompare.getRight().getTile() == target && tilesToCompare.getTop().getTile() == target)
                        toReturn = mappedTiles.getAll().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getLeft().getTile() == target && tilesToCompare.getRight().getTile() == target)
                        toReturn = mappedTiles.getLessTop().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getTop().getTile() == target && tilesToCompare.getRight().getTile() == target)
                        toReturn = mappedTiles.getLessLeft().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getTop().getTile() == target && tilesToCompare.getLeft().getTile() == target)
                        toReturn = mappedTiles.getLessRight().getTile();
                    else if (tilesToCompare.getRight().getTile() == target && tilesToCompare.getTop().getTile() == target && tilesToCompare.getLeft().getTile() == target)
                        toReturn = mappedTiles.getLessDown().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getLeft().getTile() == target)
                        toReturn = mappedTiles.getBottomAndLeft().getTile();
                    else if (tilesToCompare.getTop().getTile() == target && tilesToCompare.getLeft().getTile() == target)
                        toReturn = mappedTiles.getLeftAndTop().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getRight().getTile() == target)
                        toReturn = mappedTiles.getRightAndBottom().getTile();
                    else if (tilesToCompare.getTop().getTile() == target && tilesToCompare.getRight().getTile() == target)
                        toReturn = mappedTiles.getTopAndRight().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target && tilesToCompare.getTop().getTile() == target)
                        toReturn = mappedTiles.getTopAndDown().getTile();
                    else if (tilesToCompare.getRight().getTile() == target && tilesToCompare.getLeft().getTile() == target)
                        toReturn = mappedTiles.getRightAndLeft().getTile();
                    else if (tilesToCompare.getBottom().getTile() == target)
                        toReturn = mappedTiles.getBottom().getTile();
                    else if (tilesToCompare.getLeft().getTile() == target)
                        toReturn = mappedTiles.getLeft().getTile();
                    else if (tilesToCompare.getRight().getTile() == target)
                        toReturn = mappedTiles.getRight().getTile();
                    else if (tilesToCompare.getTop().getTile() == target)
                        toReturn = mappedTiles.getTop().getTile();
                    else if (tilesToCompare.getOnlyTopLeft().getTile() == target)
                        toReturn = mappedTiles.getOnlyTopLeft().getTile();
                    else if (tilesToCompare.getOnlyTopRight().getTile() == target)
                        toReturn = mappedTiles.getOnlyTopRight().getTile();
                    else if (tilesToCompare.getOnlyBottomLeft().getTile() == target)
                        toReturn = mappedTiles.getOnlyBottomLeft().getTile();
                    else if (tilesToCompare.getOnlyRightBottom().getTile() == target)
                        toReturn = mappedTiles.getOnlyRightBottom().getTile();
                    else
                        toReturn = mappedTiles.getMiddle().getTile();
                } else {
                    if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getAll().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getLessTop().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getLessLeft().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getLessRight().getTile();
                    else if (tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getLessDown().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getBottomAndLeft().getTile();
                    else if (tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getLeftAndTop().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getRightAndBottom().getTile();
                    else if (tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getTopAndRight().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getTopAndDown().getTile();
                    else if (tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer() && tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getRightAndLeft().getTile();
                    else if (tilesToCompare.getBottom().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getBottom().getTile();
                    else if (tilesToCompare.getLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getLeft().getTile();
                    else if (tilesToCompare.getRight().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getRight().getTile();
                    else if (tilesToCompare.getTop().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getTop().getTile();
                    else if (tilesToCompare.getOnlyTopLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getOnlyTopLeft().getTile();
                    else if (tilesToCompare.getOnlyTopRight().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getOnlyTopRight().getTile();
                    else if (tilesToCompare.getOnlyBottomLeft().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getOnlyBottomLeft().getTile();
                    else if (tilesToCompare.getOnlyRightBottom().getTile().getLayer() == mappedTiles.getTargetLayer())
                        toReturn = mappedTiles.getOnlyRightBottom().getTile();
                    else
                        toReturn = mappedTiles.getMiddle().getTile();
                }
            } else {
                toReturn = mappedTiles.getMiddle().getTile();
            }
        }
        if (layer != -1)
            toReturn.setLayer(layer);
        if (standardPrefab != null && toReturn.getPrefab() == null)
            toReturn.setPrefab(standardPrefab);
        return toReturn;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public MappedTiles getMappedTiles() {
        return mappedTiles;
    }

    public void setMappedTiles(MappedTiles mappedTiles) {
        this.mappedTiles = mappedTiles;
    }

    public Texture[] getTextures() {
        return textures;
    }

    public void setTextures(Texture[] textures) {
        this.textures = textures;
    }

    public Prefab getStandardPrefab() {
        return standardPrefab;
    }

    public void setStandardPrefab(Prefab standardPrefab) {
        this.standardPrefab = standardPrefab;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public TilesToCompare getTilesToCompare() {
        return tilesToCompare;
    }

    public void setTilesToCompare(TilesToCompare tilesToCompare) {
        this.tilesToCompare = tilesToCompare;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
