package com.xebisco.yieldengine.core.graphics.yldg1;

import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.camera.ICamera;
import com.xebisco.yieldengine.core.io.text.Font;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.utils.Color4f;

public class Paint {
    private Color4f color;
    private float strokeSize;
    private Font font;
    private Texture texture;
    private Transform transform;
    private ICamera camera = Global.getCurrentScene().getCamera();

    public Color4f getColor() {
        return color;
    }

    public Paint setColor(Color4f color) {
        this.color = color;
        return this;
    }

    public float getStrokeSize() {
        return strokeSize;
    }

    public Paint setStrokeSize(float strokeSize) {
        this.strokeSize = strokeSize;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public Paint setFont(Font font) {
        this.font = font;
        return this;
    }

    public Texture getTexture() {
        return texture;
    }

    public Paint setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Transform getTransform() {
        return transform;
    }

    public Paint setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public ICamera getCamera() {
        return camera;
    }

    public Paint setCamera(ICamera camera) {
        this.camera = camera;
        return this;
    }
}
