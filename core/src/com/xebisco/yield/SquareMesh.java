package com.xebisco.yield;

import com.xebisco.yield.editor.utils.Visible;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Renderer;

public class SquareMesh extends AbstractRenderable {
    @Visible
    private Color color = new Color(1, 1, 1, 1);

    @Visible
    private Vector2D size = new Vector2D(100, 100);

    public SquareMesh() {
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

    public SquareMesh setColor(Color color) {
        this.color = color;
        return this;
    }

    public Vector2D size() {
        return size;
    }

    public SquareMesh setSize(Vector2D size) {
        this.size = size;
        return this;
    }
}
