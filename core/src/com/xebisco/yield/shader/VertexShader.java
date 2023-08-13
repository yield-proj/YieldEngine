package com.xebisco.yield.shader;

import com.aparapi.Kernel;

public abstract class VertexShader extends Kernel {

    public float[] inXPos, inYPos, outXPos, outYPos;

    public VertexShader() {
        setExplicit(true);
    }

    public void putAll() {
        put(inXPos);
        put(inYPos);
        put(outXPos);
        put(outYPos);
    }

    public void getOut() {
        get(outXPos);
        get(outYPos);
    }
}
