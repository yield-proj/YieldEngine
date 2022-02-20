package com.xebisco.yield;

public class Animation {

    private final Texture[] frames;
    private final String name;
    private int actFrame = 0, frameDelay = 100;

    public Animation(String name, Texture...frames) {
        this.frames = frames;
        this.name = name;
    }

    public Texture[] getFrames() {
        return frames;
    }

    public int getActFrame() {
        return actFrame;
    }

    public void setActFrame(int actFrame) {
        this.actFrame = actFrame;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public String getName() {
        return name;
    }
}
