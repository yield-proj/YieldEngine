package com.xebisco.yield.components;

import com.xebisco.yield.Obj;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGraphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;

import java.awt.*;

public class Text extends Shape
{
    private String contents = "Sample Text";
    private Font font = defaultFont;
    private static Font defaultFont = new Font("arial", Font.PLAIN, 30);
    private org.newdawn.slick.Font slickFont;

    public Text()
    {
    }

    public Text(String contents)
    {
        this.contents = contents;
    }

    @Override
    public void parameters(YldGraphics graphics)
    {

    }

    @Override
    public void previous(YldGraphics graphics)
    {
        getObj().font = font;
        getObj().type = Obj.ShapeType.TEXT;
    }

    @Override
    public void process(Obj obj)
    {
        obj.x = (int) (getEntity().getTransform().position.x);
        obj.y = (int) (getEntity().getTransform().position.y);
        obj.value = contents;
        obj.slickFont = slickFont;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public Font getFont()
    {
        return font;
    }

    public void setFont(Font font)
    {
        this.font = font;
        if (game.getSlickApp() != null)
            setSlickFont(new TrueTypeFont(font, false));
    }

    public org.newdawn.slick.Font getSlickFont()
    {
        return slickFont;
    }

    public void setSlickFont(org.newdawn.slick.Font slickFont)
    {

        this.slickFont = slickFont;
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
