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

import org.jbox2d.collision.shapes.Shape;

public class RectOffEdges extends YldScript {

    private Vector2 size;

    public RectOffEdges(Vector2 size) {
        this.size = size;
    }

    @Override
    public void create() {
        addComponent(new EdgeCollider(new Vector2(size.x / 2f, size.y / 2f), new Vector2(-size.x / 2f, size.y / 2f)));
        addComponent(new EdgeCollider(new Vector2(size.x / 2f, -size.y / 2f), new Vector2(-size.x / 2f, -size.y / 2f)));
        addComponent(new EdgeCollider(new Vector2(-size.x / 2f, size.y / 2f), new Vector2(-size.x / 2f, -size.y / 2f)));
        addComponent(new EdgeCollider(new Vector2(size.x / 2f, size.y / 2f), new Vector2(size.x / 2f, -size.y / 2f)));
        //destroy();
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
}
