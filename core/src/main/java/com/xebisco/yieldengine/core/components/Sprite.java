package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.annotations.Editable;
import com.xebisco.yieldengine.annotations.Visible;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector2f;

import java.io.Serializable;

public class Sprite extends Rectangle {
    @Visible
    @Editable
    private Texture texture = IO.getInstance().getDefaultTexture();
    @Visible
    @Editable
    private Vector2f offset = new Vector2f();

    public Sprite() {
    }

    public Sprite(Vector2f size) {
        super(size);
    }

    public Sprite(int color, Vector2f size) {
        super(color, size);
    }

    public Sprite(Texture texture) {
        this.texture = texture;
    }

    public Sprite(Vector2f size, Texture texture) {
        super(size);
        this.texture = texture;
    }

    public Sprite(int color, Vector2f size, Texture texture) {
        super(color, size);
        this.texture = texture;
    }

    @Override
    public void onLateUpdate() {
        Transform t = getEntity().getNewWorldTransform();
        t.translate(offset);
        Render render = Render.getInstance();
        render.getInstructionsList().add(
                new DrawInstruction()
                        .setType(DrawInstruction.DrawInstructionType.DRAW_IMAGE.getTypeString())
                        .setDrawObjects(
                                new Serializable[]{
                                        getColor(),
                                        getSize(),
                                        texture.getImageReference()
                                }
                        )
                        .setCamera(Global.getCurrentScene().getCamera())
                        .setTransform(t)
        );
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public Sprite setOffset(Vector2f offset) {
        this.offset = offset;
        return this;
    }
}
