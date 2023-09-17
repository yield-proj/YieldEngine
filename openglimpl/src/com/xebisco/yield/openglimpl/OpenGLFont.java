package com.xebisco.yield.openglimpl;

import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

public class OpenGLFont {
    private TextRenderer textRenderer;
    private final Font font;

    public OpenGLFont(Font font) {
        this.font = font;
    }

    public TextRenderer textRenderer() {
        return textRenderer;
    }

    public OpenGLFont setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
        return this;
    }

    public Font font() {
        return font;
    }
}
