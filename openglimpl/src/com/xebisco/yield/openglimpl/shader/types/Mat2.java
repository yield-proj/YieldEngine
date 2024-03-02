package com.xebisco.yield.openglimpl.shader.types;

import org.joml.Matrix2fc;

public class Mat2 extends AbstractMat {
    public Mat2(float a11, float a12, float a21, float a22) {
        super(new float[][]{new float[]{a11, a12}, new float[]{a21, a22}});
    }

    public Mat2(Matrix2fc m) {
        this(m.m00(), m.m01(), m.m10(), m.m11());
    }

    public Mat2(Vec2 column1, Vec2 column2) {
        this(column1.x(), column1.y(), column2.x(), column2.y());
    }
}
