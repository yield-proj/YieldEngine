package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.text.Font;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.io.Serializable;

public class Text extends Component {
    private String contents = "Sample Text";
    private Font font = IO.getInstance().getDefaultFont();
    private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    public Text(String contents, Vector4f color) {
        this.contents = contents;
        this.color = color;
    }

    public Text(String contents, Font font, Vector4f color) {
        this.contents = contents;
        this.font = font;
        this.color = color;
    }

    @Override
    public void onLateUpdate() {
        Render render = Render.getInstance();
        render.getInstructionsList().add(
                new DrawInstruction()
                        .setType(DrawInstruction.DrawInstructionType.DRAW_TEXT.getTypeString())
                        .setDrawObjects(
                                new Serializable[]{
                                        color,
                                        font.getFontReference(),
                                        contents
                                }
                        )
                        .setCamera(Global.getCurrentScene().getCamera())
                        .setTransform(getEntity().getNewWorldTransform())
        );
    }

    public String getContents() {
        return contents;
    }

    public Text setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public Text setFont(Font font) {
        this.font = font;
        return this;
    }

    public Vector4f getColor() {
        return color;
    }

    public Text setColor(Vector4f color) {
        this.color = color;
        return this;
    }
}
