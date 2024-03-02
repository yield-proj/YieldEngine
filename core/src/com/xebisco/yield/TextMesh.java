package com.xebisco.yield;

import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Renderer;

public class TextMesh extends AbstractRenderable {

    private String contents = "Sample Text";
    private Color color = new Color(1, 1, 1, 1);

    public TextMesh() {
        super(Form.TEXT);
    }

    @Override
    public void render(Renderer renderer) {
        paint().setColor(color);
        paint().setText(contents);
        super.render(renderer);
    }

    @Override
    public void onStart() {
        if(paint().font() == null)
            paint().setFont(application().defaultFont());
    }

    public String contents() {
        return contents;
    }

    public TextMesh setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Color color() {
        return color;
    }

    public TextMesh setColor(Color color) {
        this.color = color;
        return this;
    }
}
