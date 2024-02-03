package com.xebisco.yield.texture;

import com.xebisco.yield.SquareMesh;
import com.xebisco.yield.editor.utils.AffectsEditorEntitySize;
import com.xebisco.yield.editor.utils.Visible;
import com.xebisco.yield.rendering.Renderer;

@AffectsEditorEntitySize
public class TexturedSquareMesh extends SquareMesh {

    @Visible
    private Texture texture;

    public TexturedSquareMesh() {
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

    public Texture texture() {
        return texture;
    }

    public TexturedSquareMesh setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }
}
