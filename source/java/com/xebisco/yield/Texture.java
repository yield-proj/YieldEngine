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

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Texture extends RelativeFile
{

    private BufferedImage image;
    private java.awt.Image invX, invY, invXY;
    private Image slickImage;
    private static int imageType = BufferedImage.TYPE_INT_ARGB;
    private static int textures;
    private boolean flippedX, flippedY;

    public Texture(BufferedImage image)
    {
        super("");
        textures++;
        if (YldGame.lwjgl)
        {
            try
            {
                slickImage = new Image(BufferedImageUtil.getTexture(Yld.RAND.nextLong() + "-" + textures, image));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            this.image = image;
        }
    }

    public Texture(Texture texture, int x, int y, int width, int height)
    {
        super(texture.getRelativePath());
        textures++;
        if (YldGame.lwjgl)
        {
            this.slickImage = texture.getSlickImage().getSubImage(x, y, width, height);
        }
        else
        {
            final BufferedImage img = new BufferedImage(texture.getImage().getWidth(null), texture.getImage().getHeight(null), imageType);
            img.getGraphics().drawImage(texture.image, 0, 0, null);
            this.image = img.getSubimage(x, y, width, height);
        }
    }

    public Texture(Texture texture, int x, int y, int width, int height, int imageType)
    {
        super(texture.getRelativePath());
        textures++;
        if (YldGame.lwjgl)
        {
            this.slickImage = texture.getSlickImage().getSubImage(x, y, width, height);
        }
        else
        {
            final BufferedImage img = new BufferedImage(texture.getImage().getWidth(null), texture.getImage().getHeight(null), imageType);
            img.getGraphics().drawImage(texture.image, 0, 0, null);
            this.image = img.getSubimage(x, y, width, height);
        }
    }

    public Texture(final String relativePath)
    {
        super(relativePath);
        textures++;
        if (YldGame.lwjgl)
        {
            try
            {
                this.slickImage = new Image(Yld.class.getResourceAsStream(relativePath), Yld.RAND.nextLong() + "-" + textures, false, Image.FILTER_NEAREST);
            } catch (SlickException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                this.image = ImageIO.read(Objects.requireNonNull(Yld.class.getResource(relativePath)));
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public int getWidth()
    {
        if (slickImage == null)
        {
            return image.getWidth();
        }
        else
        {
            return slickImage.getWidth();
        }
    }

    public int getHeight()
    {
        if (slickImage == null)
        {
            return image.getHeight();
        }
        else
        {
            return slickImage.getHeight();
        }
    }

    public java.awt.Image getImage()
    {
        if (flippedX && flippedY)
            return invXY;
        else if (flippedX)
            return invX;
        else if (flippedY)
            return invY;
        else
            return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
        this.invX = image.getScaledInstance(-image.getWidth(), image.getHeight(), java.awt.Image.SCALE_FAST);
        this.invY = image.getScaledInstance(image.getWidth(), -image.getHeight(), java.awt.Image.SCALE_FAST);
        this.invXY = image.getScaledInstance(-image.getWidth(), -image.getHeight(), java.awt.Image.SCALE_FAST);
    }

    public static int getImageType()
    {
        return imageType;
    }

    public static void setImageType(int imageType)
    {
        Texture.imageType = imageType;
    }

    public Image getSlickImage()
    {
        return slickImage.getFlippedCopy(flippedX, flippedY);
    }

    public void setSlickImage(Image slickImage)
    {
        this.slickImage = slickImage;
    }

    public static int getTextures()
    {
        return textures;
    }

    public static void setTextures(int textures)
    {
        Texture.textures = textures;
    }

    public boolean isFlippedX()
    {
        return flippedX;
    }

    public void setFlippedX(boolean flippedX)
    {
        this.flippedX = flippedX;
    }

    public boolean isFlippedY()
    {
        return flippedY;
    }

    public void setFlippedY(boolean flippedY)
    {
        this.flippedY = flippedY;
    }

    public java.awt.Image getInvX()
    {
        return invX;
    }

    public void setInvX(java.awt.Image invX)
    {
        this.invX = invX;
    }

    public java.awt.Image getInvY()
    {
        return invY;
    }

    public void setInvY(java.awt.Image invY)
    {
        this.invY = invY;
    }

    public java.awt.Image getInvXY()
    {
        return invXY;
    }

    public void setInvXY(java.awt.Image invXY)
    {
        this.invXY = invXY;
    }
}
