package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.core.graphics.yldg1.Paint;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.utils.Color4f;
import com.xebisco.yieldengine.utils.Editable;
import com.xebisco.yieldengine.utils.Visible;
import org.joml.Vector2f;

public class Sprite extends Rectangle {
    @Visible
    @Editable
    private Texture texture = IO.getInstance().getDefaultTexture();

    protected final Paint paint = new Paint();

    public Sprite() {
    }

    public Sprite(Vector2f size) {
        super(size);
    }

    public Sprite(Color4f color, Vector2f size) {
        super(color, size);
    }

    public Sprite(Texture texture) {
        this.texture = texture;
    }

    public Sprite(Vector2f size, Texture texture) {
        super(size);
        this.texture = texture;
    }

    public Sprite(Color4f color, Vector2f size, Texture texture) {
        super(color, size);
        this.texture = texture;
    }

    @Override
    public void onPaint(Graphics g) {
        g.getG1().drawRect(getSize().x(), getSize().y(), paint.setTransform(getWorldTransform()).setColor(getColor()).setTexture(texture));
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }
}
