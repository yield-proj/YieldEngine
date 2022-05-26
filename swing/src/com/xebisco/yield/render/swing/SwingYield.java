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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Xebisco
 * @since 4-1.2
 */
public class SwingYield extends JPanel implements RenderMaster, KeyListener, MouseListener {

    private Graphics g;

    private boolean started;

    private JFrame frame;

    private AffineTransform defTransform;

    private BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);

    private final Set<Integer> pressing = new HashSet<>();

    private HashMap<Integer, BufferedImage> images = new HashMap<>();
    private HashMap<String, Font> fonts = new HashMap<>();

    private static BufferedImage yieldImage;

    private View view;

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

            @Override
            public void custom(String instruction, Object... args) {

            }
        };
    }

    @Override
    public SampleWindow initWindow(WindowConfiguration configuration) {
        frame = new JFrame();
        frame.setResizable(configuration.resizable);
        frame.setAlwaysOnTop(configuration.alwaysOnTop);
        frame.setTitle(configuration.title);
        frame.setUndecorated(configuration.undecorated);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(SwingYield.class.getResource(configuration.internalIconPath))).getImage());
        frame.setVisible(true);
        frame.setSize(configuration.width + frame.getInsets().right + frame.getInsets().left, configuration.height + frame.getInsets().top + frame.getInsets().bottom);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        if (yieldImage == null) {
            try {
                yieldImage = ImageIO.read(Objects.requireNonNull(Yld.class.getResourceAsStream("assets/yieldlogo.png")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (Yld.debug) {
            repaint();
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
        if (view != null) {
            if (defTransform == null)
                defTransform = ((Graphics2D) g).getTransform();
            if (image.getWidth() != view.getWidth() || image.getHeight() != view.getHeight()) {
                image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            this.g = image.getGraphics();
            g.drawImage(image, 0, 0, frame.getWidth() - frame.getInsets().right - frame.getInsets().left, frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom, null);
            this.g.setColor(toAWTColor(view.getBgColor()));
            this.g.fillRect(0, 0, image.getWidth(), image.getHeight());
            started = true;
        } else {
            if (yieldImage == null) {
                g.setColor(java.awt.Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g.setColor(java.awt.Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.drawImage(yieldImage, getWidth() / 2 - yieldImage.getWidth() / 2, getHeight() / 2 - yieldImage.getHeight() / 2, null);
            }
        }
    }

    @Override
    public void frameStart() {

    }

    @Override
    public void frameEnd(View view) {
        this.view = view;
        repaint();
    }

    @Override
    public void close() {

    }

    @Override
    public void loadTexture(Texture texture) {
        try {
            images.put(texture.getTextureID(), ImageIO.read(Objects.requireNonNull(texture.getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unloadTexture(Texture texture) {
        images.remove(texture.getTextureID());
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
        return pressing;
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
    public int mouseX() {
        return Yld.clamp((int) (((float) MouseInfo.getPointerInfo().getLocation().x - frame.getInsets().left - frame.getInsets().right - frame.getX()) / (float) getWidth() * (float) view.getWidth()), 0, view.getWidth());
    }

    @Override
    public int mouseY() {
        return Yld.clamp((int) (((float) MouseInfo.getPointerInfo().getLocation().y - frame.getInsets().top - frame.getInsets().bottom - frame.getY()) / (float) getHeight() * (float) view.getHeight()), 0, view.getHeight());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressing.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressing.remove(e.getKeyCode());
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

    public static BufferedImage getYieldImage() {
        return yieldImage;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public AffineTransform getDefTransform() {
        return defTransform;
    }

    public void setDefTransform(AffineTransform defTransform) {
        this.defTransform = defTransform;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressing.add(-e.getButton() - 1);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressing.remove(-e.getButton() - 1);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
