package com.xebisco.yield.engine;

import com.xebisco.yield.Obj;
import com.xebisco.yield.View;
import com.xebisco.yield.YldExtension;
import com.xebisco.yield.YldGraphics;
import com.xebisco.yield.config.WindowConfiguration;
import com.xebisco.yield.exceptions.RendException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class YldWindow
{
    private final JFrame frame;
    private final YldWindowG windowG;
    private final YldGraphics graphics = new YldGraphics();

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
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath))).getImage());
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
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath))).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowG.setDoubleBuffered(configuration.doubleBuffered);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.requestFocus();
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public YldWindowG getWindowG()
    {
        return windowG;
    }

    public static class YldWindowG extends JPanel
    {

        private GameHandler handler;

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
                    g.setColor(new Color((int) (View.getActView().getBgColor().getR() * 255), (int) (View.getActView().getBgColor().getG() * 255), (int) (View.getActView().getBgColor().getB() * 255), (int) (View.getActView().getBgColor().getA() * 255)));
                    width = View.getActView().getImage().getWidth();
                    height = View.getActView().getImage().getHeight();
                }

            }
            else
            {
                g.setColor(bgColor);
            }
            g.fillRect(0, 0, width, height);
            this.g = g;
            if (handler.getGame().getExtensions() != null)
            {
                for (int i = 0; i < handler.getGame().getExtensions().size(); i++)
                {
                    YldExtension extension = handler.getGame().getExtensions().get(i);
                    extension.render(g);
                }
            }
            if (handler.getGame().getScene() != null)
            {
                handleGraphics(g, handler.getGame().getScene().getGraphics());
            }
            handleGraphics(g, handler.getGame().getGraphics());

            if (View.getActView() != null)
            {
                g1.drawImage(View.getActView().getImage(), 0, 0, getWidth(), getHeight(), this);
            }
            g.dispose();
        }

        public static void handleGraphics(Graphics g, YldGraphics graphics)
        {
            int i = 0, max = 0;
            for(int r = 0; r < graphics.shapeRends.size(); r++) {
                if(graphics.shapeRends.get(r).index > max)
                    max = graphics.shapeRends.get(r).index;
            }
            while (i <= max)
            {
                for(int r = 0; r < graphics.shapeRends.size(); r++) {
                    Obj rend = graphics.shapeRends.get(r);
                    if (rend.active && i == rend.index)
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
                        g.setColor(new Color((int) (rend.drawColor.getR() * 255), (int) (rend.drawColor.getG() * 255), (int) (rend.drawColor.getB() * 255), (int) (rend.drawColor.getA() * 255)));
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
                                g.drawImage(rend.image, x, y, x2 - x, y2 - y, null);
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
                            g.drawString(rend.value, x, y + y2);
                        }

                        g2.setTransform(transform);
                    }
                }

                i++;
            }
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
