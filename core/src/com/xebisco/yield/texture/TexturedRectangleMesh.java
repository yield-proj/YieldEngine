package com.xebisco.yield.texture;

import com.xebisco.yield.AbstractTexture;
import com.xebisco.yield.RectangleMesh;
import com.xebisco.yield.rendering.Renderer;

public class TexturedRectangleMesh extends RectangleMesh {

    private AbstractTexture texture;

    public TexturedRectangleMesh() {
        paint().setHasImage(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(texture == null) texture = application().defaultTexture();
    }

    @Override
    public void render(Renderer renderer) {
        paint().setDrawObj(texture.imageRef());
        super.render(renderer);
    }

    public AbstractTexture texture() {
        return texture;
    }

    public TexturedRectangleMesh setTexture(AbstractTexture texture) {
        this.texture = texture;
        return this;
    }
}
