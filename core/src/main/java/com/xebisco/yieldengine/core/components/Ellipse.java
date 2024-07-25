package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.Serializable;

public class Ellipse extends Rectangle {
    public Ellipse() {
    }

    public Ellipse(Vector2f size) {
        super(size);
    }

    public Ellipse(Vector4f color, Vector2f size) {
        super(color, size);
    }

    @Override
    public void onLateUpdate() {
        Render render = Render.getInstance();
        render.getInstructionsList().add(
                new DrawInstruction()
                        .setType(DrawInstruction.DrawInstructionType.DRAW_ELLIPSE.getTypeString())
                        .setDrawObjects(
                                new Serializable[]{
                                        getColor(),
                                        getSize()
                                }
                        )
                        .setCamera(Global.getCurrentScene().getCamera())
                        .setTransform(getEntity().getNewWorldTransform())
        );
    }
}
