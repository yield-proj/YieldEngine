package com.xebisco.yield.openglimpl.shader.types;

public class Sampler2D {
    private int id;

    public Sampler2D(int id) {
        this.id = id;
    }

    public Sampler2D() {
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
