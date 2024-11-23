package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.annotations.Color;
import com.xebisco.yieldengine.annotations.Editable;
import com.xebisco.yieldengine.annotations.Visible;
import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.text.Font;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;

import java.io.Serializable;

public class Text extends Component {
    @Visible
    @Editable
    private String contents = "Sample Text";
    @Visible
    @Editable
    private Font font = IO.getInstance().getDefaultFont();
    @Visible
    @Editable
    @Color
    private int color = 0xFFFFFFFF;

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    public Text(String contents, int color) {
        this.contents = contents;
        this.color = color;
    }

    public Text(String contents, Font font, int color) {
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

    public int getColor() {
        return color;
    }

    public Text setColor(int color) {
        this.color = color;
        return this;
    }
}
