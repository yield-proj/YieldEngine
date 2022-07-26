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

package com.xebisco.swingyield;

import com.xebisco.yield.Color;
import com.xebisco.yield.*;
import com.xebisco.yield.config.WindowConfiguration;
import com.xebisco.yield.exceptions.AudioClipException;
import com.xebisco.yield.exceptions.CannotLoadException;
import com.xebisco.yield.exceptions.InvalidTextureTypeException;
import com.xebisco.yield.render.ExceptionThrower;
import com.xebisco.yield.render.RenderMaster;
import com.xebisco.swingyield.exceptions.NotCapableTextureException;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Xebisco
 * @since 4-1.2
 */
public class SwingYield extends Canvas implements RenderMaster, KeyListener, MouseListener, ExceptionThrower {

    private Graphics g;

    private boolean started;

    private JFrame frame;

    private double savedRotation;

    private final SwingYield swingYield;

    private Vector2 savedRotationPoint = new Vector2();

    private AffineTransform defTransform;

    private Image image;

    private final Set<Integer> pressing = new HashSet<>();

    private HashMap<Integer, Image> images = new HashMap<>();
    private HashMap<Integer, Clip> clips = new HashMap<>();
    private HashMap<String, Font> fonts = new HashMap<>();

    private final static BufferedImage yieldImage;
    private float fps;

    private long last, actual;

    static {
        BufferedImage yieldImage1 = null;
        try {
            yieldImage1 = ImageIO.read(Objects.requireNonNull(Yld.class.getResourceAsStream("assets/yieldlogo.png")));
        } catch (IOException e) {
            Yld.throwException(e);
            System.exit(1);
        } catch (NullPointerException e) {
            Yld.throwException(new CannotLoadException("Could not find yield image"));
            System.exit(1);
        }
        yieldImage = yieldImage1;
    }

    public SwingYield() {
        swingYield = this;
    }

    private View view;

    public static java.awt.Color toAWTColor(Color color) {
        return new java.awt.Color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    public static Color toYieldColor(java.awt.Color color) {
        return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    @Override
    public void before(YldGame game) {

    }

    public void rotate(Vector2 point, Float angle) {
        ((Graphics2D) g).rotate(Math.toRadians(-angle), point.x, point.y);
    }

    @Override
    public SampleGraphics initGraphics() {
        return new SwingGraphics();
    }

    public class SwingGraphics implements SampleGraphics {
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
            if (pos.x - size.x < view.getWidth() &&
                    pos.x + size.x > 0 &&
                    pos.y - size.y < view.getHeight() &&
                    pos.y + size.y > 0)
                if (filled) {
                    g.fillRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                } else {
                    g.drawRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                }
        }

        @Override
        public void drawRoundRect(Vector2 pos, Vector2 size, Color color, boolean filled, int arcWidth, int arcHeight) {
            g.setColor(toAWTColor(color));
            if (pos.x - size.x < view.getWidth() &&
                    pos.x + size.x > 0 &&
                    pos.y - size.y < view.getHeight() &&
                    pos.y + size.y > 0)
                if (filled) {
                    g.fillRoundRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y, arcWidth, arcHeight);
                } else {
                    g.drawRoundRect((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y, arcWidth, arcHeight);
                }
        }

        @Override
        public void drawOval(Vector2 pos, Vector2 size, Color color, boolean filled) {
            g.setColor(toAWTColor(color));
            if (pos.x - size.x < view.getWidth() &&
                    pos.x + size.x > 0 &&
                    pos.y - size.y < view.getHeight() &&
                    pos.y + size.y > 0)
                if (filled) {
                    g.fillOval((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                } else {
                    g.drawOval((int) (pos.x - size.x / 2), (int) (pos.y - size.y / 2), (int) size.x, (int) size.y);
                }
        }

        @Override
        public void drawArc(Vector2 pos, Vector2 size, Color color, boolean filled, int startAngle, int arcAngle) {
            g.setColor(toAWTColor(color));
            if (pos.x - size.x < view.getWidth() &&
                    pos.x + size.x > 0 &&
                    pos.y - size.y < view.getHeight() &&
                    pos.y + size.y > 0)
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
            if (texture instanceof NotCapableTexture) {
                throw new NotCapableTextureException();
            }
            Image image = images.get(texture.getTextureID());
            if (pos.x - size.x < view.getWidth() &&
                    pos.x + size.x > 0 &&
                    pos.y - size.y < view.getHeight() &&
                    pos.y + size.y > 0)
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
            Class<?>[] types = new Class<?>[args.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = args[i].getClass();
            }
            try {
                SwingYield.class.getDeclaredMethod(instruction, types).invoke(swingYield, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public SampleGraphics specificGraphics() {
        return null;
    }

    @Override
    public Texture duplicate(Texture texture) {
        Texture t = new Texture(null);
        t.setVisualUtils(texture.getVisualUtils());
        t.setFilter(texture.getFilter());
        t.setInvertedX(texture.getInvertedX());
        t.setInvertedY(texture.getInvertedY());
        t.setInvertedXY(texture.getInvertedXY());
        t.setInputStream(texture.getInputStream());
        t.setUrl(texture.getUrl());
        images.put(t.getTextureID(), images.get(texture.getTextureID()));
        return t;
    }

    @Override
    public Texture overlayTexture(Texture tex1, Texture tex2, Vector2 pos1, Vector2 pos2) {
        Image i1 = images.get(tex1.getTextureID()), i2 = images.get(tex2.getTextureID());
        Texture tex = new Texture(null, tex1.getTextureType());
        loadTexture(tex, i1, (int) pos1.x, (int) pos1.y);
        Image i3 = images.get(tex.getTextureID());
        Graphics g = i3.getGraphics();
        g.drawImage(i2, (int) pos2.x, (int) pos2.y, null);
        g.dispose();
        return tex;
    }

    public void drawTexture(Texture texture, Vector2 pos, Vector2 size) {
        if (texture instanceof NotCapableTexture) {
            throw new NotCapableTextureException();
        }
        Image image = images.get(texture.getTextureID());
        g.drawImage(image, (int) (pos.x), (int) (pos.y), (int) size.x, (int) size.y, null);
    }

    public void changeWindowIcon(Texture icon) {
        Image i = images.get(icon.getTextureID());
        frame.setIconImage(i);
    }

    private float renderMod;

    @Override
    public void loadAudioPlayer(AudioPlayer player) {
        try {
            clips.put(player.getPlayerID(), AudioSystem.getClip());
        } catch (LineUnavailableException e) {
            Yld.throwException(e);
        }
    }

    @Override
    public void pausePlayer(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            clip.stop();
        }
    }

    @Override
    public void resumePlayer(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            clip.start();
        }
    }

    @Override
    public void setMicrosecondPosition(AudioPlayer audioPlayer, long pos) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            clip.setMicrosecondPosition(pos);
        }
    }

    @Override
    public long getMicrosecondPosition(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            return clip.getMicrosecondPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getMicrosecondLength(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            return clip.getMicrosecondLength();
        } else {
            return 0;
        }
    }

    @Override
    public float getVolume(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            return (float) Math.pow(10f, gainControl.getValue() / 20f);
        } else return 0;
    }

    @Override
    public void setVolume(AudioPlayer audioPlayer, float value) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(value));
        }
    }

    @Override
    public void flushPlayer(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if(clip.isOpen()) {
            clip.close();
            clips.remove(audioPlayer.getPlayerID());
        }
    }

    @Override
    public void setLoop(AudioPlayer audioPlayer, boolean loop) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.loop(0);
            }
        }
    }

    @Override
    public void setLoop(AudioPlayer audioPlayer, int count) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            clip.loop(count);
        }
    }

    @Override
    public boolean isPlayerRunning(AudioPlayer audioPlayer) {
        Clip clip = clips.get(audioPlayer.getPlayerID());
        if (clip.isOpen()) {
            return clip.isRunning();
        } else {
            return false;
        }
    }

    @Override
    public void loadAudioClip(AudioClip audioClip, AudioPlayer audioPlayer, MultiThread multiThread, YldB yldB) {
        try {
            Clip clip = clips.get(audioPlayer.getPlayerID());
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioClip.getUrl());
            clip.close();
            if (multiThread == null)
                clip.open(inputStream);
            else
                yldB.concurrent(() -> {
                    try {
                        clip.open(inputStream);
                    } catch (LineUnavailableException | IOException e) {
                        Yld.throwException(e);
                    }
                }, multiThread);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            Yld.throwException(e);
        } catch (NullPointerException e) {
            Yld.throwException(new AudioClipException("Cannot find audio file: '" + audioClip.getCachedPath() + "'"));
        }
    }

    @Override
    public SampleWindow initWindow(WindowConfiguration configuration) {
        renderMod = configuration.renderMod;
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        frame = new JFrame();
        frame.setSize(configuration.width, configuration.height);
        frame.setResizable(configuration.resizable);
        frame.setAlwaysOnTop(configuration.alwaysOnTop);
        frame.setTitle(configuration.title);
        frame.setUndecorated(configuration.undecorated);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        if (configuration.fullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setAlwaysOnTop(true);
        }
        frame.setVisible(true);
        if (!configuration.fullscreen) {
            frame.setSize(configuration.width + frame.getInsets().right + frame.getInsets().left, configuration.height + frame.getInsets().top + frame.getInsets().bottom);
        }
        frame.setLocationRelativeTo(null);
        addKeyListener(this);
        addMouseListener(this);
        if (configuration.hideMouse) {
            Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(Objects.requireNonNull(Yld.class.getResource("assets/none.png"))).getImage(), new Point(), "none");
            frame.setCursor(cursor);
        }
        changeWindowIcon(configuration.icon);
        render();
        /*if (Yld.debug) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
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

    public void render() {
        if (getBufferStrategy() == null) {
            createBufferStrategy(3);
        }
        BufferStrategy bs = getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        actual = System.currentTimeMillis();
        fps = 1000 / (float) (actual - last);
        g.setColor(java.awt.Color.BLACK);
        int w = frame.getWidth() - frame.getInsets().right - frame.getInsets().left,
                h = frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom;
        g.fillRect(0, 0, w, h);
        if (view != null) {
            if (defTransform == null)
                defTransform = ((Graphics2D) g).getTransform();
            started = true;
            Graphics2D g2 = (Graphics2D) g;
            g2.rotate(Math.toRadians(view.getTransform().rotation) * -1, w / 2f, h / 2f);
            g.drawImage(image, (int) (w / 2 - w * view.getTransform().scale.x / 2 + view.getPosition().x), (int) (h / 2 - h * view.getTransform().scale.y / 2 + view.getPosition().y), (int) (w * view.getTransform().scale.x), (int) (h * view.getTransform().scale.y), null);
        }
       /* if(!started) {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, w, h);
            g.drawImage(yieldImage, getWidth() / 2 - yieldImage.getWidth() / 2, getHeight() / 2 - yieldImage.getHeight() / 2, null);
        }*/
        g.dispose();
        bs.show();
        requestFocus();
        Toolkit.getDefaultToolkit().sync();
        last = System.currentTimeMillis();
    }
/*
    @Override
    public void update(Graphics g) {
        paintComponent(g);
    }*/

    @Override
    public void frameStart(SampleGraphics g, View view) {
        this.view = view;
        intFrame++;
        if (image != null) {
            this.g = image.getGraphics();
            this.g.setColor(toAWTColor(view.getBgColor()));
            this.g.fillRect(0, 0, view.getWidth(), view.getHeight());
        }
    }

    private int intFrame;

    @Override
    public void frameEnd() {
        if (g != null)
            g.dispose();
        if (image == null || image.getWidth(null) != view.getWidth() || image.getHeight(null) != view.getHeight()) {
            image = createVolatileImage(view.getWidth(), view.getHeight());
            Graphics gi = image.getGraphics();
            gi.setColor(java.awt.Color.BLACK);
            gi.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
            gi.dispose();
            //image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        if (intFrame % renderMod == 0)
            render();
       /* if (intFrame % renderMod == 0)
            SwingUtilities.invokeLater(this::repaint);*/
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
            InputStream in = texture.getInputStream();
            if (in == null)
                Yld.throwException(new CannotLoadException("Could not find the texture: '" + texture.getCachedPath() + "'"));
            else
                loadTexture(texture, ImageIO.read(in), 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTexture(Texture texture, Image img, int ix, int iy) {
        Image image;
        if (texture.getTextureType() == TexType.NATIVE) {
            image = createVolatileImage(img.getWidth(null), img.getHeight(null));
            Graphics g = image.getGraphics();
            g.drawImage(img, ix, iy, null);
            g.dispose();
        } else if (texture.getTextureType() == TexType.SIMULATED) {
            image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.drawImage(img, ix, iy, null);
            g.dispose();
            BufferedImage buffImage = (BufferedImage) image;
            BufferedImage imageX = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB),
                    imageY = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB),
                    imageXY = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < image.getHeight(null); y++) {
                for (int x = 0; x < image.getWidth(null); x++) {
                    imageX.setRGB((image.getWidth(null) - 1) - x, y, buffImage.getRGB(x, y));
                    imageY.setRGB(x, (image.getHeight(null) - 1) - y, buffImage.getRGB(x, y));
                    imageXY.setRGB((image.getWidth(null) - 1) - x, (image.getHeight(null) - 1) - y, buffImage.getRGB(x, y));
                }
            }
            Texture invX = new Texture(""), invY = new Texture(""), invXY = new Texture("");
            images.put(invX.getTextureID(), imageX);
            images.put(invY.getTextureID(), imageY);
            images.put(invXY.getTextureID(), imageXY);
            invX.setVisualUtils(this);
            invY.setVisualUtils(this);
            invXY.setVisualUtils(this);
            texture.setInvertedX(invX);
            texture.setInvertedY(invY);
            texture.setInvertedXY(invXY);
        } else {
            throw new InvalidTextureTypeException(texture.getTextureType().toString());
        }
        images.put(texture.getTextureID(), image);
        if (texture.getInvertedX() == null)
            texture.setInvertedX(new NotCapableTexture());
        if (texture.getInvertedY() == null)
            texture.setInvertedY(new NotCapableTexture());
        if (texture.getInvertedXY() == null)
            texture.setInvertedXY(new NotCapableTexture());
        texture.setVisualUtils(this);
    }

    @Override
    public void setPixel(Texture texture, Color color, Vector2 position) {
        try {
            ((BufferedImage) images.get(texture.getTextureID())).setRGB((int) position.x, (int) position.y, toAWTColor(color).getRGB());
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }
    }

    @Override
    public void unloadTexture(Texture texture) {
        images.remove(texture.getTextureID());
    }

    @Override
    public void clearTexture(Texture texture) {
        if (texture.getTextureType() == TexType.SIMULATED) {
            images.replace(texture.getTextureID(), new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB));
        } else {
            images.replace(texture.getTextureID(), createVolatileImage(texture.getWidth(), texture.getHeight()));
        }
    }

    @Override
    public void loadFont(String saveName, String fontName, int fontSize, int fontStyle) {
        fonts.put(saveName, new Font(fontName, fontStyle, fontSize));
    }

    @Override
    public void loadFont(String fontName, float size, float sizeToLoad, int fontFormat, InputStream inputStream) {
        try {
            fonts.put(fontName, Font.createFont(fontFormat, inputStream).deriveFont(sizeToLoad).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Texture cutTexture(Texture texture, int x, int y, int width, int height) {
        Texture tex;
        Image img;
        try {
            img = ((BufferedImage) images.get(texture.getTextureID())).getSubimage(x, y, width, height);
            tex = new Texture(null);
            loadTexture(tex, img, 0, 0);
        } catch (ClassCastException e) {
            img = createVolatileImage(width, height);
            Graphics g = img.getGraphics();
            g.drawImage(images.get(texture.getTextureID()), -x, -y, null);
            g.dispose();
            tex = new Texture(null, TexType.NATIVE);
            loadTexture(tex, img, 0, 0);
        }

        return tex;
    }

    @Override
    public Texture scaleTexture(Texture texture, int width, int height) {
        Image img = images.get(texture.getTextureID());
        Image image1;
        Texture tex;
        if (img instanceof VolatileImage) {
            image1 = createVolatileImage(width, height);
            tex = new Texture(null, TexType.NATIVE);
        } else {
            image1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            tex = new Texture(null);
        }
        Graphics g = image1.getGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        loadTexture(tex, image1, 0, 0);
        return tex;
    }

    @Override
    public Color[][] getTextureColors(Texture texture) {
        BufferedImage image1;
        try {
            image1 = (BufferedImage) images.get(texture.getTextureID());
        } catch (ClassCastException e) {
            image1 = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image1.getGraphics();
            g.drawImage(images.get(texture.getTextureID()), 0, 0, null);
            g.dispose();
        }
        Color[][] pixels = new Color[image1.getWidth()][image1.getHeight()];
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                int p = image1.getRGB(x, y);
                pixels[x][y] = (new Color(((p >> 16) & 0xff) / 255f, ((p >> 8) & 0xff) / 255f, (p & 0xff) / 255f, ((p >> 24) & 0xff) / 255f));
            }
        }
        return pixels;
    }

    @Override
    public void setTextureColors(Texture texture, Color[][] colors) {
        try {
            BufferedImage image1 = (BufferedImage) images.get(texture.getTextureID());
            for (int x = 0; x < colors.length; x++) {
                for (int y = 0; y < colors[0].length; y++) {
                    image1.setRGB(x, y, toAWTColor(colors[x][y]).getRGB());
                }
            }
        } catch (ClassCastException e) {
            Image image1 = images.get(texture.getTextureID());
            Graphics g = image1.getGraphics();
            for (int x = 0; x < colors.length; x++) {
                for (int y = 0; y < colors[0].length; y++) {
                    g.setColor(toAWTColor(colors[x][y]));
                    g.drawRect(x, y, 1, 1);
                }
            }
            g.dispose();
        }
    }

    public SwingYield getSwingYield() {
        return swingYield;
    }

    public float getRenderMod() {
        return renderMod;
    }

    public void setRenderMod(float renderMod) {
        this.renderMod = renderMod;
    }

    public int getIntFrame() {
        return intFrame;
    }

    public void setIntFrame(int intFrame) {
        this.intFrame = intFrame;
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
    public void throwException(Exception e) {
        if (JOptionPane.showOptionDialog(null,
                e.getMessage() + "\nContinue execution? This can make the running game unstable.",
                e.getClass().getSimpleName(), JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE, null, new String[]{"Yes", "No"},
                "No") == 1) {
            System.exit(1);
        }
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
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
}
