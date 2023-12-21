package com.xebisco.yield.rendering;

import com.xebisco.yield.Color;
import com.xebisco.yield.Colors;
import com.xebisco.yield.Transform2D;

public class Paint {
    private Object drawObj;
    private Transform2D transformation;
    private boolean hasImage;
    private Color color = new Color(Colors.WHITE);

    public Object drawObj() {
        return drawObj;
    }

    public Paint setDrawObj(Object drawObj) {
        this.drawObj = drawObj;
        return this;
    }

    public Transform2D transformation() {
        return transformation;
    }

    public Paint setTransformation(Transform2D transformation) {
        this.transformation = transformation;
        return this;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public Paint setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
        return this;
    }

    public Color color() {
        return color;
    }

    public Paint setColor(Color color) {
        this.color = color;
        return this;
    }
}
