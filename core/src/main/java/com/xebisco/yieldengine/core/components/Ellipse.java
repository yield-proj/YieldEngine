package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.utils.Color4f;
import org.joml.Vector2f;

public class Ellipse extends Rectangle {
    public Ellipse() {
    }

    public Ellipse(Vector2f size) {
        super(size);
    }

    public Ellipse(Color4f color, Vector2f size) {
        super(color, size);
    }

    @Override
    public void onPaint(Graphics g) {
        g.getG1().drawEllipse(getSize().x(), getSize().y(), paint.setTransform(getWorldTransform()).setColor(getColor()));
    }
}
