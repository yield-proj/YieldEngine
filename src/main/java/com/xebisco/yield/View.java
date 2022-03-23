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
import java.awt.image.BufferedImage;

/**
 * The resolution of the actual Yield Game Engine.
 */
public class View
{
    private final BufferedImage image;
    private static View actView;
    private static int standardImageType = BufferedImage.TYPE_INT_RGB;
    private final int width, height;
    private Color bgColor = new Color(.1176470588235294f, .1764705882352941f, .4549019607843137f);

    /**
     * Creates a View and set its width and height.
     * @param width The width to be set.
     * @param height The height to be set.
     */
    public View(int width, int height)
    {
        actC();
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, standardImageType);
    }

    /**
     * @return The middle point of the actual View.
     */
    public static Vector2 mid()
    {
        return new Vector2(View.getActView().getWidth() / 2f, View.getActView().getHeight() / 2f);
    }

    /**
     * @return The upper left point of the actual View.
     */
    public static Vector2 leftUp()
    {
        return new Vector2(0, 0);
    }

    /**
     * @return The downer left point of the actual View.
     */
    public static Vector2 leftDown()
    {
        return new Vector2(0, View.getActView().getHeight() / 2f);
    }

    /**
     * @return The upper right point of the actual View.
     */
    public static Vector2 rightUp()
    {
        return new Vector2(View.getActView().getWidth() / 2f, 0);
    }

    /**
     * @return The downer right point of the actual View.
     */
    public static Vector2 rightDown()
    {
        return new Vector2(View.getActView().getWidth() / 2f, View.getActView().getHeight() / 2f);
    }

    private void actC()
    {
        actView = this;
    }

    /**
     * Getter for the image variable.
     * @return The image variable (null if in GPU mode).
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Getter for the background color.
     * @return The background color.
     */
    public Color getBgColor()
    {
        return bgColor;
    }

    /**
     * Setter for the background color.
     */
    public void setBgColor(Color bgColor)
    {
        this.bgColor = bgColor;
    }

    /**
     * Getter for the actual View.
     * @return The actual View.
     */
    public static View getActView()
    {
        return actView;
    }

    public static int getStandardImageType()
    {
        return standardImageType;
    }

    /**
     * Setter for the actual View.
     */
    public static void setActView(View view)
    {
        View.actView = view;
    }

    public static void setStandardImageType(int standardImageType)
    {
        View.standardImageType = standardImageType;
    }

    /**
     * Getter for the height of this View instance.
     * @return The height.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Getter for the width of this View instance.
     * @return The width.
     */
    public int getWidth()
    {
        return width;
    }
}
