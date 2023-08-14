package com.xebisco.yield.shader;

public class DefaultVertexShader extends VertexShader {
    @Override
    public void run() {
        int i = getGlobalId();
        outXPos[i] = (int) inXPos[i];
        outYPos[i] = (int) inYPos[i];
    }
}
