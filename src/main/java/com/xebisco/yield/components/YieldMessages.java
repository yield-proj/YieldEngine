package com.xebisco.yield.components;

import com.xebisco.yield.*;
import com.xebisco.yield.Color;
import com.xebisco.yield.Component;

import java.awt.Font;

public class YieldMessages extends Component
{
    public static Color bgColor = Colors.DARK_GRAY, textColor = Colors.WHITE;
    public static boolean show;
    public static Font yieldFont = new Font("Verdana", Font.BOLD, 20), textFont = new Font("arial", Font.PLAIN, 10);
    public Obj bg, yieldText;
    public Obj[] msg = new Obj[25];
    public static int maxWidth = 200;

    @Override
    public void create()
    {
        graphics.setColor(bgColor);
        graphics.getColor().setA(.5f);
        bg = graphics.rect(0, 0, maxWidth, 100);
        graphics.setFont(yieldFont);
        graphics.setColor(textColor);
        yieldText = graphics.text("Yield " + Yld.VERSION);
        graphics.setFont(textFont);
        for (int i = 0; i < msg.length; i++)
        {
            msg[i] = graphics.text("", 10, i * 30 + 40);
        }
    }

    @Override
    public void update(float delta)
    {
        for (int i = 0; i < msg.length; i++)
        {
            msg[i].active = show;
            if (Yld.MESSAGES.size() > i)
            {
                msg[i].value = Yld.MESSAGES.get(i);
            }
        }
        yieldText.active = show;
        bg.y2 = scene.getGame().getWindow().getWindowG().getHeight();
        bg.x2 = maxWidth;
        bg.active = show;
        if (Yld.MESSAGES.size() > msg.length)
        {
            Yld.MESSAGES.remove(Yld.MESSAGES.size() - 1);
        }
    }
}
