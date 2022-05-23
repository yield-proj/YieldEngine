/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.render.swing;

import com.xebisco.yield.Color;
import com.xebisco.yield.*;
import com.xebisco.yield.config.WindowConfiguration;
import com.xebisco.yield.render.RenderMaster;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Xebisco
 * @since 4-1.2
 */
public class SwingPanel extends JPanel implements RenderMaster {

    private Graphics g;

    private boolean started;

    private JFrame frame;

    private AffineTransform defTransform;

    private BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);

    private HashMap<Integer, BufferedImage> images = new HashMap<>();
    private HashMap<String, Font> fonts = new HashMap<>();

    public static java.awt.Color toAWTColor(Color color) {
        return new java.awt.Color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    @Override
    public SampleGraphics initGraphics() {
        return new SampleGraphics() {

            @Override
            public void setRotation(Vector2 point, float angle) {
                ((Graphics2D) g).setTransform(defTransform);
                ((Graphics2D) g).rotate(Math.toRadians(-angle), point.x, point.y);
            }

            @Override
            public void drawLine(Vector2 point1, Vector2 point2, Color color) {
                g.setColor(toAWTColor(color));
                g.drawLine((int) point1.x, (int) point1.y, (int) point2.x, (int) point2.y);
            }

            @Override
            public void drawRect(Vector2 pos, Vector2 size, Color color, boolean filled) {
                g.setColor(toAWTColor(color));
                if (filled) {
                    g.fillRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                } else {
                    g.drawRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                }
            }

            @Override
            public void drawRoundRect(Vector2 pos, Vector2 size, Color color, boolean filled, int arcWidth, int arcHeight) {
                g.setColor(toAWTColor(color));
                if (filled) {
                    g.fillRoundRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y, arcWidth, arcHeight);
                } else {
                    g.drawRoundRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y, arcWidth, arcHeight);
                }
            }

            @Override
            public void drawOval(Vector2 pos, Vector2 size, Color color, boolean filled) {
                g.setColor(toAWTColor(color));
                if (filled) {
                    g.fillOval((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                } else {
                    g.drawOval((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                }
            }

            @Override
            public void drawArc(Vector2 pos, Vector2 size, Color color, boolean filled, int startAngle, int arcAngle) {
                g.setColor(toAWTColor(color));
                if (filled) {
                    g.fillArc((int) pos.x, (int) pos.y, (int) size.x, (int) size.y, startAngle, arcAngle);
                } else {
                    g.drawArc((int) pos.x, (int) pos.y, (int) size.x, (int) size.y, startAngle, arcAngle);
                }
            }

            @Override
            public void drawString(String str, Color color, Vector2 pos) {
                g.setColor(toAWTColor(color));
                g.drawString(str, (int) (pos.y - getStringWidth(str) / 2), (int) (pos.y + getStringHeight(str) / 2));
            }

            @Override
            public void drawTexture(Texture texture, Vector2 pos, Vector2 size) {
                BufferedImage image = images.get(texture.getTextureID());
                g.drawImage(image, (int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y, null);
            }

            @Override
            public void setFilter(Filter filter) {
                if (filter == Filter.LINEAR) {
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                } else if (filter == Filter.NEAREST) {
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                }
            }

            @Override
            public void setFont(String fontName) {
                g.setFont(fonts.get(fontName));
            }

            @Override
            public float getStringWidth(String str) {
                return g.getFontMetrics().stringWidth(str);
            }

            @Override
            public float getStringWidth(String str, String font) {
                return g.getFontMetrics(fonts.get(font)).stringWidth(str);
            }

            @Override
            public float getStringHeight(String str) {
                return (float) g.getFontMetrics().getStringBounds(str, g).getHeight();
            }

            @Override
            public float getStringHeight(String str, String font) {
                return (float) g.getFontMetrics(fonts.get(font)).getStringBounds(str, g).getHeight();
            }
        };
    }

    @Override
    public SampleWindow initWindow(WindowConfiguration configuration) {
        frame = new JFrame();
        frame.setSize(configuration.width, configuration.height);
        frame.setResizable(configuration.resizable);
        frame.setAlwaysOnTop(configuration.alwaysOnTop);
        frame.setTitle(configuration.title);
        frame.setUndecorated(configuration.undecorated);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
        return new SampleWindow() {
            @Override
            public int getWidth() {
                return frame.getWidth();
            }

            @Override
            public int getHeight() {
                return frame.getHeight();
            }
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (defTransform == null)
            defTransform = ((Graphics2D) g).getTransform();
        if (image.getWidth() != View.getActView().getWidth() || image.getHeight() != View.getActView().getHeight()) {
            image = new BufferedImage(View.getActView().getWidth(), View.getActView().getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        this.g = image.getGraphics();
        g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);
        this.g.setColor(toAWTColor(View.getActView().getBgColor()));
        this.g.fillRect(0, 0, image.getWidth(), image.getHeight());
        started = true;
    }

    @Override
    public void frameStart() {

    }

    @Override
    public void frameEnd() {
        repaint();
    }

    @Override
    public void close() {

    }

    @Override
    public void loadTexture(Texture texture) {
        try {
            images.put(texture.getTextureID(), ImageIO.read(Objects.requireNonNull(SwingPanel.class.getResourceAsStream(texture.getRelativePath()))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadFont(String fontName, int fontSize, int fontStyle) {
        fonts.put(fontName, new Font(fontName, fontStyle, fontSize));
    }

    @Override
    public void loadFont(String fontName, int fontFormat, InputStream inputStream) {
        try {
            fonts.put(fontName, Font.createFont(fontFormat, inputStream));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Integer> pressing() {
        return null;
    }

    @Override
    public int getTextureWidth(int textureId) {
        return images.get(textureId).getWidth();
    }

    @Override
    public int getTextureHeight(int textureId) {
        return images.get(textureId).getHeight();
    }

    @Override
    public boolean canStart() {
        return started;
    }

    public Graphics getG() {
        return g;
    }

    public void setG(Graphics g) {
        this.g = g;
    }

    public HashMap<Integer, BufferedImage> getImages() {
        return images;
    }

    public void setImages(HashMap<Integer, BufferedImage> images) {
        this.images = images;
    }

    public HashMap<String, Font> getFonts() {
        return fonts;
    }

    public void setFonts(HashMap<String, Font> fonts) {
        this.fonts = fonts;
    }
}
