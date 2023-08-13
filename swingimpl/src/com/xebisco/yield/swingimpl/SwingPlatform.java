/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.swingimpl;

import com.xebisco.yield.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;

public class SwingPlatform implements GraphicsManager, FontManager, TextureManager, MouseCheck, SpritesheetTextureManager, InputManager, KeyListener, MouseListener, MouseWheelListener, ViewportZoomScale, ToggleFullScreen {
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private final HashSet<Input.Key> pressingKeys = new HashSet<>();
    private final HashSet<Input.MouseButton> pressingMouseButtons = new HashSet<>();
    private final Vector2D mousePosition = new Vector2D();
    private final KeyAction addKeyAction = pressingKeys::add;
    private final KeyAction removeKeyAction = pressingKeys::remove;
    private final MouseButtonAction addBtnAction = pressingMouseButtons::add;
    private final MouseButtonAction removeBtnAction = pressingMouseButtons::remove;
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private GraphicsConfiguration graphicsConfiguration = device.getDefaultConfiguration();
    private DisplayMode defaultDisplayMode;
    private JFrame frame;
    private Graphics2D graphics, fontSizeGraphics;
    private boolean stretch;

    private PlatformInit platformInit;

    private Vector2D camera;
    private SwingCanvas canvas;

    private TwoAnchorRepresentation zoomScale;

    public static Color awtColor(com.xebisco.yield.Color yieldColor) {
        return new Color(yieldColor.getARGB(), true);
    }

    public Dimension onSizeBoundary(Image image, Dimension boundary) {
        int original_width = image.getWidth(frame);
        int original_height = image.getHeight(frame);
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width;
        int new_height;

        new_height = bound_height;
        new_width = (new_height * original_width) / original_height;
        if (new_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        return new Dimension(new_width, new_height);
    }

    @Override
    public void setCamera(Vector2D camera) {
        this.camera = camera;
    }

    @Override
    public Object loadFont(com.xebisco.yield.Font font) {
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, font.getInputStream()).deriveFont((float) font.getSize());
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            font.getInputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return f;
    }

    @Override
    public void unloadFont(com.xebisco.yield.Font font) {
        font.setFontRef(null);
    }

    @Override
    public Object loadTexture(Texture texture) {
        return new SwingImage(loadAWTBufferedImage(new BufferedInputStream(texture.getInputStream())));
    }

    private BufferedImage loadAWTBufferedImage(BufferedInputStream inputStream) {
        BufferedImage i;
        try {
            i = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage out = graphicsConfiguration.createCompatibleImage(i.getWidth(), i.getHeight(), i.getTransparency());
        Graphics2D g = out.createGraphics();
        g.setBackground(TRANSPARENT);
        g.clearRect(0, 0, out.getWidth(), out.getHeight());
        g.drawImage(i, 0, 0, frame);
        g.dispose();
        i.flush();
        return out;
    }

    @Override
    public void unloadTexture(Texture texture) {
        ((Image) texture.getImageRef()).flush();
        texture.setImageRef(null);
    }

    @Override
    public int getImageWidth(Object imageRef) {
        return ((SwingImage) imageRef).originalImage().getWidth();
    }

    @Override
    public int getImageHeight(Object imageRef) {
        return ((SwingImage) imageRef).originalImage().getHeight();
    }

    @Override
    public Collection<Input.Key> getPressingKeys() {
        return pressingKeys;
    }

    @Override
    public Collection<Input.MouseButton> getPressingMouseButtons() {
        return pressingMouseButtons;
    }

    @Override
    public double getMouseX() {
        return mousePosition.getX();
    }

    @Override
    public double getMouseY() {
        return mousePosition.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        key(addKeyAction, e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        key(removeKeyAction, e);
    }

    private void key(KeyAction keyAction, KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER -> keyAction.call(Input.Key.VK_ENTER);
            case KeyEvent.VK_BACK_SPACE -> keyAction.call(Input.Key.VK_BACK_SPACE);
            case KeyEvent.VK_TAB -> keyAction.call(Input.Key.VK_TAB);
            case KeyEvent.VK_CANCEL -> keyAction.call(Input.Key.VK_CANCEL);
            case KeyEvent.VK_CLEAR -> keyAction.call(Input.Key.VK_CLEAR);
            case KeyEvent.VK_SHIFT -> keyAction.call(Input.Key.VK_SHIFT);
            case KeyEvent.VK_CONTROL -> keyAction.call(Input.Key.VK_CONTROL);
            case KeyEvent.VK_ALT -> keyAction.call(Input.Key.VK_ALT);
            case KeyEvent.VK_PAUSE -> keyAction.call(Input.Key.VK_PAUSE);
            case KeyEvent.VK_CAPS_LOCK -> keyAction.call(Input.Key.VK_CAPS_LOCK);
            case KeyEvent.VK_ESCAPE -> keyAction.call(Input.Key.VK_ESCAPE);
            case KeyEvent.VK_SPACE -> keyAction.call(Input.Key.VK_SPACE);
            case KeyEvent.VK_PAGE_UP -> keyAction.call(Input.Key.VK_PAGE_UP);
            case KeyEvent.VK_PAGE_DOWN -> keyAction.call(Input.Key.VK_PAGE_DOWN);
            case KeyEvent.VK_END -> keyAction.call(Input.Key.VK_END);
            case KeyEvent.VK_HOME -> keyAction.call(Input.Key.VK_HOME);
            case KeyEvent.VK_LEFT -> keyAction.call(Input.Key.VK_LEFT);
            case KeyEvent.VK_UP -> keyAction.call(Input.Key.VK_UP);
            case KeyEvent.VK_RIGHT -> keyAction.call(Input.Key.VK_RIGHT);
            case KeyEvent.VK_DOWN -> keyAction.call(Input.Key.VK_DOWN);
            case KeyEvent.VK_COMMA -> keyAction.call(Input.Key.VK_COMMA);
            case KeyEvent.VK_MINUS -> keyAction.call(Input.Key.VK_MINUS);
            case KeyEvent.VK_PERIOD -> keyAction.call(Input.Key.VK_PERIOD);
            case KeyEvent.VK_SLASH -> keyAction.call(Input.Key.VK_SLASH);
            case KeyEvent.VK_0 -> keyAction.call(Input.Key.VK_0);
            case KeyEvent.VK_1 -> keyAction.call(Input.Key.VK_1);
            case KeyEvent.VK_2 -> keyAction.call(Input.Key.VK_2);
            case KeyEvent.VK_3 -> keyAction.call(Input.Key.VK_3);
            case KeyEvent.VK_4 -> keyAction.call(Input.Key.VK_4);
            case KeyEvent.VK_5 -> keyAction.call(Input.Key.VK_5);
            case KeyEvent.VK_6 -> keyAction.call(Input.Key.VK_6);
            case KeyEvent.VK_7 -> keyAction.call(Input.Key.VK_7);
            case KeyEvent.VK_8 -> keyAction.call(Input.Key.VK_8);
            case KeyEvent.VK_9 -> keyAction.call(Input.Key.VK_9);
            case KeyEvent.VK_SEMICOLON -> keyAction.call(Input.Key.VK_SEMICOLON);
            case KeyEvent.VK_EQUALS -> keyAction.call(Input.Key.VK_EQUALS);
            case KeyEvent.VK_A -> keyAction.call(Input.Key.VK_A);
            case KeyEvent.VK_B -> keyAction.call(Input.Key.VK_B);
            case KeyEvent.VK_C -> keyAction.call(Input.Key.VK_C);
            case KeyEvent.VK_D -> keyAction.call(Input.Key.VK_D);
            case KeyEvent.VK_E -> keyAction.call(Input.Key.VK_E);
            case KeyEvent.VK_F -> keyAction.call(Input.Key.VK_F);
            case KeyEvent.VK_G -> keyAction.call(Input.Key.VK_G);
            case KeyEvent.VK_H -> keyAction.call(Input.Key.VK_H);
            case KeyEvent.VK_I -> keyAction.call(Input.Key.VK_I);
            case KeyEvent.VK_J -> keyAction.call(Input.Key.VK_J);
            case KeyEvent.VK_K -> keyAction.call(Input.Key.VK_K);
            case KeyEvent.VK_L -> keyAction.call(Input.Key.VK_L);
            case KeyEvent.VK_M -> keyAction.call(Input.Key.VK_M);
            case KeyEvent.VK_N -> keyAction.call(Input.Key.VK_N);
            case KeyEvent.VK_O -> keyAction.call(Input.Key.VK_O);
            case KeyEvent.VK_P -> keyAction.call(Input.Key.VK_P);
            case KeyEvent.VK_Q -> keyAction.call(Input.Key.VK_Q);
            case KeyEvent.VK_R -> keyAction.call(Input.Key.VK_R);
            case KeyEvent.VK_S -> keyAction.call(Input.Key.VK_S);
            case KeyEvent.VK_T -> keyAction.call(Input.Key.VK_T);
            case KeyEvent.VK_U -> keyAction.call(Input.Key.VK_U);
            case KeyEvent.VK_V -> keyAction.call(Input.Key.VK_V);
            case KeyEvent.VK_W -> keyAction.call(Input.Key.VK_W);
            case KeyEvent.VK_X -> keyAction.call(Input.Key.VK_X);
            case KeyEvent.VK_Y -> keyAction.call(Input.Key.VK_Y);
            case KeyEvent.VK_Z -> keyAction.call(Input.Key.VK_Z);
            case KeyEvent.VK_OPEN_BRACKET -> keyAction.call(Input.Key.VK_OPEN_BRACKET);
            case KeyEvent.VK_BACK_SLASH -> keyAction.call(Input.Key.VK_BACK_SLASH);
            case KeyEvent.VK_CLOSE_BRACKET -> keyAction.call(Input.Key.VK_CLOSE_BRACKET);
            case KeyEvent.VK_NUMPAD0 -> keyAction.call(Input.Key.VK_NUMPAD0);
            case KeyEvent.VK_NUMPAD1 -> keyAction.call(Input.Key.VK_NUMPAD1);
            case KeyEvent.VK_NUMPAD2 -> keyAction.call(Input.Key.VK_NUMPAD2);
            case KeyEvent.VK_NUMPAD3 -> keyAction.call(Input.Key.VK_NUMPAD3);
            case KeyEvent.VK_NUMPAD4 -> keyAction.call(Input.Key.VK_NUMPAD4);
            case KeyEvent.VK_NUMPAD5 -> keyAction.call(Input.Key.VK_NUMPAD5);
            case KeyEvent.VK_NUMPAD6 -> keyAction.call(Input.Key.VK_NUMPAD6);
            case KeyEvent.VK_NUMPAD7 -> keyAction.call(Input.Key.VK_NUMPAD7);
            case KeyEvent.VK_NUMPAD8 -> keyAction.call(Input.Key.VK_NUMPAD8);
            case KeyEvent.VK_NUMPAD9 -> keyAction.call(Input.Key.VK_NUMPAD9);
            case KeyEvent.VK_MULTIPLY -> keyAction.call(Input.Key.VK_MULTIPLY);
            case KeyEvent.VK_ADD -> keyAction.call(Input.Key.VK_ADD);
            case KeyEvent.VK_SEPARATOR -> keyAction.call(Input.Key.VK_SEPARATOR);
            case KeyEvent.VK_SUBTRACT -> keyAction.call(Input.Key.VK_SUBTRACT);
            case KeyEvent.VK_DECIMAL -> keyAction.call(Input.Key.VK_DECIMAL);
            case KeyEvent.VK_DIVIDE -> keyAction.call(Input.Key.VK_DIVIDE);
            case KeyEvent.VK_DELETE -> keyAction.call(Input.Key.VK_DELETE);
            case KeyEvent.VK_NUM_LOCK -> keyAction.call(Input.Key.VK_NUM_LOCK);
            case KeyEvent.VK_SCROLL_LOCK -> keyAction.call(Input.Key.VK_SCROLL_LOCK);
            case KeyEvent.VK_F1 -> keyAction.call(Input.Key.VK_F1);
            case KeyEvent.VK_F2 -> keyAction.call(Input.Key.VK_F2);
            case KeyEvent.VK_F3 -> keyAction.call(Input.Key.VK_F3);
            case KeyEvent.VK_F4 -> keyAction.call(Input.Key.VK_F4);
            case KeyEvent.VK_F5 -> keyAction.call(Input.Key.VK_F5);
            case KeyEvent.VK_F6 -> keyAction.call(Input.Key.VK_F6);
            case KeyEvent.VK_F7 -> keyAction.call(Input.Key.VK_F7);
            case KeyEvent.VK_F8 -> keyAction.call(Input.Key.VK_F8);
            case KeyEvent.VK_F9 -> keyAction.call(Input.Key.VK_F9);
            case KeyEvent.VK_F10 -> keyAction.call(Input.Key.VK_F10);
            case KeyEvent.VK_F11 -> keyAction.call(Input.Key.VK_F11);
            case KeyEvent.VK_F12 -> keyAction.call(Input.Key.VK_F12);
            case KeyEvent.VK_PRINTSCREEN -> keyAction.call(Input.Key.VK_PRINTSCREEN);
            case KeyEvent.VK_INSERT -> keyAction.call(Input.Key.VK_INSERT);
            case KeyEvent.VK_HELP -> keyAction.call(Input.Key.VK_HELP);
            case KeyEvent.VK_META -> keyAction.call(Input.Key.VK_META);
            case KeyEvent.VK_BACK_QUOTE -> keyAction.call(Input.Key.VK_BACK_QUOTE);
            case KeyEvent.VK_QUOTE -> keyAction.call(Input.Key.VK_QUOTE);
            case KeyEvent.VK_KP_UP -> keyAction.call(Input.Key.VK_KP_UP);
            case KeyEvent.VK_KP_DOWN -> keyAction.call(Input.Key.VK_KP_DOWN);
            case KeyEvent.VK_KP_LEFT -> keyAction.call(Input.Key.VK_KP_LEFT);
            case KeyEvent.VK_KP_RIGHT -> keyAction.call(Input.Key.VK_KP_RIGHT);
            case KeyEvent.VK_AMPERSAND -> keyAction.call(Input.Key.VK_AMPERSAND);
            case KeyEvent.VK_ASTERISK -> keyAction.call(Input.Key.VK_ASTERISK);
            case KeyEvent.VK_QUOTEDBL -> keyAction.call(Input.Key.VK_QUOTEDBL);
            case KeyEvent.VK_LESS -> keyAction.call(Input.Key.VK_LESS);
            case KeyEvent.VK_GREATER -> keyAction.call(Input.Key.VK_GREATER);
            case KeyEvent.VK_BRACELEFT -> keyAction.call(Input.Key.VK_BRACELEFT);
            case KeyEvent.VK_BRACERIGHT -> keyAction.call(Input.Key.VK_BRACERIGHT);
            case KeyEvent.VK_AT -> keyAction.call(Input.Key.VK_AT);
            case KeyEvent.VK_COLON -> keyAction.call(Input.Key.VK_COLON);
            case KeyEvent.VK_CIRCUMFLEX -> keyAction.call(Input.Key.VK_CIRCUMFLEX);
            case KeyEvent.VK_DOLLAR -> keyAction.call(Input.Key.VK_DOLLAR);
            case KeyEvent.VK_EURO_SIGN -> keyAction.call(Input.Key.VK_EURO_SIGN);
            case KeyEvent.VK_EXCLAMATION_MARK -> keyAction.call(Input.Key.VK_EXCLAMATION_MARK);
            case KeyEvent.VK_INVERTED_EXCLAMATION_MARK -> keyAction.call(Input.Key.VK_INVERTED_EXCLAMATION_MARK);
            case KeyEvent.VK_LEFT_PARENTHESIS -> keyAction.call(Input.Key.VK_LEFT_PARENTHESIS);
            case KeyEvent.VK_NUMBER_SIGN -> keyAction.call(Input.Key.VK_NUMBER_SIGN);
            case KeyEvent.VK_PLUS -> keyAction.call(Input.Key.VK_PLUS);
            case KeyEvent.VK_RIGHT_PARENTHESIS -> keyAction.call(Input.Key.VK_RIGHT_PARENTHESIS);
            case KeyEvent.VK_UNDERSCORE -> keyAction.call(Input.Key.VK_UNDERSCORE);
            case KeyEvent.VK_WINDOWS -> keyAction.call(Input.Key.VK_WINDOWS);
            case KeyEvent.VK_CONTEXT_MENU -> keyAction.call(Input.Key.VK_CONTEXT_MENU);
        }
    }

    @Override
    public void dispose() {
        frame.dispose();
        frame = null;
    }

    @Override
    public void init(PlatformInit platformInit) {
        this.platformInit = platformInit;

        fontSizeGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();

        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        stretch = platformInit.isStretchViewport();


        canvas = new SwingCanvas();

        if (frame == null) {
            frame = new JFrame(graphicsConfiguration);
            frame.add(canvas);
        }

        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(platformInit.getTitle());

        updateScreenState(platformInit.isFullscreen() && device.isFullScreenSupported(), platformInit.isUndecorated());

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(frame.getWidth(), frame.getHeight() + frame.getInsets().top);
        canvas.requestFocus();

        canvas.createBufferStrategy(2);
    }

    public void updateScreenState(boolean fullscreen, boolean undecorated) {
        if (frame.isDisplayable())
            frame.dispose();
        if (fullscreen) {
            frame.setUndecorated(true);
            if (device.isDisplayChangeSupported())
                device.setDisplayMode(new DisplayMode((int) platformInit.getViewportSize().getWidth(), (int) platformInit.getViewportSize().getHeight(), device.getDisplayMode().getBitDepth(), device.getDisplayMode().getRefreshRate()));
            device.setFullScreenWindow(frame);
        } else {
            frame.setUndecorated(undecorated);
            frame.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight());
            if (device.isDisplayChangeSupported())
                device.setDisplayMode(defaultDisplayMode);
        }
    }

    @Override
    public void updateWindowIcon(Texture icon) {
        frame.setIconImage(((SwingImage) icon.getImageRef()).originalImage());
    }

    public void frame() {
        graphics = canvas.prepareRender();
        if (stretch)
            graphics.scale(canvas.getWidth() / platformInit.getViewportSize().getWidth(), canvas.getHeight() / platformInit.getViewportSize().getHeight());
        else {
            Size2D viewport = Global.onSizeBoundary(platformInit.getViewportSize(), new Size2D(canvas.getWidth(), canvas.getHeight()));
            graphics.translate((int) (canvas.getWidth() / 2 - viewport.getWidth() / 2), (int) (canvas.getHeight() / 2 - viewport.getHeight() / 2));
            graphics.scale(viewport.getWidth() / platformInit.getViewportSize().getWidth(), viewport.getHeight() / platformInit.getViewportSize().getHeight());
        }

        graphics.translate(platformInit.getViewportSize().getWidth() / 2, platformInit.getViewportSize().getHeight() / 2);

        Point mouse = canvas.getMousePosition();
        if (mouse != null)
            mousePosition.set(mouse.getX() / canvas.getWidth() * platformInit.getViewportSize().getWidth() - platformInit.getViewportSize().getWidth() / 2., -mouse.getY() / canvas.getHeight() * platformInit.getViewportSize().getHeight() + platformInit.getViewportSize().getHeight() / 2.);
    }

    @Override
    public void draw(List<DrawInstruction> drawInstructions) {
        if (!frame.isVisible())
            return;

        frame();

        try {
            for (DrawInstruction drawInstruction : drawInstructions) {
                AffineTransform savedTransform = new AffineTransform(graphics.getTransform());

                if (!drawInstruction.ignoreViewportScale())
                    graphics.scale(zoomScale.getX(), zoomScale.getY());

                if (!drawInstruction.ignoreCameraPosition())
                    graphics.translate(-camera.getX(), camera.getY());

                draw(drawInstruction);
                graphics.setTransform(savedTransform);
            }
        } catch (ConcurrentModificationException ignore) {
        }

        graphics.dispose();
        canvas.finishRender(graphics);
    }

    public void draw(DrawInstruction drawInstruction) {
        AffineTransform savedTransform = new AffineTransform(graphics.getTransform());

        graphics.translate(drawInstruction.x(), -drawInstruction.y());
        graphics.translate(drawInstruction.centerOffsetX(), -drawInstruction.centerOffsetY());

        if (drawInstruction.rotateBeforeScale()) {
            graphics.rotate(Math.toRadians(-drawInstruction.rotation()), 0, 0);
            graphics.scale(drawInstruction.scaleX(), drawInstruction.scaleY());
        } else {
            graphics.scale(drawInstruction.scaleX(), drawInstruction.scaleY());
            graphics.rotate(Math.toRadians(-drawInstruction.rotation()), 0, 0);
        }
        graphics.translate(-drawInstruction.centerOffsetX(), drawInstruction.centerOffsetY());


        if (drawInstruction.verticesX() == null && drawInstruction.verticesY() == null) {
            if (drawInstruction.color() != null) {
                graphics.setColor(awtColor(drawInstruction.color()));
                AffineTransform t = graphics.getTransform();
                graphics.setTransform(new AffineTransform());
                graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                graphics.setTransform(t);
            }
        } else {

            graphics.setClip(null);
            Composite savedComposite = graphics.getComposite();
            graphics.setColor(awtColor(drawInstruction.color()));
            graphics.setFont(null);

            if (drawInstruction.text() != null && drawInstruction.fontRef() != null) {
                graphics.setFont((Font) drawInstruction.fontRef());
                graphics.drawString(drawInstruction.text(), -graphics.getFontMetrics().stringWidth(drawInstruction.text()) / 2, graphics.getFont().getSize() / 4);
            } else if (drawInstruction.imageRef() != null) {
                SwingImage swingImage = (SwingImage) drawInstruction.imageRef();
                swingImage.updateImage(drawInstruction.color());
                Image image = swingImage.filteredImage();


                Polygon p = new Polygon(drawInstruction.verticesX(), negateIntArray(drawInstruction.verticesY()), drawInstruction.verticesX().length);
                graphics.setClip(p);
                int[] vx = drawInstruction.verticesX(), vy = negateIntArray(drawInstruction.verticesY());
                int lx = vx[0], ly = vy[0], hx = lx, hy = ly;

                for (int i = 0; i < vx.length; i++) {
                    int x = vx[i], y = vy[i];
                    if (x < lx)
                        lx = x;
                    if (y < ly)
                        ly = y;
                    if (x > hx)
                        hx = x;
                    if (y > hy)
                        hy = y;
                }

                graphics.drawImage(image, lx, ly, hx - lx, hy - ly, canvas);
            } else {
                if (drawInstruction.stroke() == 0)
                    graphics.fillPolygon(drawInstruction.verticesX(), negateIntArray(drawInstruction.verticesY()), drawInstruction.verticesX().length);
                else {
                    graphics.setStroke(new BasicStroke((float) drawInstruction.stroke()));
                    graphics.drawPolygon(drawInstruction.verticesX(), negateIntArray(drawInstruction.verticesY()), drawInstruction.verticesX().length);
                }
            }


            graphics.setComposite(savedComposite);
        }

        for (DrawInstruction di : drawInstruction.childrenInstructions()) {
            draw(di);
        }

        graphics.setTransform(savedTransform);
    }

    private int[] negateIntArray(int[] array) {
        final int[] newArray = new int[array.length];
        for (int i = 0; i < newArray.length; i++)
            newArray[i] = -array[i];
        return newArray;
    }

    @Override
    public boolean shouldClose() {
        if (frame == null)
            return true;
        return !frame.isDisplayable();
    }

    @Override
    public double getStringWidth(String text, Object fontRef) {
        return fontSizeGraphics.getFontMetrics((Font) fontRef).stringWidth(text);
    }

    @Override
    public double getStringHeight(String text, Object fontRef) {
        return fontSizeGraphics.getFontMetrics((Font) fontRef).getHeight();
    }

    public GraphicsDevice getDevice() {
        return device;
    }

    public void setDevice(GraphicsDevice device) {
        this.device = device;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return graphicsConfiguration;
    }

    public void setGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        this.graphicsConfiguration = graphicsConfiguration;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> pressingMouseButtons.add(Input.MouseButton.BUTTON1);
            case MouseEvent.BUTTON2 -> pressingMouseButtons.add(Input.MouseButton.BUTTON2);
            case MouseEvent.BUTTON3 -> pressingMouseButtons.add(Input.MouseButton.BUTTON3);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> pressingMouseButtons.remove(Input.MouseButton.BUTTON1);
            case MouseEvent.BUTTON2 -> pressingMouseButtons.remove(Input.MouseButton.BUTTON2);
            case MouseEvent.BUTTON3 -> pressingMouseButtons.remove(Input.MouseButton.BUTTON3);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            pressingMouseButtons.add(Input.MouseButton.SCROLL_UP);
        } else {
            pressingMouseButtons.add(Input.MouseButton.SCROLL_DOWN);
        }
    }

    public Vector2D getMousePosition() {
        return mousePosition;
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    @Override
    public void setZoomScale(TwoAnchorRepresentation zoomScale) {
        this.zoomScale = zoomScale;
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        if (platformInit != null) {
            platformInit.setFullscreen(fullScreen);
            if (frame != null) {
                updateScreenState(fullScreen, platformInit.isUndecorated());
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        }
    }

    @Override
    public Object loadSpritesheetTexture(SpritesheetTexture spritesheetTexture) {
        return loadAWTBufferedImage(new BufferedInputStream(spritesheetTexture.getInputStream()));
    }

    @Override
    public void unloadSpritesheetTexture(SpritesheetTexture spritesheetTexture) {
        ((Image) spritesheetTexture.getSpritesheetImageRef()).flush();
        spritesheetTexture.setSpritesheetImageRef(null);
    }

    @Override
    public Texture getTextureFromRegion(int x, int y, int width, int height, SpritesheetTexture spritesheetTexture) {
        return new Texture(((BufferedImage) spritesheetTexture.getSpritesheetImageRef()).getSubimage(x, y, width, height), null, this);
    }

    @Override
    public int getSpritesheetImageWidth(Object imageRef) {
        return ((BufferedImage) imageRef).getWidth();
    }

    @Override
    public int getSpritesheetImageHeight(Object imageRef) {
        return ((BufferedImage) imageRef).getHeight();
    }

    private interface KeyAction {
        void call(Input.Key key);
    }

    private interface MouseButtonAction {
        void call(Input.MouseButton button);
    }
}
