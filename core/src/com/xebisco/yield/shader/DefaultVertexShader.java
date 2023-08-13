package com.xebisco.yield.shader;

import com.xebisco.yield.shader.VertexShader;

public class DefaultVertexShader extends VertexShader {
    @Override
    public void run() {
        int i = getGlobalId();
        outXPos[i] = (int) inXPos[i];
        outYPos[i] = (int) inYPos[i];
    }
}
