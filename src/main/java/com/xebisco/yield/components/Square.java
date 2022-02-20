package com.xebisco.yield.components;

public class Square extends Rectangle {

    private float size = 64;

    @Override
    public void update(float delta) {
        super.update(delta);
        setWidth(size);
        setHeight(size);
    }

    public float getSize() {
        return size;
    }


    public void setSize(float size) {
        this.size = size;
    }
}
