package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.core.graphics.IPainter;
import com.xebisco.yieldengine.core.graphics.yldg1.Paint;
import com.xebisco.yieldengine.utils.Color4f;
import com.xebisco.yieldengine.utils.ColorUtils;
import com.xebisco.yieldengine.utils.Editable;
import com.xebisco.yieldengine.utils.Visible;
import org.joml.Vector2f;

public class Rectangle extends Component implements IPainter {
    @Visible
    @Editable
    private Color4f color = ColorUtils.argb(0xFFFFFFFF);
    @Visible
    @Editable
    private Vector2f size = new Vector2f(100, 100);
    @Visible
    @Editable
    private boolean filled = true;

    protected final Paint paint = new Paint();

    public Rectangle() {
    }

    public Rectangle(Vector2f size) {
        this.size = size;
    }

    public Rectangle(Color4f color, Vector2f size) {
        this.color = color;
        this.size = size;
    }

    @Override
    public void onPaint(Graphics g) {
        g.getG1().drawRect(getSize().x(), getSize().y(), paint.setTransform(getWorldTransform()).setColor(color));
    }

    public Color4f getColor() {
        return color;
    }

    public Rectangle setColor(Color4f color) {
        this.color = color;
        return this;
    }

    public Vector2f getSize() {
        return size;
    }

    public Rectangle setSize(Vector2f size) {
        this.size = size;
        return this;
    }

    public boolean isFilled() {
        return filled;
    }

    public Rectangle setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    protected Paint getPaint() {
        return paint;
    }
}
