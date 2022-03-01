package com.xebisco.yield.components;

import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;

import java.awt.*;

public class Text extends Shape {
    private String contents = "Sample Text";
    private Font font = defaultFont;
    private static Font defaultFont = new Font("arial", Font.PLAIN, 30);

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    @Override
    public void parameters(YldGraphics graphics) {

    }

    @Override
    public void previous(YldGraphics graphics)
    {
        getObj().font = font;
    }

    @Override
    public void process(Obj obj) {
        obj.x = (int) (getEntity().getTransform().position.x) - obj.x2 / 2;
        obj.y = (int) (getEntity().getTransform().position.y) - obj.y2 / 2;
        obj.value = contents;
        obj.type = Obj.ShapeType.TEXT;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Font getFont()
    {
        return font;
    }

    public void setFont(Font font)
    {
        this.font = font;
    }

    public static Font getDefaultFont()
    {
        return defaultFont;
    }

    public static void setDefaultFont(Font defaultFont)
    {
        Text.defaultFont = defaultFont;
    }
}
