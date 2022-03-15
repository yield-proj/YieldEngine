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

import com.xebisco.yield.utils.Vector2;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class View
{
    private final BufferedImage image;
    private static View actView;
    private static int standardImageType = BufferedImage.TYPE_INT_RGB;
    private final int width, height;
    private Color bgColor = new Color(.1176470588235294f, .1764705882352941f, .4549019607843137f);

    public View(int width, int height)
    {
        actC();
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, standardImageType);
    }

    public static Vector2 mid()
    {
        return new Vector2(View.getActView().getWidth() / 2f, View.getActView().getHeight() / 2f);
    }

    public static Vector2 leftUp()
    {
        return new Vector2(0, 0);
    }

    public static Vector2 leftDown()
    {
        return new Vector2(0, View.getActView().getHeight() / 2f);
    }

    public static Vector2 rightUp()
    {
        return new Vector2(View.getActView().getWidth() / 2f, 0);
    }

    public static Vector2 rightDown()
    {
        return new Vector2(View.getActView().getWidth() / 2f, View.getActView().getHeight() / 2f);
    }

    private void actC()
    {
        actView = this;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public Color getBgColor()
    {
        return bgColor;
    }

    public void setBgColor(Color bgColor)
    {
        this.bgColor = bgColor;
    }

    public static View getActView()
    {
        return actView;
    }

    public static int getStandardImageType()
    {
        return standardImageType;
    }

    public static void setActView(View view)
    {
        View.actView = view;
    }

    public static void setStandardImageType(int standardImageType)
    {
        View.standardImageType = standardImageType;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }
}
