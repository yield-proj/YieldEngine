package com.xebisco.yield.slick;

import com.xebisco.yield.Texture;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGame;
import org.newdawn.slick.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Deprecated
public class SlickTexture extends Texture
{
    private Image slickImage;

    public SlickTexture(BufferedImage image)
    {
        super(image);
    }

    public SlickTexture(Texture texture, int x, int y, int width, int height)
    {
        super(texture, x, y, width, height);
    }

    public SlickTexture(Texture texture, int x, int y, int width, int height, int imageType)
    {
        super(texture, x, y, width, height, imageType);
    }

    public SlickTexture(String relativePath)
    {
        super(relativePath);
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
