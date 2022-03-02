package com.xebisco.yield.slick;

import com.xebisco.yield.Obj;
import com.xebisco.yield.View;
import com.xebisco.yield.YldGame;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.*;

public class SlickGame extends BasicGame
{
    private final YldGame game;

    private Image viewImage;
    private Color bgColor = Color.darkGray;
    private final HashSet<Image> images = new HashSet<>();

    public SlickGame(YldGame game)
    {
        super(game.getConfiguration().title);
        this.game = game;
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException
    {
        viewImage = new Image(1280, 720);
        game.getHandler().getThread().start();
        Display.setResizable(game.getConfiguration().resizable);
        gameContainer.setShowFPS(game.getConfiguration().showFPS);
        String[] ICON_PATHS = new String[]{game.getConfiguration().internalIconPath + "16.png", game.getConfiguration().internalIconPath + "32.png",
                game.getConfiguration().internalIconPath + "64.png", game.getConfiguration().internalIconPath + "128.png"};
        ByteBuffer[] icon_array = new ByteBuffer[ICON_PATHS.length];
        try
        {
            for (int i = 0; i < ICON_PATHS.length; i++)
            {
                icon_array[i] = ByteBuffer.allocateDirect(1);
                String path = ICON_PATHS[i];
                icon_array[i] = loadIcon(path);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Display.setIcon(icon_array);
        new View(1280, 720);
    }

    private static ByteBuffer loadIcon(String path) throws IOException
    {
        InputStream inputStream = SlickGame.class.getResourceAsStream(path);
        try
        {
            PNGDecoder decoder = new PNGDecoder(inputStream);
            ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
            decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.RGBA);
            bytebuf.flip();
            return bytebuf;
        } finally
        {
            assert inputStream != null;
            inputStream.close();
        }
    }

    private static ByteBuffer loadIconInstance(BufferedImage image, int dimension)
    {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio;
        if (image.getWidth() > scaledIcon.getWidth())
        {
            ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
        }
        else
        {
            ratio = (int) (scaledIcon.getWidth() / image.getWidth());
        }
        if (image.getHeight() > scaledIcon.getHeight())
        {
            double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();
            if (r2 < ratio)
            {
                ratio = r2;
            }
        }
        else
        {
            double r2 = (int) (scaledIcon.getHeight() / image.getHeight());
            if (r2 < ratio)
            {
                ratio = r2;
            }
        }
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
                (int) (width), (int) (height), null);
        g.dispose();

        byte[] imageBuffer = new byte[dimension * dimension * 4];
        int counter = 0;
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                int colorSpace = scaledIcon.getRGB(j, i);
                imageBuffer[counter] = (byte) ((colorSpace << 8) >> 24);
                imageBuffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
                imageBuffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
                imageBuffer[counter + 3] = (byte) (colorSpace >> 24);
                counter += 4;
            }
        }
        return ByteBuffer.wrap(imageBuffer);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException
    {
        if (!game.getHandler().isRunning())
        {
            gameContainer.exit();
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException
    {
        Graphics g1 = g;
        g = viewImage.getGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, viewImage.getWidth(), viewImage.getHeight());
        int i = 0, max = 0;
        for (int r = 0; r < game.getScene().getGraphics().shapeRends.size(); r++)
        {
            try
            {
                if (game.getScene().getGraphics().shapeRends.get(r).index > max)
                    max = game.getScene().getGraphics().shapeRends.get(r).index;
            } catch (NullPointerException ignore)
            {
            }
        }
        while (i <= max)
        {
            for (Obj rend : game.getScene().getGraphics().shapeRends)
            {
                int x = rend.x, y = rend.y, x2 = rend.x2 - x, y2 = rend.y2 - y;
                g.setColor(new Color(rend.color.getR() * 255, rend.color.getG() * 255, rend.color.getB() * 255, rend.color.getA() * 255));
                g.resetTransform();
                g.rotate(rend.rotationX, rend.rotationY, rend.rotationV);
                if (rend.type == Obj.ShapeType.RECT)
                    if (rend.image == null)
                        if (rend.filled)
                            g.fillRect(x, y, x2, y2);
                        else
                            g.drawRect(x, y, x2, y2);
                    else
                    {
                        if (rend.value == null)
                        {
                            try
                            {
                                rend.value = String.valueOf(images.size());
                                images.add(new Image(BufferedImageUtil.getTexture(String.valueOf(images.size()), rend.image)));
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        AtomicReference<Image> image = new AtomicReference<>();
                        images.forEach(img ->
                        {
                            if (img.getTexture().getTextureRef().hashCode() == rend.value.hashCode())
                            {
                                image.set(img);
                            }
                        });
                        g.drawImage(image.get(), x, y, rend.x2, rend.y2, 0, 0, rend.image.getWidth(), rend.image.getHeight());
                    }
                else if (rend.type == Obj.ShapeType.OVAL)
                    if (rend.filled)
                        g.fillOval(x, y, x2, y2);
                    else
                        g.drawOval(x, y, x2, y2);
                else if (rend.type == Obj.ShapeType.TEXT)
                    g.drawString(rend.value, x, y);
                else if (rend.type == Obj.ShapeType.LINE)
                    g.drawLine(x, y, x2, y2);
                else if (rend.type == Obj.ShapeType.POINT)
                    g.fillRect(x, y, 1, 1);
            }
            i++;
        }
        g1.drawImage(viewImage, 0, 0, Display.getWidth(), Display.getHeight(), 0, 0, View.getActView().getWidth(), View.getActView().getHeight());
    }

    @Override
    public boolean closeRequested()
    {
        System.exit(0);
        return super.closeRequested();
    }

    public YldGame getGame()
    {
        return game;
    }

    public Image getViewImage()
    {
        return viewImage;
    }

    public void setViewImage(Image viewImage)
    {
        this.viewImage = viewImage;
    }

    public Color getBgColor()
    {
        return bgColor;
    }

    public void setBgColor(Color bgColor)
    {
        this.bgColor = bgColor;
    }

    public HashSet<Image> getImages()
    {
        return images;
    }
}
