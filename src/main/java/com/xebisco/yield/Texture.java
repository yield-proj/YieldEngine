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

import com.xebisco.yield.utils.Conversions;
import com.xebisco.yield.utils.Vector2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Texture extends RelativeFile
{

    private BufferedImage image, invX, invY, invXY;
    private Image slickImage;
    private ArrayList<YldShader> shaders = new ArrayList<>();
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
            this.setImage(img.getSubimage(x, y, width, height));
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
            this.setImage(img.getSubimage(x, y, width, height));
        }
    }

    public Texture(String relativePath)
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
                this.setImage(ImageIO.read(Objects.requireNonNull(Yld.class.getResource(relativePath))));
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
        invX = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        invY = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        invXY = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                invX.setRGB((image.getWidth() - 1) - x, y, image.getRGB(x, y));
                invY.setRGB(x, (image.getHeight() - 1) - y, image.getRGB(x, y));
                invXY.setRGB((image.getWidth() - 1) - x, (image.getHeight() - 1) - y, image.getRGB(x, y));
            }
        }
        /*this.invX = image.getScaledInstance(-image.getWidth(), image.getHeight(), java.awt.Image.SCALE_FAST);
        this.invY = image.getScaledInstance(image.getWidth(), -image.getHeight(), java.awt.Image.SCALE_FAST);
        this.invXY = image.getScaledInstance(-image.getWidth(), -image.getHeight(), java.awt.Image.SCALE_FAST);*/
    }

    public <S extends YldShader> S getShader(Class<S> shader)
    {
        S shader1 = null;
        for (YldShader shader2 : shaders)
        {
            if (shader.getName().hashCode() == shader2.getClass().getName().hashCode() && shader.getName().equals(shader2.getClass().getName()))
            {
                shader1 = shader.cast(shader2);
                break;
            }
        }
        return shader1;
    }

    public void processShaders()
    {
        if (slickImage != null)
        {
            try
            {
                org.newdawn.slick.Graphics g = slickImage.getGraphics();
                for (int x = 0; x < getWidth(); x++)
                {
                    for (int y = 0; y < getHeight(); y++)
                    {
                        for (YldShader shader : shaders)
                        {
                            Pixel pixel = new Pixel();
                            pixel.setLocation(new Vector2(x, y));
                            pixel.setColor(Conversions.toColor(slickImage.getColor(x, y)));
                            shader.process(pixel);
                            g.setColor(Conversions.toSlickColor(pixel.getColor()));
                            g.fillRect((int) pixel.getLocation().x, (int) pixel.getLocation().y, 1, 1);
                        }
                    }
                }
                g.flush();
            } catch (SlickException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            BufferedImage img = new BufferedImage(getWidth(), getHeight(), java.awt.Image.SCALE_FAST);
            Graphics g = img.getGraphics();
            for (int x = 0; x < getWidth(); x++)
            {
                for (int y = 0; y < getHeight(); y++)
                {
                    for (YldShader shader : shaders)
                    {
                        Pixel pixel = new Pixel();
                        pixel.setLocation(new Vector2(x, y));
                        pixel.setColor(Conversions.toColor(new Color(image.getRGB(x, y))));
                        shader.process(pixel);
                        g.setColor(Conversions.toAWTColor(pixel.getColor()));
                        g.fillRect((int) pixel.getLocation().x, (int) pixel.getLocation().y, 1, 1);
                    }
                }
            }
            Graphics g1 = image.getGraphics();
            g1.drawImage(img, 0, 0, null);
            g.dispose();
            g1.dispose();
        }
    }

    public YldShader addShader(YldShader shader)
    {
        shaders.add(shader);
        return shader;
    }

    public ArrayList<YldShader> getShaders()
    {
        return shaders;
    }

    public void setShaders(ArrayList<YldShader> shaders)
    {
        this.shaders = shaders;
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
        if (slickImage != null)
            return slickImage.getFlippedCopy(flippedX, flippedY);
        return null;
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

    public BufferedImage getInvX()
    {
        return invX;
    }

    public void setInvX(BufferedImage invX)
    {
        this.invX = invX;
    }

    public BufferedImage getInvY()
    {
        return invY;
    }

    public void setInvY(BufferedImage invY)
    {
        this.invY = invY;
    }

    public BufferedImage getInvXY()
    {
        return invXY;
    }

    public void setInvXY(BufferedImage invXY)
    {
        this.invXY = invXY;
    }
}
