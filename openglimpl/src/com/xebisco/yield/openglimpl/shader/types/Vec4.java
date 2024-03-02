package com.xebisco.yield.openglimpl.shader.types;

public class Vec4 {
    private float x, y, z, w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4() {
    }

    public float x() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float y() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float z() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float w() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }
}
