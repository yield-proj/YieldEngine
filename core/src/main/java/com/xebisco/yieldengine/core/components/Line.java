package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.Serializable;

public class Line extends Component {
    private Vector2f point1 = new Vector2f(-50, -50), point2 = new Vector2f(50, 50);
    private float thickness = 1f;
    private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);

    public Line() {
    }

    public Line(Vector2f point1, Vector2f point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Line(Vector2f point1, Vector2f point2, Vector4f color) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
    }

    public Line(Vector2f point1, Vector2f point2, float thickness) {
        this.point1 = point1;
        this.point2 = point2;
        this.thickness = thickness;
    }

    public Line(Vector2f point1, Vector2f point2, float thickness, Vector4f color) {
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
}
