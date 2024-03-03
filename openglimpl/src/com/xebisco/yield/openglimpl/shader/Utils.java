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

package com.xebisco.yield.openglimpl.shader;

import com.xebisco.yield.Transform2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.openglimpl.GLUtils;
import com.xebisco.yield.openglimpl.shader.types.Mat4;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Utils {
    public static Mat4 transformationMatrix(Transform2D transform) {
        return transformationMatrix(transform, new Vector2D(1, 1));
    }

    public static Mat4 transformationMatrix(Transform2D transform, Vector2D size) {
        return new Mat4(new Matrix4f().identity().translate(new Vector3f((float) transform.position().x(), (float) transform.position().y(), 0)).rotateZ((float) Math.toRadians(transform.zRotation())).scaleXY((float) (transform.scale().x() * size.width()), (float) (transform.scale().y() * size.height())));
    }

    public static Mat4 viewMatrix(Transform2D camera, Vector2D viewportSize) {
        return new Mat4(GLUtils.orthoViewMatrix(camera, viewportSize));
    }
}
