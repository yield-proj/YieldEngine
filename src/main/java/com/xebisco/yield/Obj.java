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

package com.xebisco.yield;

import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A graphical object.
 * @since 4_alpha1
 * @author Xebisco
 */
public class Obj
{
    public int x, x2, y, y2, index, rotationV, rotationX, rotationY;
    public ShapeType type;
    public boolean filled, center, active = true;
    public Color color, drawColor;
    public String value;
    public Font font;
    public Image image;
    public org.newdawn.slick.Image slickImage;
    public org.newdawn.slick.Font slickFont;

    public Obj(int x, int x2, int y, int y2, ShapeType type, boolean filled, Color color, String value, Font font)
    {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
        this.type = type;
        this.filled = filled;
        this.color = color;
        this.value = value;
        this.font = font;
        if (YldGame.lwjgl && font != null)
        {
            slickFont = new TrueTypeFont(font, false);
        }
        drawColor = color;
    }

    public void center()
    {
        if (View.getActView() != null)
        {
            x = View.getActView().getWidth() / 2 - x2 / 2;
            y = View.getActView().getHeight() / 2 - y2 / 2;
        }
    }

    public enum ShapeType
    {
        OVAL, RECT, LINE, POINT, TEXT
    }
}
