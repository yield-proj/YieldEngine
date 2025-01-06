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

public class Line extends Component implements IPainter {
    @Visible
    @Editable
    private Vector2f point1 = new Vector2f(-50, -50), point2 = new Vector2f(50, 50);
    @Visible
    @Editable
    private float thickness = 4f;
    @Visible
    @Editable
    private Color4f color = ColorUtils.argb(0xFFFFFFFF);

    protected final Paint paint = new Paint();

    public Line() {
    }

    public Line(Vector2f point1, Vector2f point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Line(Vector2f point1, Vector2f point2, Color4f color) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
    }

    public Line(Vector2f point1, Vector2f point2, float thickness) {
        this.point1 = point1;
        this.point2 = point2;
        this.thickness = thickness;
    }

    public Line(Vector2f point1, Vector2f point2, float thickness, Color4f color) {
        this.point1 = point1;
        this.point2 = point2;
        this.thickness = thickness;
        this.color = color;
    }

    @Override
    public void onPaint(Graphics g) {
        g.getG1().drawLine(point1.x, point1.y, point2.x, point2.y, paint.setTransform(getWorldTransform()).setColor(color).setStrokeSize(thickness));
    }

    public Vector2f getPoint1() {
        return point1;
    }

    public Line setPoint1(Vector2f point1) {
        this.point1 = point1;
        return this;
    }

    public Vector2f getPoint2() {
        return point2;
    }

    public Line setPoint2(Vector2f point2) {
        this.point2 = point2;
        return this;
    }

    public float getThickness() {
        return thickness;
    }

    public Line setThickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public Color4f getColor() {
        return color;
    }

    public Line setColor(Color4f color) {
        this.color = color;
        return this;
    }

    private Paint getPaint() {
        return paint;
    }
}
