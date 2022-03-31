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

/**
 * A Texture is an image for a game, can be added to graphical objects to display images.
 */
public class Texture extends RelativeFile
{

    private BufferedImage image, invX, invY, invXY;
    private Image slickImage;
    private ArrayList<YldShader> shaders = new ArrayList<>();
    private static int imageType = BufferedImage.TYPE_INT_ARGB;
    private static int textures;
    private boolean flippedX, flippedY;

    /**
     * Creates a Texture instance based on the image variable passed.
     * @param image The image variable.
     */
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

    /**
     * Creates a texture with the same properties of the passed texture.
     * @param texture The texture to be cloned.
     */
    public Texture(Texture texture) {
        super(texture.getRelativePath());
        if(texture.getImage() != null)
        this.setImage((BufferedImage) texture.getImage());
        this.setFlippedX(texture.isFlippedX());
        this.setShaders(texture.getShaders());
        this.setFlippedY(texture.isFlippedY());
        if(texture.getSlickImage() != null)
        this.setSlickImage(texture.getSlickImage());
    }

    /**
     * Creates a sub texture based on the arguments passed.
     * @param texture The parent texture.
     * @param x The x to start the sub texture.
     * @param y The y to start the sub texture.
     * @param width The width of the sub texture.
     * @param height The height of the sub texture.
     */
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

    /**
     * Creates a sub texture based on the arguments passed.
     * @param texture The parent texture.
     * @param x The x to start the sub texture.
     * @param y The y to start the sub texture.
     * @param width The width of the sub texture.
     * @param height The height of the sub texture.
     * @param imageType The image type for the texture (only in the CPU mode).
     */
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

    /**
     * Creates a Texture instance based on the relativePath variable passed.
     * @param relativePath The relative path of the image.
     */
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

    /**
     * Getter for the Texture width.
     * @return The Texture width.
     */
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

    /**
     * Getter for the Texture height.
     * @return The Texture height.
     */
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

    /**
     * Getter for the AWT image.
     * @return The AWT image. (null if in GPU mode)
     */
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

    /**
     * Setter for the AWT image.
     */
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
    }

    /**
     * Search for all the YldShader instances in this Texture.
     * @param shader The class type of the shader that's being searched.
     * @return The shader found (null if not found)
     */
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

    /**
     * Updates all the shaders added in the shaders list. (Can be slow).
     */
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

    /**
     * Adds a shader to the shaders list.
     * @param shader The shader to be added.
     * @return The added shader.
     */
    public YldShader addShader(YldShader shader)
    {
        shaders.add(shader);
        return shader;
    }

    /**
     * Getter for the shaders list.
     * @return The shaders list.
     */
    public ArrayList<YldShader> getShaders()
    {
        return shaders;
    }

    /**
     * Setter for the shaders list.
     */
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

    /**
     * Getter for the slickImage variable.
     * @return The slickImage variable. (null if in CPU mode)
     */
    public Image getSlickImage()
    {
        if (slickImage != null)
            return slickImage.getFlippedCopy(flippedX, flippedY);
        return null;
    }

    /**
     * Setter for the slickImage variable.
     */
    public void setSlickImage(Image slickImage)
    {
        this.slickImage = slickImage;
    }

    /**
     * Counter for all the textures in this Yield Game Engine instance.
     */
    public static int getTextures()
    {
        return textures;
    }

    public static void setTextures(int textures)
    {
        Texture.textures = textures;
    }

    /**
     * @return If the Texture is flipped in the x-axis.
     */
    public boolean isFlippedX()
    {
        return flippedX;
    }

    /**
     * Sets if the Texture is flipped in the x-axis.
     * @param flippedX If the is flipped in the x-axis.
     */
    public void setFlippedX(boolean flippedX)
    {
        this.flippedX = flippedX;
    }

    /**
     * @return If the Texture is flipped in the y-axis.
     */
    public boolean isFlippedY()
    {
        return flippedY;
    }

    /**
     * Sets if the Texture is flipped in the y-axis.
     * @param flippedY If the is flipped in the y-axis.
     */
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
