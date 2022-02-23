package com.xebisco.yield.systems;

import com.xebisco.yield.UpdateSystem;

public class YldTimeSystem extends UpdateSystem {
    @Override
    public void update(float delta) {
        scene.getTime().setDelta(delta);
        scene.getTime().setFps(1f / delta);
    }
}
