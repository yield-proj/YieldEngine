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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * It's a rectangle collider
 */
public class RectCollider extends Collider {
    private Vector2 size = new Vector2(32, 32);

    public RectCollider(Vector2 size) {
        this.size = size;
    }

    public RectCollider() {
    }

    @Override
    public Shape shape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / scene.getPpm() / 2f, size.y / scene.getPpm() / 2f, Yld.toVec2(getOffset().div(scene.getPpm())), 0);
        return shape;
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
}
