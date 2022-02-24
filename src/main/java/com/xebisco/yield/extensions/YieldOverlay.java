package com.xebisco.yield.extensions;

import com.xebisco.yield.View;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldExtension;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class YieldOverlay extends YldExtension
{
    private final java.awt.Color bgColor = new Color(64, 64, 64, 200),
            buttonColor = new Color(67, 67, 67, 255),
            selectedButtonColor = new Color(106, 106, 106, 255);
    private final BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
    private final Image yieldImage = new ImageIcon(Objects.requireNonNull(YieldOverlay.class.getResource("/com/xebisco/yield/assets/yieldlogo.png"))).getImage();
    private final Font font1 = new Font("Verdana", Font.BOLD, 30), font2 = new Font("arial", Font.BOLD, 30);
    private int w, h;
    private final int arc = 40;
    private static boolean show;
    private boolean exitButtonSelected;

    @Override
    public void update(float delta)
    {
        if (show)
        {
            if (View.getActView() != null)
            {
                w = View.getActView().getWidth();
                h = View.getActView().getHeight();
            }
            else
            {
                w = game.getWindow().getWindowG().getWidth();
                h = game.getWindow().getWindowG().getHeight();
            }
            exitButtonSelected = game.getInput().getX() > w - ((w / 1280f) * 250) && game.getInput().getY() > h - ((h / 720f) * 100);
            if (exitButtonSelected && game.getInput().isTouching())
            {
                show = false;
            }
        }
    }

    @Override
    public void render(java.awt.Graphics graphics)
    {
        if (show)
        {
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setBackground(new Color(255, 255, 255, 0));
            g.clearRect(0, 0, image.getWidth(), image.getHeight());
            g.setColor(bgColor);
            //MAIN RECT
            g.fillRoundRect(10, 10, image.getWidth() - 430, 100, arc, arc);
            //MESSAGES RECT
            g.fillRoundRect(10, 120, image.getWidth() - 430, 500, arc, arc);
            //STATS RECT
            g.fillRoundRect(image.getWidth() - 410, 10, image.getWidth() - (image.getWidth() - 410) - 10, 610, arc, arc);
            g.setColor(Color.BLACK);
            //MAIN RECT
            g.drawRoundRect(10, 10, image.getWidth() - 430, 100, arc, arc);
            //MESSAGES RECT
            g.drawRoundRect(10, 120, image.getWidth() - 430, 500, arc, arc);
            //STATS RECT
            g.drawRoundRect(image.getWidth() - 410, 10, image.getWidth() - (image.getWidth() - 410) - 10, 610, arc, arc);

            g.setColor(Color.WHITE);
            g.setFont(font2);

            //STATS

            int x = image.getWidth() - 390;
            g.drawString("FPS: " + game.getScene().getTime().getFps(), x, 80);
            g.drawString("Target FPS: " + game.getScene().getTime().getTargetFPS(), x, 80 + 40);
            g.drawString("TS: " + game.getScene().getTime().getGameHandler().getThread().getState(), x, 80 + 40 * 2);
            g.drawString("Scene: " + game.getScene().getClass().getSimpleName(), x, 80 + 40 * 3);
            g.drawString("JVM Memory: " + Yld.MEMORY + "/" + Yld.MAX_MEMORY + "MB", x, 80 + 40 * 4);
            g.drawString("JRE Version: " + System.getProperty("java.version"), x, 80 + 40 * 5);
            g.drawString("Java Class Version: " + System.getProperty("java.class.version"), x, 80 + 40 * 6);
            g.drawString("Yield BUILD: " + Yld.BUILD, x, 80 + 40 * 7);
            g.drawString("Extensions Active: " + game.getExtensions().size(), x, 80 + 40 * 8);
            g.drawString("Architecture: " + System.getProperty("os.arch"), x, 80 + 40 * 9);
            g.drawString("Act Resolution: " + w + "x" + h, x, 80 + 40 * 10);

            //MESSAGES
            for (int i = 0; i < Yld.MESSAGES.size(); i++)
            {
                g.drawString(Yld.MESSAGES.get(i), 20, 200 + 50 * i);
            }

            g.setFont(font1);


            //TITLE
            g.drawImage(yieldImage, 30, 30, 60, 60, null);
            g.drawString("Messages:", 20, 130 + font1.getSize());
            g.drawString("Yield " + Yld.VERSION + ", on " + System.getProperty("os.name"), 100, 60 + font1.getSize() / 2);


            //EXIT BUTTON

            if (exitButtonSelected)
            {
                g.setColor(selectedButtonColor);
            }
            else
            {
                g.setColor(buttonColor);
            }
            g.fillRoundRect(1020, 630, 250, 80, arc, arc);

            g.setColor(Color.BLACK);
            g.drawRoundRect(1020, 630, 250, 80, arc, arc);

            g.setColor(Color.WHITE);
            g.drawString("Close", 1100, 680);

            g.dispose();
            graphics.drawImage(image, 0, 0, w, h, null);
        }
    }

    public Color getBgColor()
    {
        return bgColor;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public Image getYieldImage()
    {
        return yieldImage;
    }

    public Font getFont1()
    {
        return font1;
    }

    public Font getFont2()
    {
        return font2;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public int getArc()
    {
        return arc;
    }

    public static boolean isShow()
    {
        return show;
    }

    public static void setShow(boolean show)
    {
        YieldOverlay.show = show;
    }
}
