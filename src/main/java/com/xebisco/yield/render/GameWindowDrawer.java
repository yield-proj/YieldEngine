package com.xebisco.yield.render;

import com.xebisco.yield.display.GameWindow;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;

public class GameWindowDrawer extends JPanel {

    private final GameWindow window;
    private boolean paused = false;
    private Color bgColor = Color.BLACK;

    public GameWindowDrawer(GameWindow window) {
        this.window = window;
        this.setPreferredSize(new Dimension(window.getGameInfo().config.width, window.getGameInfo().config.height));
        this.setDoubleBuffered(true);
        window.add(this);
    }

    @Override
    public void update(Graphics g) {
        paintComponent(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!paused) {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.dispose();
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public GameWindow getWindow() {
        return window;
    }
}
