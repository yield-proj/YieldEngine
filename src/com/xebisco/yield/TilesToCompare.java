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
 * It's a class that holds the tiles that are to be compared to the current tile
 */
public class TilesToCompare {

    private TileGen  left, onlyTopLeft, top, onlyTopRight, right, onlyRightBottom, bottom, onlyBottomLeft;

    /**
     * This function returns the left TileGen object.
     *
     * @return The left tile.
     */
    public TileGen getLeft() {
        return left;
    }

    /**
     * This function sets the left tile to the tile passed in.
     *
     * @param left The tile to the left of this tile.
     */
    public void setLeft(TileGen left) {
        this.left = left;
    }

    /**
     * This function returns the onlyTopLeft variable.
     *
     * @return The onlyTopLeft variable is being returned.
     */
    public TileGen getOnlyTopLeft() {
        return onlyTopLeft;
    }

    /**
     * This function sets the onlyTopLeft variable to the value of the onlyTopLeft parameter.
     *
     * @param onlyTopLeft This is the tile that will be used for the top left corner of the map.
     *
     */
    public void setOnlyTopLeft(TileGen onlyTopLeft) {
        this.onlyTopLeft = onlyTopLeft;
    }

    public TileGen getTop() {
        return top;
    }

    /**
     * This function sets the top tile of the current tile to the tile passed in as a parameter.
     *
     * @param top The tile that is above this tile.
     */
    public void setTop(TileGen top) {
        this.top = top;
    }

    /**
     * This function returns the onlyTopRight variable.
     *
     * @return The onlyTopRight variable is being returned.
     */
    public TileGen getOnlyTopRight() {
        return onlyTopRight;
    }

    /**
     * This function sets the onlyTopRight variable to the value of the onlyTopRight parameter.
     *
     * @param onlyTopRight The tile that will be used for the top right corner of the tile.
     */
    public void setOnlyTopRight(TileGen onlyTopRight) {
        this.onlyTopRight = onlyTopRight;
    }

    /**
     * This function returns the right tile generator.
     *
     * @return The right tile.
     */
    public TileGen getRight() {
        return right;
    }

    /**
     * This function sets the right tile to the tile passed in.
     *
     * @param right The tile to the right of this tile.
     */
    public void setRight(TileGen right) {
        this.right = right;
    }

    /**
     * This function returns the onlyRightBottom variable.
     *
     * @return The onlyRightBottom variable is being returned.
     */
    public TileGen getOnlyRightBottom() {
        return onlyRightBottom;
    }

    /**
     * This function sets the onlyRightBottom variable to the value of the onlyRightBottom parameter.
     *
     * @param onlyRightBottom This is the tile that will be used for the bottom right corner of the map.
     */
    public void setOnlyRightBottom(TileGen onlyRightBottom) {
        this.onlyRightBottom = onlyRightBottom;
    }

    /**
     * This function returns the bottom tile generator.
     *
     * @return The bottom tile.
     */
    public TileGen getBottom() {
        return bottom;
    }

    /**
     * This function sets the bottom tile of the current tile to the tile passed in as a parameter.
     *
     * @param bottom The tile that is below this tile.
     */
    public void setBottom(TileGen bottom) {
        this.bottom = bottom;
    }

    /**
     * This function returns the onlyBottomLeft variable.
     *
     * @return The onlyBottomLeft variable is being returned.
     */
    public TileGen getOnlyBottomLeft() {
        return onlyBottomLeft;
    }

    /**
     * This function sets the onlyBottomLeft variable to the value of the onlyBottomLeft parameter.
     *
     * @param onlyBottomLeft This is the tile that will be used if the tile is only on the bottom left of the map.
     */
    public void setOnlyBottomLeft(TileGen onlyBottomLeft) {
        this.onlyBottomLeft = onlyBottomLeft;
    }
}
