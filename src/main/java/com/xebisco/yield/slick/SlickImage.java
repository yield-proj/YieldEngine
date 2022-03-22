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

package com.xebisco.yield.slick;

import com.xebisco.yield.graphics.SampleGraphics;
import com.xebisco.yield.graphics.SampleImage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SlickImage implements SampleImage
{
    private Image slickImage;
    @Override
    public SampleImage init(int width, int height)
    {
        try
        {
            slickImage = new Image(width, height);
        } catch (SlickException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public int getWidth()
    {
        return slickImage.getWidth();
    }

    @Override
    public int getHeight()
    {
        return slickImage.getHeight();
    }

    @Override
    public SampleGraphics getGraphics()
    {
        SlickGraphics graphics = new SlickGraphics();
        try
        {
            graphics.setGraphics(slickImage.getGraphics());
        } catch (SlickException e)
        {
            e.printStackTrace();
        }
        return graphics;
    }

    @Override
    public SampleImage getSubimage(int x, int y, int width, int height)
    {
        final SlickImage image = new SlickImage();
        image.setSlickImage(slickImage.getSubImage(x, y, width, height));
        return image;
    }

    @Override
    public SampleImage getScaledInstance(int width, int height)
    {
        final SlickImage image = new SlickImage();
        image.setSlickImage(slickImage.getScaledCopy(width, height));
        return image;
    }

    public Image getSlickImage()
    {
        return slickImage;
    }

    public void setSlickImage(Image slickImage)
    {
        this.slickImage = slickImage;
    }
}
