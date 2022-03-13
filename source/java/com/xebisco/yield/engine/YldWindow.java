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

package com.xebisco.yield.engine;

import com.xebisco.yield.*;
import com.xebisco.yield.config.WindowConfiguration;
import com.xebisco.yield.graphics.AWTGraphics;
import com.xebisco.yield.utils.Conversions;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class YldWindow
{
    private final JFrame frame;
    private final YldWindowG windowG;
    private final YldGraphics graphics = new YldGraphics();
    private boolean sync;

    public YldWindow()
    {
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        frame = new JFrame();
        frame.add(windowG = new YldWindowG());
    }

    public void startGraphics()
    {

    }

    public void toWindow(WindowConfiguration configuration)
    {
        frame.dispose();
        if (configuration.hideMouse)
            frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                    new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource("/com/xebisco/yield/assets/none.png"))).getImage(),
                    new Point(0, 0),
                    null));
        frame.setAlwaysOnTop(configuration.alwaysOnTop);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath + ".png"))).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowG.setDoubleBuffered(configuration.doubleBuffered);
        frame.setSize(configuration.width, configuration.height);
        if (configuration.position == WindowConfiguration.WindowPos.CENTER)
        {
            frame.setLocationRelativeTo(null);
        }
        frame.setExtendedState(JFrame.NORMAL);
        frame.setUndecorated(configuration.undecorated);
        frame.setResizable(configuration.resizable);
        frame.setVisible(true);
        frame.requestFocus();
        sync = configuration.sync;
    }

    public void toFullscreen(WindowConfiguration configuration)
    {
        frame.dispose();
        if (configuration.hideMouse)
            frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                    new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource("/com/xebisco/yield/assets/none.png"))).getImage(),
                    new Point(0, 0),
                    null));
        frame.setAlwaysOnTop(true);
        if (configuration.internalIconPath.split("\\.").length > 1)
            frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath))).getImage());
        else
            frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath + ".png"))).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowG.setDoubleBuffered(configuration.doubleBuffered);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.requestFocus();
        sync = configuration.sync;
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public YldWindowG getWindowG()
    {
        return windowG;
    }

    public class YldWindowG extends JPanel
    {

        private GameHandler handler;
        private AWTGraphics sampleGraphics = new AWTGraphics();

        private Graphics g;
        private Color bgColor = new Color(0xFF1E2D74);

        @Override
        public void update(Graphics g)
        {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g1)
        {
            Graphics g = g1;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            int width = getWidth(), height = getHeight();
            if (View.getActView() != null)
            {
                if (View.getActView().getImage() != null)
                {
                    g = View.getActView().getImage().getGraphics();
                    g.setColor(Conversions.toAWTColor(View.getActView().getBgColor()));
                    width = View.getActView().getWidth();
                    height = View.getActView().getHeight();
                }

            }
            else
            {
                g.setColor(bgColor);
            }
            this.g = g;
            g.fillRect(0, 0, width, height);
            if (handler.getGame().getScene() != null)
                handleGraphics(g, handler.getGame().getScene().getGraphics());
            if (handler.getGame().getExtensions() != null)
            {
                for (int i = 0; i < handler.getGame().getExtensions().size(); i++)
                {
                    YldExtension extension = handler.getGame().getExtensions().get(i);
                    sampleGraphics.setGraphics(g);
                    extension.render(sampleGraphics);
                }
            }
            if (View.getActView() != null)
                g1.drawImage(View.getActView().getImage(), 0, 0, getWidth(), getHeight(), this);

            if (sync)
            {
                Toolkit.getDefaultToolkit().sync();
            }
        }

        public void handleGraphics(Graphics g, YldGraphics graphics)
        {
            int i = 0, max = 0;
            for (int r = 0; r < graphics.shapeRends.size(); r++)
            {
                if (graphics.shapeRends.get(r).index > max)
                    max = graphics.shapeRends.get(r).index;
            }
            while (i <= max)
            {
                for (int r = 0; r < graphics.shapeRends.size(); r++)
                {
                    Obj rend = graphics.shapeRends.get(r);
                    if (i == rend.index && rend.active && rend.color.getA() > 0)
                    {
                        Graphics2D g2 = (Graphics2D) g;
                        AffineTransform transform = g2.getTransform();
                        g2.rotate(Math.toRadians(rend.rotationV), rend.rotationX, rend.rotationY);
                        if (rend.center)
                        {
                            if (rend.x2 != 0 || rend.y2 != 0)
                            {
                                rend.center();
                            }
                        }
                        for (int i2 = 0; i2 < graphics.getFilters().size(); i2++)
                        {
                            graphics.getFilters().get(i2).process(rend);
                        }
                        g.setColor(Conversions.toAWTColor(rend.drawColor));
                        g.setFont(rend.font);
                        int x = rend.x, x2 = rend.x2, y = rend.y, y2 = rend.y2;
                        if (rend.type != Obj.ShapeType.TEXT)
                        {
                            if (x2 < x)
                            {
                                int sx = x;
                                x = x2;
                                x2 = sx;
                            }
                            if (y2 < y)
                            {
                                int sy = y;
                                y = y2;
                                y2 = sy;
                            }
                        }

                        if (rend.type == Obj.ShapeType.RECT)
                            if (rend.image == null)
                                if (rend.filled)
                                    g.fillRect(x, y, x2 - x, y2 - y);
                                else
                                    g.drawRect(x, y, x2 - x, y2 - y);
                            else
                            {
                                g.drawImage(rend.image, x, y, x2 - x, y2 - y, null);
                                rend.slickImage = null;
                            }
                        else if (rend.type == Obj.ShapeType.OVAL)
                            if (rend.filled)
                                g.fillOval(x, y, x2 - x, y2 - y);
                            else
                                g.drawOval(x, y, x2 - x, y2 - y);
                        else if (rend.type == Obj.ShapeType.LINE)
                            g.drawLine(x, y, x2, y2);
                        else if (rend.type == Obj.ShapeType.POINT)
                            g.drawRect(x, y, 1, 1);
                        else if (rend.type == Obj.ShapeType.TEXT)
                        {
                            rend.x2 = g.getFontMetrics().stringWidth(rend.value);
                            rend.y2 = g.getFontMetrics().getHeight();
                            g.drawString(rend.value, x - rend.x2 / 2, y + rend.y2 / 2);
                        }

                        g2.setTransform(transform);
                    }
                }

                i++;
            }
        }

        public AWTGraphics getSampleGraphics()
        {
            return sampleGraphics;
        }

        public void setSampleGraphics(AWTGraphics sampleGraphics)
        {
            this.sampleGraphics = sampleGraphics;
        }

        public Color getBgColor()
        {
            return bgColor;
        }

        public void setBgColor(Color bgColor)
        {
            this.bgColor = bgColor;
        }

        public Graphics getG()
        {
            return g;
        }

        public void setG(Graphics g)
        {
            this.g = g;
        }

        public GameHandler getHandler()
        {
            return handler;
        }

        public void setHandler(GameHandler handler)
        {
            this.handler = handler;
        }
    }


    public YldGraphics getGraphics()
    {
        return graphics;
    }
}
