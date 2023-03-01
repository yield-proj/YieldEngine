package com.xebisco.yield;

public class TextureRectangle extends Rectangle {

    private Texture texture = Global.getDefaultTexture();

    @Override
    public void onStart() {
        super.onStart();
        getDrawInstruction().setType(DrawInstruction.Type.IMAGE);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        getDrawInstruction().setRenderRef(texture.getImageRef());
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
