package com.xebisco.yieldengine.glimpl.mem;

import com.xebisco.yieldengine.core.io.text.Font;
import com.xebisco.yieldengine.core.io.text.IFontLoader;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.core.io.texture.TextureFilter;
import com.xebisco.yieldengine.glimpl.window.OGLPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.IntStream;

public class OGLFontLoader implements IFontLoader {

    private final OGLPanel window;

    public OGLFontLoader(OGLPanel window) {
        this.window = window;
    }

    @Override
    public Font loadFont(String absolutePath, float size, boolean antiAliasing) {
        try {
            java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(absolutePath)).deriveFont(size);
            BufferedImage metricsImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D metricsImageGraphics = metricsImage.createGraphics();
            if (antiAliasing) {
                metricsImageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            metricsImageGraphics.setFont(awtFont);
            FontMetrics metrics = metricsImageGraphics.getFontMetrics();
            metricsImageGraphics.dispose();
            metricsImage.flush();
            HashMap<Character, Texture> glyphs = new HashMap<>();
            IntStream.range(0, 255).parallel().forEach(i -> {
                char c = (char) i;
                if (awtFont.canDisplay(c) && metrics.charWidth(c) > 0) {
                    BufferedImage image = new BufferedImage(metrics.charWidth(c), metrics.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = image.createGraphics();
                    if (antiAliasing) {
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    }
                    g.setFont(awtFont);
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(c), 0, metrics.getAscent());
                    g.dispose();
                    glyphs.put(c, OGLTextureLoader.loadTextureFromBufferedImage(image, TextureFilter.NEAREST, window));
                }
            });
            return Font.create(glyphs, size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unloadFont(Object fontReference) {

    }
}
