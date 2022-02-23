package com.xebisco.yield;

import com.xebisco.yield.components.YieldMessages;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public final class Yld
{

    public static final String VERSION = "4 dev 4";
    public static final ArrayList<String> MESSAGES = new ArrayList<>();
    public static final Random RAND = new Random();

    public static void message(Object msg)
    {
        msg = "(" + new Date() + ") " + msg;
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        int textwidth = (int) (YieldMessages.textFont.getStringBounds(msg.toString(), frc).getWidth()) + 20;
        if (textwidth > YieldMessages.maxWidth)
        {
            YieldMessages.maxWidth = textwidth;
        }
        YieldMessages.show = true;
        MESSAGES.add(0, msg.toString());
        System.out.println(msg);
    }

    public static void log(Object msg)
    {
        System.out.println(msg);
    }

    public static void exit()
    {
        System.exit(0);
    }

}
