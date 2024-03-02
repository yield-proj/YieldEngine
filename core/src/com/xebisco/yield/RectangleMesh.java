package com.xebisco.yield;

import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Renderer;

public class RectangleMesh extends AbstractRenderable {
    private Color color = new Color(1, 1, 1, 1);

    private Vector2D size = new Vector2D(100, 100);

    public RectangleMesh() {
        super(Form.SQUARE);
        paint().setHasImage(false);
    }

    @Override
    public void render(Renderer renderer) {
        paint().setColor(color);
        paint().setRectSize(size);
        super.render(renderer);
    }

    public Color color() {
        return color;
    }

    public RectangleMesh setColor(Color color) {
        this.color = color;
        return this;
    }

    public Vector2D size() {
        return size;
    }

    public RectangleMesh setSize(Vector2D size) {
        this.size = size;
        return this;
    }
}
