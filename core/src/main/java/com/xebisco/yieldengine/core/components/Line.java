package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.annotations.Color;
import com.xebisco.yieldengine.annotations.Editable;
import com.xebisco.yieldengine.annotations.Visible;
import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector2f;

import java.io.Serializable;

public class Line extends Component {
    @Visible
    @Editable
    private Vector2f point1 = new Vector2f(-50, -50), point2 = new Vector2f(50, 50);
    @Visible
    @Editable
    private float thickness = 4f;
    @Visible
    @Editable
    @Color
    private int color = 0xFFFFFFFF;

    public Line() {
    }

    public Line(Vector2f point1, Vector2f point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Line(Vector2f point1, Vector2f point2, int color) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
    }

    public Line(Vector2f point1, Vector2f point2, float thickness) {
        this.point1 = point1;
        this.point2 = point2;
        this.thickness = thickness;
    }

    public Line(Vector2f point1, Vector2f point2, float thickness, int color) {
        this.point1 = point1;
        this.point2 = point2;
        this.thickness = thickness;
        this.color = color;
    }

    @Override
    public void onLateUpdate() {
        Render render = Render.getInstance();
        render.getInstructionsList().add(
                new DrawInstruction()
                        .setType(DrawInstruction.DrawInstructionType.DRAW_ELLIPSE.getTypeString())
                        .setDrawObjects(
                                new Serializable[]{
                                        color,
                                        point1,
                                        point2,
                                        thickness
                                }
                        )
                        .setCamera(Global.getCurrentScene().getCamera())
                        .setTransform(getEntity().getNewWorldTransform())
        );
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

    public int getColor() {
        return color;
    }

    public Line setColor(int color) {
        this.color = color;
        return this;
    }
}
