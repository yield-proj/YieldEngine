package com.xebisco.yield.openglimpl.shader.types;

import org.joml.Matrix3fc;

public class Mat3 extends AbstractMat {
    public Mat3(
            float a11, float a12, float a13,
            float a21, float a22, float a23,
            float a31, float a32, float a33
    ) {
        super(new float[][]{new float[]{a11, a12, a13}, new float[]{a21, a22, a23}, new float[]{a31, a32, a33}});
    }

    public Mat3(Matrix3fc m) {
        this(m.m00(), m.m01(), m.m02(), m.m10(), m.m11(), m.m12(), m.m20(), m.m21(), m.m22());
    }
}
