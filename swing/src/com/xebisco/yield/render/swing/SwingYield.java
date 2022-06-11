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
import java.awt.image.VolatileImage;
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

    private double savedRotation;

    private Vector2 savedRotationPoint = new Vector2();

    private AffineTransform defTransform;

    private VolatileImage image;

    private final Set<Integer> pressing = new HashSet<>();

    private HashMap<Integer, Image> images = new HashMap<>();
    private HashMap<String, Font> fonts = new HashMap<>();

    private final static BufferedImage yieldImage;
    private float fps;

    private long last, actual;

    private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

    static {
        try {
            yieldImage = ImageIO.read(Objects.requireNonNull(Yld.class.getResourceAsStream("assets/yieldlogo.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private View view;

    public static java.awt.Color toAWTColor(Color color) {
        return new java.awt.Color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    public static Color toYieldColor(java.awt.Color color) {
        return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    @Override
    public SampleGraphics initGraphics() {
        return new SampleGraphics() {

            @Override
            public void setRotation(Vector2 point, float angle) {
                ((Graphics2D) g).setTransform(defTransform);
                savedRotation = Math.toRadians(-angle);
                savedRotationPoint = point;
                ((Graphics2D) g).rotate(savedRotation, point.x, point.y);
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
            public void drawString(String str, Color color, Vector2 pos, Vector2 scale, String fontName) {
                g.setColor(toAWTColor(color));
                setFont(fontName);
                AffineTransform af = new AffineTransform();
                af.concatenate(AffineTransform.getScaleInstance(scale.x, scale.y));
                ((Graphics2D) g).setTransform(af);
                ((Graphics2D) g).rotate(savedRotation, savedRotationPoint.x, savedRotationPoint.y);
                af = null;

                g.drawString(str, (int) (pos.x - getStringWidth(str) / 2), (int) (pos.y + (getStringHeight(str) / 4)));
                ((Graphics2D) g).setTransform(defTransform);
            }

            @Override
            public void drawTexture(Texture texture, Vector2 pos, Vector2 size) {
                Image image = images.get(texture.getTextureID());
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
        frame.setSize(configuration.width, configuration.height);
        frame.setResizable(configuration.resizable);
        frame.setAlwaysOnTop(configuration.alwaysOnTop);
        frame.setTitle(configuration.title);
        frame.setUndecorated(configuration.undecorated);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        if(configuration.fullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setAlwaysOnTop(true);
        }
        frame.setVisible(true);
        if(!configuration.fullscreen) {
            frame.setSize(configuration.width + frame.getInsets().right + frame.getInsets().left, configuration.height + frame.getInsets().top + frame.getInsets().bottom);
        }
        frame.setLocationRelativeTo(null);
        repaint();
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(SwingYield.class.getResource(configuration.internalIconPath))).getImage());
        if (Yld.debug) {
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
        actual = System.currentTimeMillis();
        fps = 1000 / (float) (actual - last);
        if (view != null) {
            if (defTransform == null)
                defTransform = ((Graphics2D) g).getTransform();
            started = true;
            g.drawImage(image, 0, 0, frame.getWidth() - frame.getInsets().right - frame.getInsets().left, frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom, null);
        } else {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(yieldImage, getWidth() / 2 - yieldImage.getWidth() / 2, getHeight() / 2 - yieldImage.getHeight() / 2, null);
        }
        g.dispose();
        Toolkit.getDefaultToolkit().sync();
        last = System.currentTimeMillis();
    }

    @Override
    public void update(Graphics g) {
        paintComponent(g);
    }

    @Override
    public void frameStart(SampleGraphics g, View view) {
        this.view = view;
        if(image != null)
            this.g = image.getGraphics();
        if (started)
            g.drawRect(new Vector2(view.getWidth() / 2f, view.getHeight() / 2f), new Vector2(view.getWidth(), view.getHeight()), view.getBgColor(), true);
    }

    @Override
    public void frameEnd() {
        if (g != null)
            g.dispose();
        if (image == null || image.getWidth() != view.getWidth() || image.getHeight() != view.getHeight()) {
            image = gc.createCompatibleVolatileImage(view.getWidth(), view.getHeight(), Transparency.TRANSLUCENT);
        }
        (new Repainter(this)).execute();
    }

    @Override
    public float fpsCount() {
        return fps;
    }

    @Override
    public void close() {

    }

    @Override
    public void loadTexture(Texture texture) {
        try {
            loadTexture(texture, ImageIO.read(Objects.requireNonNull(texture.getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTexture(Texture texture, Image img) {
        BufferedImage image = (BufferedImage) img;
        images.put(texture.getTextureID(), image);
        BufferedImage imageX = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB),
                imageY = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB),
                imageXY = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics gX = imageX.getGraphics();
        for (int y = 0; y < image.getHeight(null); y++) {
            for (int x = 0; x < image.getWidth(null); x++) {
                imageX.setRGB((image.getWidth() - 1) - x, y, image.getRGB(x, y));
                imageY.setRGB(x, (image.getHeight() - 1) - y, image.getRGB(x, y));
                imageXY.setRGB((image.getWidth() - 1) - x, (image.getHeight() - 1) - y, image.getRGB(x, y));
            }
        }
        Texture invX = new Texture(""), invY = new Texture(""), invXY = new Texture("");
        images.put(invX.getTextureID(), imageX);
        images.put(invY.getTextureID(), imageY);
        images.put(invXY.getTextureID(), imageXY);
        texture.setInvertedX(invX);
        texture.setInvertedY(invY);
        texture.setInvertedXY(invXY);
        texture.setVisualUtils(this);
    }

    @Override
    public void setPixel(Texture texture, Color color, Vector2 position) {
        try {
            Graphics g = images.get(texture.getTextureID()).getGraphics();
            g.setColor(toAWTColor(color));
            g.drawRect((int) position.x, (int) position.y, 1, 1);
            g.dispose();
        } catch (ArrayIndexOutOfBoundsException ignore) {
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
    public Texture cutTexture(Texture texture, int x, int y, int width, int height) {
        VolatileImage img = gc.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
        Graphics g = img.getGraphics();
        g.drawImage(images.get(texture.getTextureID()), -x, -y, null);
        g.dispose();
        Texture tex = new Texture("");
        loadTexture(tex, img);
        return tex;
    }

    @Override
    public Texture scaleTexture(Texture texture, int width, int height) {
        Image img = images.get(texture.getTextureID());
        BufferedImage image1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image1.getGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        Texture tex = new Texture("");
        loadTexture(tex, image1);
        return tex;
    }

    @Override
    public Color[][] getTextureColors(Texture texture) {
        BufferedImage image1 = (BufferedImage) images.get(texture.getTextureID());
        Color[][] pixels = new Color[image1.getWidth()][image1.getHeight()];
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                int p = image1.getRGB(x, y);
                pixels[x][y] = (new Color(((p>>16) & 0xff) / 255f, ((p>>8) & 0xff) / 255f, (p & 0xff) / 255f, ((p>>24) & 0xff) / 255f));
            }
        }
        return pixels;
    }

    @Override
    public void setTextureColors(Texture texture, Color[][] colors) {
        BufferedImage image1 = (BufferedImage) images.get(texture.getTextureID());
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                image1.setRGB(x, y, toAWTColor(colors[x][y]).getRGB());
            }
        }
    }

    @Override
    public void unloadFont(String fontName) {
        fonts.remove(fontName);
    }

    @Override
    public Set<Integer> pressing() {
        return pressing;
    }

    @Override
    public int getTextureWidth(int textureId) {
        return images.get(textureId).getWidth(null);
    }

    @Override
    public int getTextureHeight(int textureId) {
        return images.get(textureId).getHeight(null);
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

    public HashMap<Integer, Image> getImages() {
        return images;
    }

    public void setImages(HashMap<Integer, Image> images) {
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

    public VolatileImage getImage() {
        return image;
    }

    public void setImage(VolatileImage image) {
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

    public double getSavedRotation() {
        return savedRotation;
    }

    public void setSavedRotation(double savedRotation) {
        this.savedRotation = savedRotation;
    }

    public Vector2 getSavedRotationPoint() {
        return savedRotationPoint;
    }

    public void setSavedRotationPoint(Vector2 savedRotationPoint) {
        this.savedRotationPoint = savedRotationPoint;
    }

    public Set<Integer> getPressing() {
        return pressing;
    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public long getActual() {
        return actual;
    }

    public void setActual(long actual) {
        this.actual = actual;
    }

    public GraphicsEnvironment getGe() {
        return ge;
    }

    public void setGe(GraphicsEnvironment ge) {
        this.ge = ge;
    }

    public GraphicsConfiguration getGc() {
        return gc;
    }

    public void setGc(GraphicsConfiguration gc) {
        this.gc = gc;
    }
}
