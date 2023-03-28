/*
 * Copyright [2022-2023] [Xebisco]
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

package com.xebisco.yield.physics;

import com.xebisco.yield.Size2D;
import com.xebisco.yield.VisibleOnInspector;
import org.jbox2d.collision.shapes.*;

public class RectangleCollider extends Collider {
    @VisibleOnInspector
    private Size2D size = new Size2D(100, 100);
    @Override
    public Shape getShape() {
        PolygonShape s = new PolygonShape();
        s.setAsBox((float) (size.getWidth() * Math.abs(getTransform().getScale().getX()) / getApplication().getPhysicsPpm() / 2.0), (float) (size.getHeight() * Math.abs(getTransform().getScale().getY()) / getApplication().getPhysicsPpm() / 2.0));
        return s;
    }

    public Size2D getSize() {
        return size;
    }

    public void setSize(Size2D size) {
        this.size = size;
    }
}
