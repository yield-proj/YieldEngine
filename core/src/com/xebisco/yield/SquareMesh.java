package com.xebisco.yield;

import com.xebisco.yield.rendering.Form;

public class SquareMesh extends AbstractRenderable {
    public SquareMesh() {
        super(Form.SQUARE);
        paint().setHasImage(false);
    }
}
