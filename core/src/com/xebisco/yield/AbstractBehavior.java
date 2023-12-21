package com.xebisco.yield;

public abstract class AbstractBehavior implements Behavior {
    private long frames;

    public void tick(ContextTime time) {
        if(frames == 0) {
            onStart();
        }
        onUpdate(time);
        frames++;
    }

    public long frames() {
        return frames;
    }
}
