package com.xebisco.yield;

import com.xebisco.yield.UpdateSystem;

public class YldTimeSystem extends UpdateSystem {
    @Override
    public void update(float delta) {
        scene.getTime().delta = delta;
        scene.getTime().fps = 1 / delta;
    }
}
