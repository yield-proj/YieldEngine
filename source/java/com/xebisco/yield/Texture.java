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
    private Image slickImage;
    private static int imageType = BufferedImage.TYPE_INT_ARGB;
    private static int textures;

    public Texture(BufferedImage image)
    {
        super("");
        textures++;
        try
        {
            slickImage = new Image(BufferedImageUtil.getTexture(Yld.RAND.nextLong() + "-" + textures, image));
        } catch (Exception e)
        {
            this.image = image;
        }
    }

    public Texture(Texture texture, int x, int y, int width, int height)
    {
        super(texture.getRelativePath());
        textures++;
        try
        {
            final Image img = new Image(texture.getImage().getWidth(null), texture.getImage().getHeight(null));
            img.getGraphics().drawImage(texture.slickImage, 0, 0, null);
            this.slickImage = img.getSubImage(x, y, width, height);
        } catch (Exception e)
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
        try
        {
            final Image img = new Image(texture.getImage().getWidth(null), texture.getImage().getHeight(null));
            img.getGraphics().drawImage(texture.slickImage, 0, 0, null);
            this.slickImage = img.getSubImage(x, y, width, height);
        } catch (Exception e)
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
        try
        {
            this.slickImage = new Image(Yld.class.getResourceAsStream(relativePath), Yld.RAND.nextLong() + "-" + textures, false, Image.FILTER_NEAREST);
        } catch (Exception e)
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

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
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
        return slickImage;
    }

    public void setSlickImage(Image slickImage)
    {
        this.slickImage = slickImage;
    }
}
