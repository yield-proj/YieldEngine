package com.xebisco.yield.engine;

import com.xebisco.yield.Obj;
import com.xebisco.yield.YldCamera;
import com.xebisco.yield.YldExtension;
import com.xebisco.yield.YldGraphics;
import com.xebisco.yield.config.WindowConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class YldWindow {
    private final JFrame frame;
    private final YldWindowG windowG;
    private final YldGraphics graphics = new YldGraphics();

    public YldWindow() {
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        frame = new JFrame();
        frame.add(windowG = new YldWindowG());
    }

    public void startGraphics() {

    }

    public void toWindow(WindowConfiguration configuration) {
        frame.dispose();
        frame.setAlwaysOnTop(configuration.alwaysOnTop);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath))).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowG.setDoubleBuffered(configuration.doubleBuffered);
        frame.setSize(configuration.width, configuration.height);
        if (configuration.position == WindowConfiguration.WindowPos.CENTER) {
            frame.setLocationRelativeTo(null);
        }
        frame.setExtendedState(JFrame.NORMAL);
        frame.setUndecorated(configuration.undecorated);
        frame.setResizable(configuration.resizable);
        frame.setVisible(true);
        frame.requestFocus();
    }

    public void toFullscreen(WindowConfiguration configuration) {
        frame.dispose();
        frame.setAlwaysOnTop(true);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(YldWindow.class.getResource(configuration.internalIconPath))).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowG.setDoubleBuffered(configuration.doubleBuffered);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.requestFocus();
    }

    public JFrame getFrame() {
        return frame;
    }

    public YldWindowG getWindowG() {
        return windowG;
    }

    public class YldWindowG extends JPanel {

        private GameHandler handler;

        private Graphics g;
        private Color bgColor = new Color(0xFF1E2D74);

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g1) {
            Graphics g = g1;
            int width = getWidth(), height = getHeight();
            if (YldCamera.getActCamera() != null) {
                g = YldCamera.getActCamera().getImage().getGraphics();
                g.setColor(YldCamera.getActCamera().getBgColor());
                width = YldCamera.getActCamera().getImage().getWidth();
                height = YldCamera.getActCamera().getImage().getHeight();
            } else {
                g.setColor(bgColor);
            }
            g.fillRect(0, 0, width, height);
            this.g = g;
            for (int i = 0; i < handler.getGame().getExtensions().size(); i++) {
                YldExtension extension = handler.getGame().getExtensions().get(i);
                extension.render(g);
            }
            int i = 0;
            while (i < graphics.shapeRends.size()) {
                Obj rend = graphics.shapeRends.get(i);
                g.setColor(rend.color);
                g.setFont(rend.font);
                if (rend.type == Obj.ShapeType.RECT)
                    if (rend.image == null)
                        if (rend.filled)
                            g.fillRect(rend.x, rend.y, rend.x2, rend.y2);
                        else
                            g.drawRect(rend.x, rend.y, rend.x2, rend.y2);
                    else
                        g.drawImage(rend.image, rend.x, rend.y, rend.x2, rend.y2, this);
                else if (rend.type == Obj.ShapeType.OVAL)
                    if (rend.filled)
                        g.fillOval(rend.x, rend.y, rend.x2, rend.y2);
                    else
                        g.drawOval(rend.x, rend.y, rend.x2, rend.y2);
                else if (rend.type == Obj.ShapeType.LINE)
                    g.drawLine(rend.x, rend.y, rend.x2, rend.y2);
                else if (rend.type == Obj.ShapeType.POINT)
                    g.drawRect(rend.x, rend.y, 1, 1);
                else if (rend.type == Obj.ShapeType.TEXT) {
                    rend.x2 = g.getFontMetrics().stringWidth(rend.value);
                    rend.y2 = g.getFontMetrics().getHeight();
                    g.drawString(rend.value, rend.x, rend.y + rend.y2);
                }


                i++;
            }
            if (YldCamera.getActCamera() != null) {
                g1.drawImage(YldCamera.getActCamera().getImage(), 0, 0, getWidth(), getHeight(), this);
            }
            g.dispose();
        }

        public Color getBgColor() {
            return bgColor;
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
        }

        public Graphics getG() {
            return g;
        }

        public void setG(Graphics g) {
            this.g = g;
        }

        public GameHandler getHandler() {
            return handler;
        }

        public void setHandler(GameHandler handler) {
            this.handler = handler;
        }
    }


    public YldGraphics getGraphics() {
        return graphics;
    }
}
