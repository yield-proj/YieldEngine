/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

import java.util.Objects;

public class RectCollider extends Component {
    private Vector2 rectPosition = new Vector2();
    private int width = 64, height = 64;

    public Vector2 getRectPosition() {
        return rectPosition;
    }

    public void setRectPosition(Vector2 rectPosition) {
        this.rectPosition = rectPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "RectCollider{" +
                "rectPosition=" + rectPosition +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RectCollider collider = (RectCollider) o;
        return width == collider.width && height == collider.height && Objects.equals(rectPosition, collider.rectPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rectPosition, width, height);
    }
}
