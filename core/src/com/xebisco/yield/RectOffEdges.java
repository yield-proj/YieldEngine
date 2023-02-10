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
 * It creates a rectangle collider that is made of lines.
 */
public class RectOffEdges extends YldScript {

    private final Vector2 size;
    private final float friction1, friction2, friction3, friction4;

    public RectOffEdges(Vector2 size) {
        this.size = size;
        this.friction1 = 1f;
        this.friction2 = 1f;
        this.friction3 = 1f;
        this.friction4 = 1f;
    }

    public RectOffEdges(Vector2 size, float friction) {
        this.size = size;
        this.friction1 = friction;
        this.friction2 = friction;
        this.friction3 = friction;
        this.friction4 = friction;
    }

    public RectOffEdges(Vector2 size, float friction1, float friction2, float friction3, float friction4) {
        this.size = size;
        this.friction1 = friction1;
        this.friction2 = friction2;
        this.friction3 = friction3;
        this.friction4 = friction4;
    }

    @Override
    public void create() {
        addComponent(new EdgeCollider(new Vector2(size.x / 2f, size.y / 2f), new Vector2(-size.x / 2f, size.y / 2f), friction1));
        addComponent(new EdgeCollider(new Vector2(size.x / 2f, -size.y / 2f), new Vector2(-size.x / 2f, -size.y / 2f), friction2));
        addComponent(new EdgeCollider(new Vector2(-size.x / 2f, size.y / 2f), new Vector2(-size.x / 2f, -size.y / 2f), friction3));
        addComponent(new EdgeCollider(new Vector2(size.x / 2f, size.y / 2f), new Vector2(size.x / 2f, -size.y / 2f), friction4));
        //destroy();
    }

    /**
     * This function returns the size of the object.
     *
     * @return The size of the object.
     */
    public Vector2 getSize() {
        return size;
    }
}
