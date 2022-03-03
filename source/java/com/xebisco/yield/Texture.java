package com.xebisco.yield;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Texture extends RelativeFile {

    private BufferedImage image;
    private static int imageType = BufferedImage.TYPE_INT_ARGB;

    public Texture(BufferedImage image) {
        super("");
        this.image = image;
    }

    public Texture(Texture texture, int x, int y, int width, int height) {
        super(texture.getRelativePath());
        final BufferedImage img = new BufferedImage(texture.getImage().getWidth(null), texture.getImage().getHeight(null), imageType);
        img.getGraphics().drawImage(texture.image, 0, 0, null);
        this.image = img.getSubimage(x, y, width, height);
    }

    public Texture(Texture texture, int x, int y, int width, int height, int imageType) {
        super(texture.getRelativePath());
        final BufferedImage img = new BufferedImage(image.getWidth(null), image.getHeight(null), imageType);
        img.getGraphics().drawImage(texture.image, 0, 0, null);
        this.image = img.getSubimage(x, y, width, height);
    }

    public Texture(final String relativePath) {
        super(relativePath);
        try
        {
            this.image = ImageIO.read(Objects.requireNonNull(Yld.class.getResource(relativePath)));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
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
}
