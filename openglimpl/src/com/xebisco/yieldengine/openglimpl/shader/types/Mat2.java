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

package com.xebisco.yieldengine.openglimpl.shader.types;

import org.joml.Matrix2fc;

public final class Mat2 extends AbstractMat {
    public Mat2(float a11, float a12, float a21, float a22) {
        super(new float[]{a11, a12, a21, a22});
    }

    public Mat2(Matrix2fc m) {
        this(m.m00(), m.m01(), m.m10(), m.m11());
    }

    public Mat2(Vec2 column1, Vec2 column2) {
        this(column1.x(), column1.y(), column2.x(), column2.y());
    }
}
