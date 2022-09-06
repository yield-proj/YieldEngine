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
 * A tile is a texture with a size and an offset
 */
public class Tile {
    private Texture texture;
    private int layer = 0;
    private Vector2 size = defaultSize.get(), offSet = new Vector2();

    private static Vector2 defaultSize = new Vector2(32, 32);

    private Prefab prefab;

    public Tile() {

    }

    public Tile(Prefab prefab) {
        this.prefab = prefab;
    }

    public Tile(Texture texture) {
        this.texture = texture;
    }

    public Tile(Texture texture, Prefab prefab) {
        this.texture = texture;
        this.prefab = prefab;
    }

    /**
     * This function returns the texture of the object.
     *
     * @return The texture of the object.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * This function sets the texture of the object to the texture passed in as a parameter.
     *
     * @param texture The texture to be used for the sprite.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * This function returns the size of the object.
     *
     * @return The size of the object.
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * This function sets the size of the object to the size passed in.
     *
     * @param size The size of the object.
     */
    public void setSize(Vector2 size) {
        this.size = size;
    }

    /**
     * It returns the offSet variable
     *
     * @return The offset of the tile.
     */
    public Vector2 getOffSet() {
        return offSet;
    }

    /**
     * It sets the tile grid offset
     *
     * @param offSet The offset of the tile to set.
     */
    public void setOffSet(Vector2 offSet) {
        this.offSet = offSet;
    }

    /**
     * This function returns the layer of the tile
     *
     * @return The layer of the tile.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * This function sets the layer of the tile to the given layer.
     *
     * @param layer The layer that the tile will be.
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }

    /**
     * Returns the default size of any tile.
     *
     * @return The default size of any tile.
     */
    public static Vector2 getDefaultSize() {
        return defaultSize;
    }

    /**
     * It sets the default size of a tile
     *
     * @param defaultSize The default size of the tile.
     */
    public static void setDefaultSize(Vector2 defaultSize) {
        Tile.defaultSize = defaultSize;
    }

    /**
     * Returns the prefab that the entity from the tile will be created from.
     *
     * @return The prefab variable is being returned.
     */
    public Prefab getPrefab() {
        return prefab;
    }

    /**
     * This function sets the prefab of the entity.
     *
     * @param prefab The prefab to be instantiated.
     */
    public void setPrefab(Prefab prefab) {
        this.prefab = prefab;
    }
}
