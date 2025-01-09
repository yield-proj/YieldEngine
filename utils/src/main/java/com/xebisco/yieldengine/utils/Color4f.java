package com.xebisco.yieldengine.utils;

import org.joml.*;

public class Color4f extends Vector4f {
    public Color4f(float red, float green, float blue, float alpha) {
        super(red, green, blue, alpha);
    }

    public Color4f(float red, float green, float blue) {
        super(red, green, blue, 1f);
    }

    public Color4f(Vector4fc v) {
        super(v);
    }

    public Color4f(Vector4dc v) {
        super(v);
    }

    public Color4f(Vector4ic v) {
        super(v);
    }

    public Color4f(Vector3fc v, float alpha) {
        super(v, alpha);
    }

    public Color4f(Vector3fc v) {
        super(v, 1f);
    }

    public Color4f(Vector3ic v, float alpha) {
        super(v, alpha);
    }

    public Color4f(Vector3ic v) {
        super(v, 1f);
    }

    public Color4f() {
    }

    public float getRed() {
        return x();
    }

    public float getGreen() {
        return y();
    }

    public float getBlue() {
        return z();
    }

    public float getAlpha() {
        return w();
    }
}
