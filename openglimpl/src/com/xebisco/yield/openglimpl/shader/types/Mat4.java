package com.xebisco.yield.openglimpl.shader.types;

import org.joml.Matrix4fc;

public class Mat4 extends AbstractMat {
    public Mat4(
            float a11, float a12, float a13, float a14,
            float a21, float a22, float a23, float a24,
            float a31, float a32, float a33, float a34,
            float a41, float a42, float a43, float a44
    ) {
        super(new float[][]{new float[]{a11, a12, a13, a14}, new float[]{a21, a22, a23, a24}, new float[]{a31, a32, a33, a34}, new float[]{a41, a42, a43, a44}});
    }

    public Mat4(Matrix4fc m) {
        this(m.m00(), m.m01(), m.m02(), m.m03(), m.m10(), m.m11(), m.m12(), m.m13(), m.m20(), m.m21(), m.m22(), m.m23(), m.m30(), m.m31(), m.m32(), m.m33());
    }
}
