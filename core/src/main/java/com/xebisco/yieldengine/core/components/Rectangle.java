package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.Serializable;

public class Rectangle extends Component {
    private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
    private Vector2f size = new Vector2f(100, 100);

    public Rectangle() {
    }

    public Rectangle(Vector2f size) {
        this.size = size;
    }

    public Rectangle(Vector4f color, Vector2f size) {
        this.color = color;
        this.size = size;
    }

    @Override
    public void onLateUpdate() {
        Render render = Render.getInstance();
        render.getInstructionsList().add(
                new DrawInstruction()
                        .setType(DrawInstruction.DrawInstructionType.DRAW_RECTANGLE.getTypeString())
                        .setDrawObjects(
                                new Serializable[]{
                                        color,
                                        size
                                }
                        )
                        .setCamera(Global.getCurrentScene().getCamera())
                        .setTransform(getEntity().getNewWorldTransform())
        );
    }

    public Vector4f getColor() {
        return color;
    }

    public Rectangle setColor(Vector4f color) {
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
}
