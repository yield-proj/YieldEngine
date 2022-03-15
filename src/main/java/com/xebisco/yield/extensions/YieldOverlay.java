/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.extensions;

import com.xebisco.yield.View;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldExtension;
import com.xebisco.yield.graphics.AWTGraphics;
import com.xebisco.yield.graphics.SampleGraphics;
import com.xebisco.yield.slick.SlickGraphics;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class YieldOverlay extends YldExtension
{
    private final Color bgColor = new Color(64, 64, 64),
            buttonColor = new Color(67, 67, 67),
            selectedButtonColor = new Color(106, 106, 106);
    private final BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
    private final Image yieldImage = new ImageIcon(Objects.requireNonNull(YieldOverlay.class.getResource("/com/xebisco/yield/assets/yieldlogo.png"))).getImage();
    private final Font font1 = new Font("Verdana", Font.BOLD, 30), font2 = new Font("arial", Font.BOLD, 30);
    private int w, h;
    private static int offsetAnimation;
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
            //exitButtonSelected = game.getInput().getX() > w - ((w / 1280f) * 250) && game.getInput().getY() > h - ((h / 720f) * 100);
            exitButtonSelected = true;
            if (game.getInput().isTouching())
            {
                show = false;
            }
        }
    }

    @Override
    public void render(SampleGraphics graphics)
    {
        if (show)
        {
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

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
            g.drawString("TS: " + game.getHandler().getThread().getState(), x, 80 + 40 * 2);
            g.drawString("Scene: " + game.getScene().getClass().getSimpleName(), x, 80 + 40 * 3);
            g.drawString("JVM Memory: " + Yld.MEMORY() + "/" + Yld.MAX_MEMORY() + "MB", x, 80 + 40 * 4);
            g.drawString("JRE Version: " + System.getProperty("java.version"), x, 80 + 40 * 5);
            g.drawString("Java Class Version: " + System.getProperty("java.class.version"), x, 80 + 40 * 6);
            g.drawString("Yield BUILD: " + Yld.BUILD, x, 80 + 40 * 7);
            g.drawString("Extensions Active: " + game.getExtensions().size(), x, 80 + 40 * 8);
            g.drawString("Architecture: " + System.getProperty("os.arch"), x, 80 + 40 * 9);
            g.drawString("Act Resolution: " + w + "x" + h, x, 80 + 40 * 10);
            g.drawString("Processors: " + Runtime.getRuntime().availableProcessors(), x, 80 + 40 * 11);

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
            if (graphics instanceof AWTGraphics)
            {
                ((AWTGraphics) graphics).getGraphics().drawImage(image, 0, offsetAnimation, w, h, null);
            }
            else
            {
                try
                {
                    ((SlickGraphics) graphics).getGraphics().drawImage(new org.newdawn.slick.Image(BufferedImageUtil.getTexture("YieldOverlayImage", image)), 0, offsetAnimation, w, h, 0, 0, image.getWidth(), image.getHeight());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            offsetAnimation -= offsetAnimation / 4;
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
        if (!YieldOverlay.isShow())
            YieldOverlay.offsetAnimation = -1000;
        YieldOverlay.show = show;
    }
}
