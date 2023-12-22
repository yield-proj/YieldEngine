package com.xebisco.yield;

import com.xebisco.yield.rendering.Form;

public class TextMesh extends AbstractRenderable {
    public TextMesh() {
        super(Form.TEXT);
        paint().setText("Sample Text");
    }

    @Override
    public void onStart() {
        transform().scale().set(.1, .1);
        if(paint().font() == null)
            paint().setFont(application().defaultFont());
    }
}
