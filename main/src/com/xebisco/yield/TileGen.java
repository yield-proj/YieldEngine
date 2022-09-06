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

/**
 * It's a class that generates tiles
 */
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

    /**
     * If the tile is a mapped tile, then it will return a tile based on the surrounding tiles. If it's not a mapped tile,
     * then it will return a random tile from the tiles array
     *
     * @return A tile.
     */
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

    /**
     * This function returns the tiles array.
     *
     * @return The tiles array.
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * This function sets the tiles to the tiles passed in as a parameter.
     *
     * @param tiles The tiles that make up the tile generator.
     */
    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    /**
     * This function returns the MappedTiles object that is stored in the variable mappedTiles.
     *
     * @return The mappedTiles object.
     */
    public MappedTiles getMappedTiles() {
        return mappedTiles;
    }

    /**
     * This function sets the value of the variable `mappedTiles` to the value of the variable `mappedTiles` that is
     * passed in as a parameter
     *
     * @param mappedTiles This is the MappedTiles object that contains the tiles that are mapped to the map.
     */
    public void setMappedTiles(MappedTiles mappedTiles) {
        this.mappedTiles = mappedTiles;
    }

    /**
     * This function returns the textures array.
     *
     * @return The textures array.
     */
    public Texture[] getTextures() {
        return textures;
    }

    /**
     * This function sets the textures of the object to the textures passed in.
     *
     * @param textures The array of textures to be used for the tile generation.
     */
    public void setTextures(Texture[] textures) {
        this.textures = textures;
    }

    /**
     * This function returns the standard prefab.
     *
     * @return The standardPrefab variable is being returned.
     */
    public Prefab getStandardPrefab() {
        return standardPrefab;
    }

    /**
     * This function sets the standardPrefab variable to the value of the standardPrefab parameter.
     *
     * @param standardPrefab The prefab that will be used to generate the tiles if the tile prefab is null.
     */
    public void setStandardPrefab(Prefab standardPrefab) {
        this.standardPrefab = standardPrefab;
    }

    /**
     * This function sets the tile object to the tile passed in as a parameter.
     *
     * @param tile The tile value to set.
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * > This function returns the tilesToCompare variable
     *
     * @return The tilesToCompare object.
     */
    public TilesToCompare getTilesToCompare() {
        return tilesToCompare;
    }

    /**
     * This function sets the tilesToCompare variable to the tilesToCompare parameter
     *
     * @param tilesToCompare This is the object that contains the tiles to compare.
     */
    public void setTilesToCompare(TilesToCompare tilesToCompare) {
        this.tilesToCompare = tilesToCompare;
    }

    /**
     * This function returns the layer of the tile generator.
     *
     * @return The layer of the tile generator.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Sets the layer of the tile generator.
     *
     * @param layer The layer value to set.
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }
}
