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

import org.joml.Matrix3fc;

public final class Mat3 extends AbstractMat {
    public Mat3(
            float a11, float a12, float a13,
            float a21, float a22, float a23,
            float a31, float a32, float a33
    ) {
        super(new float[]{a11, a12, a13, a21, a22, a23, a31, a32, a33});
    }

    public Mat3(Matrix3fc m) {
        this(m.m00(), m.m01(), m.m02(), m.m10(), m.m11(), m.m12(), m.m20(), m.m21(), m.m22());
    }
}
