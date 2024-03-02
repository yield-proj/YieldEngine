package com.xebisco.yield.openglimpl.shader.types;

abstract class AbstractMat {
    private final float[][] data;

    public AbstractMat(float[][] data) {
        this.data = data;
    }

    public float[][] data() {
        return data;
    }
}
