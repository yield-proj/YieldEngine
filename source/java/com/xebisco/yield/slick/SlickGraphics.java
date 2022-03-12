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

package com.xebisco.yield.slick;

import com.xebisco.yield.Texture;
import com.xebisco.yield.Yld;
import com.xebisco.yield.graphics.SampleGraphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class SlickGraphics implements SampleGraphics
{
    private Graphics graphics;
    private Font font;

    @Override
    public com.xebisco.yield.Color getColor()
    {
        return new com.xebisco.yield.Color(graphics.getColor().getRed() / 255f, graphics.getColor().getGreen() / 255f, graphics.getColor().getBlue() / 255f, graphics.getColor().getAlpha() / 255f);
    }

    @Override
    public void setColor(com.xebisco.yield.Color c)
    {
        graphics.setColor(new Color(c.getR(), c.getG(), c.getB(), c.getA()));
    }

    @Override
    public Font getFont()
    {
        return font;
    }

    @Override
    public void setFont(Font font)
    {
        this.font = font;
        graphics.setFont(new TrueTypeFont(font, false));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        graphics.fillRect(x, y, width, height);
    }

    @Override
    public void drawRect(int x, int y, int width, int height)
    {
        graphics.drawRect(x, y, width, height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height)
    {
        Yld.log("SLICK GRAPHICS DOES NOT SUPPORT THE 'clearRect(int x, int y, int width, int height)' METHOD");
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(int x, int y, int width, int height)
    {
        graphics.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        graphics.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        graphics.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        graphics.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawTexture(Texture texture, int x, int y, int width, int height)
    {
        Yld.log("SLICK GRAPHICS DOES NOT SUPPORT THE 'drawTexture(Texture texture, int x, int y, int width, int height)' METHOD");
    }

    @Override
    public void drawString(String str, int x, int y)
    {
        graphics.drawString(str, x, y + graphics.getFont().getHeight(str));
    }

    public Graphics getGraphics()
    {
        return graphics;
    }

    public void setGraphics(Graphics graphics)
    {
        this.graphics = graphics;
    }
}
