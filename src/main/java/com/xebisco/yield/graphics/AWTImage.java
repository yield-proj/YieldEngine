/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AWTImage implements SampleImage
{
    private Image image;

    @Override
    public SampleImage init(int width, int height)
    {
        image = new BufferedImage(width, height, Image.SCALE_FAST);
        return this;
    }

    @Override
    public int getWidth()
    {
        return ((BufferedImage) image).getWidth();
    }

    @Override
    public int getHeight()
    {
        return ((BufferedImage) image).getHeight();
    }

    @Override
    public SampleGraphics getGraphics()
    {
        final AWTGraphics graphics = new AWTGraphics();
        graphics.setGraphics(image.getGraphics());
        return graphics;
    }

    @Override
    public SampleImage getSubimage(int x, int y, int width, int height)
    {
        AWTImage image = new AWTImage();
        image.setImage(((BufferedImage) this.image).getSubimage(x, y, width, height));
        return image;
    }

    @Override
    public SampleImage getScaledInstance(int width, int height)
    {
        AWTImage image = new AWTImage();
        image.setImage(this.image.getScaledInstance(width, height, Image.SCALE_FAST));
        return image;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }
}
