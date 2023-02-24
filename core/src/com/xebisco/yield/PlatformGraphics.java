package com.xebisco.yield;

public interface PlatformGraphics {
    void init(PlatformInit platformInit);
    void draw(DrawInstruction drawInstruction);
    void dispose();
}
