package com.xebisco.yield.graphics;

import com.xebisco.yield.Color;
import com.xebisco.yield.Texture;
import com.xebisco.yield.slick.SlickGame;
import com.xebisco.yield.slick.SlickTexture;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface SampleGraphics
{
    Color getColor();

    void setColor(Color c);

    Font getFont();

    void setFont(Font font);

    void drawLine(int x1, int y1, int x2, int y2);

    void fillRect(int x, int y, int width, int height);

    void drawRect(int x, int y, int width, int height);

    void clearRect(int x, int y, int width, int height);

    void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    void drawOval(int x, int y, int width, int height);

    void fillOval(int x, int y, int width, int height);

    void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    void drawTexture(Texture texture, int x, int y, int width, int height);

    void drawTexture(SlickTexture texture, int x, int y, int width, int height, SlickGame slick);

    void drawString(String str, int x, int y);
}
