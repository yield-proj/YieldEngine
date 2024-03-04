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
import com.xebisco.yield.openglimpl.shader.types.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Utils {
    public static final Vec2[] SQUARE_POSITIONS = new Vec2[]{
        new Vec2(-1, 1),
                new Vec2(1, 1),
                new Vec2(1, -1),
                new Vec2(-1, -1)
    }, SQUARE_TEX_COORDS = new Vec2[]{
            new Vec2(0, 0),
            new Vec2(1, 0),
            new Vec2(1, 1),
            new Vec2(0, 1)
    };

    public static final int[] SQUARE_INDICES = new int[]{0, 1, 2, 2, 3, 0};

    public static Mat4 transformationMatrix(Transform2D transform) {
        return transformationMatrix(transform, new Vector2D(1, 1));
    }

    public static Mat4 transformationMatrix(Transform2D transform, Vector2D size) {
        return new Mat4(new Matrix4f().identity().translate(new Vector3f((float) transform.position().x(), (float) transform.position().y(), 0)).rotateZ((float) Math.toRadians(transform.zRotation())).scaleXY((float) (transform.scale().x() * size.width()), (float) (transform.scale().y() * size.height())));
    }

    public static Mat4 viewMatrix(Transform2D camera, Vector2D viewportSize) {
        return new Mat4(GLUtils.orthoViewMatrix(camera, viewportSize));
    }

    public static boolean isClassCompatibleShaderType(Class<?> c) {
        return (c.equals(Mat2.class) ||
                c.equals(Mat3.class) ||
                c.equals(Mat4.class) ||
                c.equals(Vec2.class) ||
                c.equals(Vec3.class) ||
                c.equals(Vec4.class) ||
                c.equals(Sampler2D.class) ||
                c.equals(byte.class) || c.equals(Byte.class) ||
                c.equals(short.class) || c.equals(Short.class) ||
                c.equals(int.class) || c.equals(Integer.class) ||
                c.equals(long.class) || c.equals(Long.class) ||
                c.equals(float.class) || c.equals(Float.class));
    }

    public static boolean isClassCompatibleAttribShaderType(Class<?> c) {
        return (c.equals(Vec2[].class) || c.equals(Vec3[].class) || c.equals(Vec4[].class));
    }
}
