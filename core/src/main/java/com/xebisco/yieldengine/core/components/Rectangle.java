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

public class Rectangle extends Component {
    @Visible
    @Editable
    @Color
    private int color = 0xFFFFFFFF;
    @Visible
    @Editable
    private Vector2f size = new Vector2f(100, 100);

    public Rectangle() {
    }

    public Rectangle(Vector2f size) {
        this.size = size;
    }

    public Rectangle(int color, Vector2f size) {
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

    public int getColor() {
        return color;
    }

    public Rectangle setColor(int color) {
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
