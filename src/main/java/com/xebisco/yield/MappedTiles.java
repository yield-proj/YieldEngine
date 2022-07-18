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

public class MappedTiles {

    private TileGen target, middle, left, leftAndTop, onlyTopLeft, top, topAndRight, onlyTopRight, right, rightAndBottom, onlyRightBottom, bottom, bottomAndLeft, onlyBottomLeft, topAndDown, rightAndLeft, lessRight, lessTop, lessLeft, lessDown, all;

    private int targetLayer = -1;

    public MappedTiles(Integer targetLayer, TileGen target, TileGen middle, TileGen left, TileGen leftAndTop, TileGen onlyTopLeft, TileGen top, TileGen topAndRight, TileGen onlyTopRight, TileGen right, TileGen rightAndBottom, TileGen onlyRightBottom, TileGen bottom, TileGen bottomAndLeft, TileGen onlyBottomLeft, TileGen topAndDown, TileGen rightAndLeft, TileGen lessRight, TileGen lessTop, TileGen lessLeft, TileGen lessDown, TileGen all) {
        if(targetLayer != null)
            this.targetLayer = targetLayer;
        this.target = target;
        this.middle = middle;
        this.left = left;
        this.leftAndTop = leftAndTop;
        this.onlyTopLeft = onlyTopLeft;
        this.top = top;
        this.topAndRight = topAndRight;
        this.onlyTopRight = onlyTopRight;
        this.right = right;
        this.rightAndBottom = rightAndBottom;
        this.onlyRightBottom = onlyRightBottom;
        this.bottom = bottom;
        this.bottomAndLeft = bottomAndLeft;
        this.onlyBottomLeft = onlyBottomLeft;
        this.topAndDown = topAndDown;
        this.rightAndLeft = rightAndLeft;
        this.lessRight = lessRight;
        this.lessTop = lessTop;
        this.lessLeft = lessLeft;
        this.lessDown = lessDown;
        this.all = all;
    }

    public int getTargetLayer() {
        return targetLayer;
    }

    public void setTargetLayer(int targetLayer) {
        this.targetLayer = targetLayer;
    }

    public TileGen getTarget() {
        return target;
    }

    public void setTarget(TileGen target) {
        this.target = target;
    }

    public TileGen getMiddle() {
        return middle;
    }

    public void setMiddle(TileGen middle) {
        this.middle = middle;
    }

    public TileGen getLeft() {
        return left;
    }

    public void setLeft(TileGen left) {
        this.left = left;
    }

    public TileGen getLeftAndTop() {
        return leftAndTop;
    }

    public void setLeftAndTop(TileGen leftAndTop) {
        this.leftAndTop = leftAndTop;
    }

    public TileGen getOnlyTopLeft() {
        return onlyTopLeft;
    }

    public void setOnlyTopLeft(TileGen onlyTopLeft) {
        this.onlyTopLeft = onlyTopLeft;
    }

    public TileGen getTop() {
        return top;
    }

    public void setTop(TileGen top) {
        this.top = top;
    }

    public TileGen getTopAndRight() {
        return topAndRight;
    }

    public void setTopAndRight(TileGen topAndRight) {
        this.topAndRight = topAndRight;
    }

    public TileGen getOnlyTopRight() {
        return onlyTopRight;
    }

    public void setOnlyTopRight(TileGen onlyTopRight) {
        this.onlyTopRight = onlyTopRight;
    }

    public TileGen getRight() {
        return right;
    }

    public void setRight(TileGen right) {
        this.right = right;
    }

    public TileGen getRightAndBottom() {
        return rightAndBottom;
    }

    public void setRightAndBottom(TileGen rightAndBottom) {
        this.rightAndBottom = rightAndBottom;
    }

    public TileGen getOnlyRightBottom() {
        return onlyRightBottom;
    }

    public void setOnlyRightBottom(TileGen onlyRightBottom) {
        this.onlyRightBottom = onlyRightBottom;
    }

    public TileGen getBottom() {
        return bottom;
    }

    public void setBottom(TileGen bottom) {
        this.bottom = bottom;
    }

    public TileGen getBottomAndLeft() {
        return bottomAndLeft;
    }

    public void setBottomAndLeft(TileGen bottomAndLeft) {
        this.bottomAndLeft = bottomAndLeft;
    }

    public TileGen getOnlyBottomLeft() {
        return onlyBottomLeft;
    }

    public void setOnlyBottomLeft(TileGen onlyBottomLeft) {
        this.onlyBottomLeft = onlyBottomLeft;
    }

    public TileGen getTopAndDown() {
        return topAndDown;
    }

    public void setTopAndDown(TileGen topAndDown) {
        this.topAndDown = topAndDown;
    }

    public TileGen getRightAndLeft() {
        return rightAndLeft;
    }

    public void setRightAndLeft(TileGen rightAndLeft) {
        this.rightAndLeft = rightAndLeft;
    }

    public TileGen getLessRight() {
        return lessRight;
    }

    public void setLessRight(TileGen lessRight) {
        this.lessRight = lessRight;
    }

    public TileGen getLessTop() {
        return lessTop;
    }

    public void setLessTop(TileGen lessTop) {
        this.lessTop = lessTop;
    }

    public TileGen getLessLeft() {
        return lessLeft;
    }

    public void setLessLeft(TileGen lessLeft) {
        this.lessLeft = lessLeft;
    }

    public TileGen getLessDown() {
        return lessDown;
    }

    public void setLessDown(TileGen lessDown) {
        this.lessDown = lessDown;
    }

    public TileGen getAll() {
        return all;
    }

    public void setAll(TileGen all) {
        this.all = all;
    }
}
