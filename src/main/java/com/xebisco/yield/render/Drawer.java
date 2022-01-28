package com.xebisco.yield.render;

import java.awt.*;

public class Drawer {

    private Graphics graphics;

    public void fillRect(float x, float y, int width, int height) {
        graphics.fillRect((int) x, (int) y, width, height);
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
