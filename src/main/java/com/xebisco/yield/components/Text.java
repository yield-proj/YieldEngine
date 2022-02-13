package com.xebisco.yield.components;

import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;

import java.awt.*;

public class Text extends Shape {
    private String contents = "Sample Text";
    private Font font;

    private Font previous;

    @Override
    public void parameters(YldGraphics graphics) {
        previous = graphics.getFont();
        graphics.setFont(font);
    }

    @Override
    public void previous(YldGraphics graphics) {
        graphics.setFont(previous);
    }

    @Override
    public void process(Obj obj) {
        obj.x = (int) (getEntity().getTransform().position.x);
        obj.y = (int) (getEntity().getTransform().position.y);
        obj.value = contents;
        obj.type = Obj.ShapeType.TEXT;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
