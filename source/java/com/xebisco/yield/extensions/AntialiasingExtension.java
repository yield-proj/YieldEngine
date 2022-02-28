package com.xebisco.yield.extensions;

import com.xebisco.yield.YldExtension;

import java.awt.*;

public class AntialiasingExtension extends YldExtension {

    @Override
    public void render(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}
