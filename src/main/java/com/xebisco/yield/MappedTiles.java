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

    private Tile target, middle, left, leftAndTop, onlyTopLeft, top, topAndRight, onlyTopRight, right, rightAndBottom, onlyRightBottom, bottom, bottomAndLeft, onlyBottomLeft, topAndDown, rightAndLeft, lessRight, lessTop, lessLeft, lessDown, all;

    public MappedTiles(Tile target, Tile middle, Tile left, Tile leftAndTop, Tile onlyTopLeft, Tile top, Tile topAndRight, Tile onlyTopRight, Tile right, Tile rightAndBottom, Tile onlyRightBottom, Tile bottom, Tile bottomAndLeft, Tile onlyBottomLeft, Tile topAndDown, Tile rightAndLeft, Tile lessRight, Tile lessTop, Tile lessLeft, Tile lessDown, Tile all) {
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

    public Tile getLeft() {
        return left;
    }

    public void setLeft(Tile left) {
        this.left = left;
    }

    public Tile getLeftAndTop() {
        return leftAndTop;
    }

    public void setLeftAndTop(Tile leftAndTop) {
        this.leftAndTop = leftAndTop;
    }

    public Tile getOnlyTopLeft() {
        return onlyTopLeft;
    }

    public void setOnlyTopLeft(Tile onlyTopLeft) {
        this.onlyTopLeft = onlyTopLeft;
    }

    public Tile getTop() {
        return top;
    }

    public void setTop(Tile top) {
        this.top = top;
    }

    public Tile getTopAndRight() {
        return topAndRight;
    }

    public void setTopAndRight(Tile topAndRight) {
        this.topAndRight = topAndRight;
    }

    public Tile getOnlyTopRight() {
        return onlyTopRight;
    }

    public void setOnlyTopRight(Tile onlyTopRight) {
        this.onlyTopRight = onlyTopRight;
    }

    public Tile getRight() {
        return right;
    }

    public void setRight(Tile right) {
        this.right = right;
    }

    public Tile getRightAndBottom() {
        return rightAndBottom;
    }

    public void setRightAndBottom(Tile rightAndBottom) {
        this.rightAndBottom = rightAndBottom;
    }

    public Tile getOnlyRightBottom() {
        return onlyRightBottom;
    }

    public void setOnlyRightBottom(Tile onlyRightBottom) {
        this.onlyRightBottom = onlyRightBottom;
    }

    public Tile getBottom() {
        return bottom;
    }

    public void setBottom(Tile bottom) {
        this.bottom = bottom;
    }

    public Tile getBottomAndLeft() {
        return bottomAndLeft;
    }

    public void setBottomAndLeft(Tile bottomAndLeft) {
        this.bottomAndLeft = bottomAndLeft;
    }

    public Tile getOnlyBottomLeft() {
        return onlyBottomLeft;
    }

    public void setOnlyBottomLeft(Tile onlyBottomLeft) {
        this.onlyBottomLeft = onlyBottomLeft;
    }

    public Tile getTarget() {
        return target;
    }

    public void setTarget(Tile target) {
        this.target = target;
    }

    public Tile getMiddle() {
        return middle;
    }

    public void setMiddle(Tile middle) {
        this.middle = middle;
    }

    public Tile getTopAndDown() {
        return topAndDown;
    }

    public void setTopAndDown(Tile topAndDown) {
        this.topAndDown = topAndDown;
    }

    public Tile getRightAndLeft() {
        return rightAndLeft;
    }

    public void setRightAndLeft(Tile rightAndLeft) {
        this.rightAndLeft = rightAndLeft;
    }

    public Tile getLessRight() {
        return lessRight;
    }

    public void setLessRight(Tile lessRight) {
        this.lessRight = lessRight;
    }

    public Tile getLessTop() {
        return lessTop;
    }

    public void setLessTop(Tile lessTop) {
        this.lessTop = lessTop;
    }

    public Tile getLessLeft() {
        return lessLeft;
    }

    public void setLessLeft(Tile lessLeft) {
        this.lessLeft = lessLeft;
    }

    public Tile getLessDown() {
        return lessDown;
    }

    public void setLessDown(Tile lessDown) {
        this.lessDown = lessDown;
    }

    public Tile getAll() {
        return all;
    }

    public void setAll(Tile all) {
        this.all = all;
    }
}
