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

import com.xebisco.yield.render.Renderable;
import com.xebisco.yield.render.RenderableType;

import java.util.Set;

/**
 * It's a shape that draws an oval
 */
public class Oval extends Shape {
    @Override
    public void render(Set<Renderable> renderables) {
        super.render(renderables);
        getRenderable().setType(RenderableType.OVAL);
    }

    public Oval(Vector2 size) {
        setSize(size);
    }

    public Oval(Vector2 size, Color color) {
        setSize(size);
        setColor(color);
    }

    public Oval() {
    }

    @Override
    public boolean colliding(float x, float y) {
        Transform t = getTransform();
        return x >= t.position.x - getSize().x * t.scale.x / 2f && x <= t.position.x + getSize().x * t.scale.y / 2f &&
                y >= t.position.y - getSize().y * t.scale.y / 2f && y <= t.position.y + getSize().y * t.scale.y / 2f;
    }
}
