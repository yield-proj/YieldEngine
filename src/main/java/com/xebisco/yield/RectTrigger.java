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

public class RectTrigger extends YldScript {
    private Vector2 offset = new Vector2(), size = new Vector2(64, 64);
    private boolean transmit = true;

    public boolean colliding(Vector2 pos, Vector2 size, Vector2 scale) {
        Transform t = getTransform();
        Vector2 position = t.position.sum(offset);
        final float r = position.x + this.size.x * t.scale.x / 2f, u = position.y - this.size.y * t.scale.y / 2f, l = position.x - this.size.x * t.scale.x * t.scale.x / 2f, d = position.y + this.size.y * t.scale.y / 2f,
                br = pos.x + size.x * scale.x / 2f, bl = pos.x - size.x * scale.x / 2f, bu = pos.y - size.y * scale.y / 2f, bd = pos.y + size.y * scale.y / 2f;
        return r >= bl && l <= br &&
                d >= bu && u <= bd;
    }

    public void setSize(Shape shape) {
        size = shape.getSize();
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public boolean isTransmit() {
        return transmit;
    }

    public void setTransmit(boolean transmit) {
        this.transmit = transmit;
    }
}
