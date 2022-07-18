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

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getOffSet() {
        return offSet;
    }

    public void setOffSet(Vector2 offSet) {
        this.offSet = offSet;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public static Vector2 getDefaultSize() {
        return defaultSize;
    }

    public static void setDefaultSize(Vector2 defaultSize) {
        Tile.defaultSize = defaultSize;
    }

    public Prefab getPrefab() {
        return prefab;
    }

    public void setPrefab(Prefab prefab) {
        this.prefab = prefab;
    }
}
