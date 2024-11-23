/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine.core.camera;

import com.xebisco.yieldengine.core.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class OrthoCamera implements ICamera {
    private final Vector2f viewport;
    private final Transform transform = new Transform();

    public OrthoCamera(Vector2f viewport) {
        this.viewport = viewport;
    }

    public OrthoCamera() {
        this(new Vector2f(1280, 720));
    }

    @Override
    public Matrix4f getViewMatrix() {
        float mx = viewport.x() / -2f, my = viewport.y() / -2f;
        return new Matrix4f().identity().ortho2D(mx, mx + viewport.x(), my, my + viewport.y()).mul(transform.getInvertedTransformMatrix());
    }

    public Vector2f getViewport() {
        return viewport;
    }

    public Transform getTransform() {
        return transform;
    }
}
