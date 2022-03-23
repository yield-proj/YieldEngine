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

package com.xebisco.yield.slick;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.PNGDecoder;

import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.Obj;
import com.xebisco.yield.View;
import com.xebisco.yield.YldExtension;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.engine.YldEngineAction;
import com.xebisco.yield.input.YldInput;
import com.xebisco.yield.utils.Conversions;

public class SlickGame extends BasicGame
{
    private final YldGame game;

    private SlickGraphics sampleGraphics = new SlickGraphics();
    private Image viewImage;
    private Color bgColor = Color.darkGray;
    private boolean started;

    public SlickGame(YldGame game)
    {
        super(game.getConfiguration().title);
        this.game = game;
    }


    public void loadConfig(GameConfiguration configuration)
    {
        game.getSlickApp().setClearEachFrame(false);
        game.getSlickApp().setTargetFrameRate(configuration.fps);
        game.getSlickApp().setAlwaysRender(configuration.alwaysRender);
        game.getSlickApp().setVSync(configuration.vSync);
        game.getSlickApp().setMusicOn(false);
        game.getSlickApp().setSoundOn(false);
        Display.setResizable(configuration.resizable);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException
    {
        loadConfig(game.getConfiguration());
        if (!started)
        {
            game.setInput(new YldInput(null, gameContainer));
            viewImage = new Image(1280, 720);
            //game.getHandler().getThread().start();
            game.getHandler().setRunning(true);
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
            if (View.getActView() == null)
                new View(1280, 720);
            glLoadIdentity();
            glMatrixMode(GL_MODELVIEW);
            game.create();
        }
        started = true;
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

    @SuppressWarnings("unused")
	@Deprecated
    private ByteBuffer loadIconInstance(BufferedImage image, int dimension)
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
    public void update(GameContainer gameContainer, int elapsed) throws SlickException
    {
        float delta = elapsed / 1000f;
        if (!game.getHandler().isIgnoreTodo())
        {
            for (int i = 0; i < game.getHandler().getTodoList().size(); i++)
            {
                YldEngineAction engineAction = game.getHandler().getTodoList().get(i);
                if (engineAction.getToExec() <= 0)
                {
                    engineAction.getAction().onAction();
                    if (!engineAction.isRepeat())
                        game.getHandler().getTodoList().remove(engineAction);
                    engineAction.setToExec(engineAction.getInitialToExec());
                }
                else
                {
                    engineAction.setToExec(engineAction.getToExec() - elapsed);
                }

            }
        }
        for (int i = 0; i < game.getExtensions().size(); i++)
        {
            YldExtension extension = game.getExtensions().get(i);
            extension.update(delta);
        }
        game.setFrames(game.getFrames() + 1);
        game.update(delta);
        game.process(delta);
        if (game.getScene() != null)
            game.updateScene(delta);
        if (game.getWindow() != null)
        {
            game.getWindow().startGraphics();
            game.getWindow().getWindowG().repaint();
        }
        if (game.getInput() != null)
            game.getInput().setClicking(false);
        /*if (toLoadTexture != null)
        {
            String ref = toLoadTexture.getRelativePath();
            if (ref.startsWith("/") || ref.startsWith("\\"))
                ref = ref.substring(1);
            try
            {
                toLoadTexture.setSlickImage(new Image(BufferedImageUtil.getTexture(ref, toLoadTexture.getImage())));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            toLoadTexture = null;
        }*/
        if (viewImage.getWidth() != View.getActView().getWidth() || viewImage.getHeight() != View.getActView().getHeight())
        {
            viewImage = new Image(View.getActView().getWidth(), View.getActView().getHeight());
        }
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
        g.setAntiAlias(false);
        bgColor = Conversions.toSlickColor(View.getActView().getBgColor());
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
            for (int i1 = 0; i1 < game.getScene().getGraphics().shapeRends.size(); i1++)
            {
                Obj rend = game.getScene().getGraphics().shapeRends.get(i1);
                if (rend.index == i)
                {
                    int x = rend.x, y = rend.y, x2 = (rend.x2 - x), y2 = (rend.y2 - y);
                    g.setColor(Conversions.toSlickColor(rend.drawColor));
                    g.resetTransform();
                    g.rotate(rend.rotationX, rend.rotationY, rend.rotationV);
                    if (rend.type == Obj.ShapeType.RECT)
                        if (rend.slickImage == null)
                        {
                            if (rend.value.hashCode() == "\\imgobj".hashCode())
                                if (rend.filled)
                                    g.fillRect(x, y, x2, y2);
                                else
                                    g.drawRect(x, y, x2, y2);
                        }
                        else
                        {
                            rend.slickImage.setFilter(Image.FILTER_NEAREST);
                            g.drawImage(rend.slickImage, x, y, rend.x2, rend.y2, 0, 0, rend.slickImage.getWidth(), rend.slickImage.getHeight());
                        }
                    else if (rend.type == Obj.ShapeType.OVAL)
                        if (rend.filled)
                            g.fillOval(x, y, x2, y2);
                        else
                            g.drawOval(x, y, x2, y2);
                    else if (rend.type == Obj.ShapeType.TEXT)
                    {
                        g.setFont(rend.slickFont);
                        rend.x2 = g.getFont().getWidth(rend.value);
                        rend.y2 = g.getFont().getLineHeight();
                        g.drawString(rend.value, x - rend.x2 / 2f, y);
                        g.resetFont();
                    }
                    else if (rend.type == Obj.ShapeType.LINE)
                        g.drawLine(x, y, x2, y2);
                    else if (rend.type == Obj.ShapeType.POINT)
                        g.fillRect(x, y, 1, 1);
                }
            }
            i++;
        }
        g.resetTransform();
        if (game.getExtensions() != null)
        {
            for (int i1 = 0; i1 < game.getExtensions().size(); i1++)
            {
                YldExtension extension = game.getExtensions().get(i1);
                sampleGraphics.setGraphics(g);
                extension.render(sampleGraphics);
            }
        }
        g1.drawImage(viewImage, 0, 0, gameContainer.getWidth(), gameContainer.getHeight(), 0, 0, View.getActView().getWidth(), View.getActView().getHeight());
        g.flush();
        g1.flush();

    }

    @Override
    public boolean closeRequested()
    {
        System.exit(0);
        return super.closeRequested();
    }


    public SlickGraphics getSampleGraphics()
    {
        return sampleGraphics;
    }

    public void setSampleGraphics(SlickGraphics sampleGraphics)
    {
        this.sampleGraphics = sampleGraphics;
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
}
