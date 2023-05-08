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
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class SwingPlatform implements PlatformGraphics, FontLoader, TextureManager, InputManager, KeyListener, MouseListener, MouseWheelListener, ViewportZoomScale, ToggleFullScreen {
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private final HashSet<Input.Key> pressingKeys = new HashSet<>();
    private final HashSet<Input.MouseButton> pressingMouseButtons = new HashSet<>();
    private final Vector2D mousePosition = new Vector2D();
    private final KeyAction addKeyAction = pressingKeys::add;
    private final KeyAction removeKeyAction = pressingKeys::remove;
    private final Dimension bounds = new Dimension();
    private final MouseButtonAction addBtnAction = pressingMouseButtons::add;
    private final MouseButtonAction removeBtnAction = pressingMouseButtons::remove;
    private Image viewBuffer;
    private GraphicsDevice device;
    private GraphicsConfiguration graphicsConfiguration;
    private JComponent canvas;
    private JFrame frame;
    private Graphics2D graphics;
    private AffineTransform defaultTransform = new AffineTransform();
    private boolean stretch, verticalSync;

    private PlatformInit platformInit;

    private Vector2D camera;

    private TwoAnchorRepresentation zoomScale = new TwoAnchorRepresentation(1, 1);

    public SwingPlatform() {
        System.setProperty("sun.java2d.opengl", "True");
        System.setProperty("sun.java2d.accthreshold", "0");
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        graphicsConfiguration = device.getDefaultConfiguration();
    }

    public static Color awtColor(com.xebisco.yield.Color yieldColor) {
        return new Color(yieldColor.getARGB(), true);
    }

    public Dimension onSizeBoundary(Image image, Dimension boundary) {
        int original_width = image.getWidth(canvas);
        int original_height = image.getHeight(canvas);
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
    public Vector2D getCamera() {
        return camera;
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
        BufferedImage i;
        try {
            i = ImageIO.read(texture.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            texture.getInputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage out = graphicsConfiguration.createCompatibleImage(i.getWidth(), i.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g = out.createGraphics();
        g.setBackground(TRANSPARENT);
        g.clearRect(0, 0, out.getWidth(), out.getHeight());
        g.drawImage(i, 0, 0, canvas);
        g.dispose();
        i.flush();
        return out;
    }

    @Override
    public void unloadTexture(Texture texture) {
        ((Image) texture.getImageRef()).flush();
        texture.setImageRef(null);
    }

   /* @Override
    public void setPixel(Object imageRef, com.xebisco.yield.Color color, int x, int y) {
        BufferedImage image = (BufferedImage) imageRef;
        image.setRGB(x, y, color.getARGB());
    }

    @Override
    public int[] getPixel(Object imageRef, int x, int y) {
        BufferedImage image = (BufferedImage) imageRef;
        return image.getRaster().getPixel(x, y, new int[4]);
    }*/

    @Override
    public int getImageWidth(Object imageRef) {
        return ((BufferedImage) imageRef).getWidth();
    }

    @Override
    public int getImageHeight(Object imageRef) {
        return ((BufferedImage) imageRef).getHeight();
    }

    /*@Override
    public Texture cropTexture(Object imageRef, int x, int y, int w, int h) {
        BufferedImage image = ((BufferedImage) imageRef).getSubimage(x, y, w, h);
        return new Texture(image, null, this);
    }

    @Override
    public Texture scaledTexture(Object imageRef, int w, int h) {
        BufferedImage image = graphicsConfiguration.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics g = image.getGraphics();
        g.drawImage((Image) imageRef, 0, 0, w, h, canvas);
        g.dispose();
        return new Texture(image, null, this);
    }*/

    @Override
    public Texture printScreenTexture() {
        BufferedImage image = graphicsConfiguration.createCompatibleImage(viewBuffer.getWidth(canvas), viewBuffer.getHeight(canvas), Transparency.TRANSLUCENT);
        Graphics g = image.getGraphics();
        g.drawImage(viewBuffer, 0, 0, canvas);
        g.dispose();
        return new Texture(image, null, this);
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

    public void key(KeyAction keyAction, KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                keyAction.call(Input.Key.VK_ENTER);
                break;
            case KeyEvent.VK_BACK_SPACE:
                keyAction.call(Input.Key.VK_BACK_SPACE);
                break;
            case KeyEvent.VK_TAB:
                keyAction.call(Input.Key.VK_TAB);
                break;
            case KeyEvent.VK_CANCEL:
                keyAction.call(Input.Key.VK_CANCEL);
                break;
            case KeyEvent.VK_CLEAR:
                keyAction.call(Input.Key.VK_CLEAR);
                break;
            case KeyEvent.VK_SHIFT:
                keyAction.call(Input.Key.VK_SHIFT);
                break;
            case KeyEvent.VK_CONTROL:
                keyAction.call(Input.Key.VK_CONTROL);
                break;
            case KeyEvent.VK_ALT:
                keyAction.call(Input.Key.VK_ALT);
                break;
            case KeyEvent.VK_PAUSE:
                keyAction.call(Input.Key.VK_PAUSE);
                break;
            case KeyEvent.VK_CAPS_LOCK:
                keyAction.call(Input.Key.VK_CAPS_LOCK);
                break;
            case KeyEvent.VK_ESCAPE:
                keyAction.call(Input.Key.VK_ESCAPE);
                break;
            case KeyEvent.VK_SPACE:
                keyAction.call(Input.Key.VK_SPACE);
                break;
            case KeyEvent.VK_PAGE_UP:
                keyAction.call(Input.Key.VK_PAGE_UP);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                keyAction.call(Input.Key.VK_PAGE_DOWN);
                break;
            case KeyEvent.VK_END:
                keyAction.call(Input.Key.VK_END);
                break;
            case KeyEvent.VK_HOME:
                keyAction.call(Input.Key.VK_HOME);
                break;
            case KeyEvent.VK_LEFT:
                keyAction.call(Input.Key.VK_LEFT);
                break;
            case KeyEvent.VK_UP:
                keyAction.call(Input.Key.VK_UP);
                break;
            case KeyEvent.VK_RIGHT:
                keyAction.call(Input.Key.VK_RIGHT);
                break;
            case KeyEvent.VK_DOWN:
                keyAction.call(Input.Key.VK_DOWN);
                break;
            case KeyEvent.VK_COMMA:
                keyAction.call(Input.Key.VK_COMMA);
                break;
            case KeyEvent.VK_MINUS:
                keyAction.call(Input.Key.VK_MINUS);
                break;
            case KeyEvent.VK_PERIOD:
                keyAction.call(Input.Key.VK_PERIOD);
                break;
            case KeyEvent.VK_SLASH:
                keyAction.call(Input.Key.VK_SLASH);
                break;
            case KeyEvent.VK_0:
                keyAction.call(Input.Key.VK_0);
                break;
            case KeyEvent.VK_1:
                keyAction.call(Input.Key.VK_1);
                break;
            case KeyEvent.VK_2:
                keyAction.call(Input.Key.VK_2);
                break;
            case KeyEvent.VK_3:
                keyAction.call(Input.Key.VK_3);
                break;
            case KeyEvent.VK_4:
                keyAction.call(Input.Key.VK_4);
                break;
            case KeyEvent.VK_5:
                keyAction.call(Input.Key.VK_5);
                break;
            case KeyEvent.VK_6:
                keyAction.call(Input.Key.VK_6);
                break;
            case KeyEvent.VK_7:
                keyAction.call(Input.Key.VK_7);
                break;
            case KeyEvent.VK_8:
                keyAction.call(Input.Key.VK_8);
                break;
            case KeyEvent.VK_9:
                keyAction.call(Input.Key.VK_9);
                break;
            case KeyEvent.VK_SEMICOLON:
                keyAction.call(Input.Key.VK_SEMICOLON);
                break;
            case KeyEvent.VK_EQUALS:
                keyAction.call(Input.Key.VK_EQUALS);
                break;
            case KeyEvent.VK_A:
                keyAction.call(Input.Key.VK_A);
                break;
            case KeyEvent.VK_B:
                keyAction.call(Input.Key.VK_B);
                break;
            case KeyEvent.VK_C:
                keyAction.call(Input.Key.VK_C);
                break;
            case KeyEvent.VK_D:
                keyAction.call(Input.Key.VK_D);
                break;
            case KeyEvent.VK_E:
                keyAction.call(Input.Key.VK_E);
                break;
            case KeyEvent.VK_F:
                keyAction.call(Input.Key.VK_F);
                break;
            case KeyEvent.VK_G:
                keyAction.call(Input.Key.VK_G);
                break;
            case KeyEvent.VK_H:
                keyAction.call(Input.Key.VK_H);
                break;
            case KeyEvent.VK_I:
                keyAction.call(Input.Key.VK_I);
                break;
            case KeyEvent.VK_J:
                keyAction.call(Input.Key.VK_J);
                break;
            case KeyEvent.VK_K:
                keyAction.call(Input.Key.VK_K);
                break;
            case KeyEvent.VK_L:
                keyAction.call(Input.Key.VK_L);
                break;
            case KeyEvent.VK_M:
                keyAction.call(Input.Key.VK_M);
                break;
            case KeyEvent.VK_N:
                keyAction.call(Input.Key.VK_N);
                break;
            case KeyEvent.VK_O:
                keyAction.call(Input.Key.VK_O);
                break;
            case KeyEvent.VK_P:
                keyAction.call(Input.Key.VK_P);
                break;
            case KeyEvent.VK_Q:
                keyAction.call(Input.Key.VK_Q);
                break;
            case KeyEvent.VK_R:
                keyAction.call(Input.Key.VK_R);
                break;
            case KeyEvent.VK_S:
                keyAction.call(Input.Key.VK_S);
                break;
            case KeyEvent.VK_T:
                keyAction.call(Input.Key.VK_T);
                break;
            case KeyEvent.VK_U:
                keyAction.call(Input.Key.VK_U);
                break;
            case KeyEvent.VK_V:
                keyAction.call(Input.Key.VK_V);
                break;
            case KeyEvent.VK_W:
                keyAction.call(Input.Key.VK_W);
                break;
            case KeyEvent.VK_X:
                keyAction.call(Input.Key.VK_X);
                break;
            case KeyEvent.VK_Y:
                keyAction.call(Input.Key.VK_Y);
                break;
            case KeyEvent.VK_Z:
                keyAction.call(Input.Key.VK_Z);
                break;
            case KeyEvent.VK_OPEN_BRACKET:
                keyAction.call(Input.Key.VK_OPEN_BRACKET);
                break;
            case KeyEvent.VK_BACK_SLASH:
                keyAction.call(Input.Key.VK_BACK_SLASH);
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                keyAction.call(Input.Key.VK_CLOSE_BRACKET);
                break;
            case KeyEvent.VK_NUMPAD0:
                keyAction.call(Input.Key.VK_NUMPAD0);
                break;
            case KeyEvent.VK_NUMPAD1:
                keyAction.call(Input.Key.VK_NUMPAD1);
                break;
            case KeyEvent.VK_NUMPAD2:
                keyAction.call(Input.Key.VK_NUMPAD2);
                break;
            case KeyEvent.VK_NUMPAD3:
                keyAction.call(Input.Key.VK_NUMPAD3);
                break;
            case KeyEvent.VK_NUMPAD4:
                keyAction.call(Input.Key.VK_NUMPAD4);
                break;
            case KeyEvent.VK_NUMPAD5:
                keyAction.call(Input.Key.VK_NUMPAD5);
                break;
            case KeyEvent.VK_NUMPAD6:
                keyAction.call(Input.Key.VK_NUMPAD6);
                break;
            case KeyEvent.VK_NUMPAD7:
                keyAction.call(Input.Key.VK_NUMPAD7);
                break;
            case KeyEvent.VK_NUMPAD8:
                keyAction.call(Input.Key.VK_NUMPAD8);
                break;
            case KeyEvent.VK_NUMPAD9:
                keyAction.call(Input.Key.VK_NUMPAD9);
                break;
            case KeyEvent.VK_MULTIPLY:
                keyAction.call(Input.Key.VK_MULTIPLY);
                break;
            case KeyEvent.VK_ADD:
                keyAction.call(Input.Key.VK_ADD);
                break;
            case KeyEvent.VK_SEPARATOR:
                keyAction.call(Input.Key.VK_SEPARATOR);
                break;
            case KeyEvent.VK_SUBTRACT:
                keyAction.call(Input.Key.VK_SUBTRACT);
                break;
            case KeyEvent.VK_DECIMAL:
                keyAction.call(Input.Key.VK_DECIMAL);
                break;
            case KeyEvent.VK_DIVIDE:
                keyAction.call(Input.Key.VK_DIVIDE);
                break;
            case KeyEvent.VK_DELETE:
                keyAction.call(Input.Key.VK_DELETE);
                break;
            case KeyEvent.VK_NUM_LOCK:
                keyAction.call(Input.Key.VK_NUM_LOCK);
                break;
            case KeyEvent.VK_SCROLL_LOCK:
                keyAction.call(Input.Key.VK_SCROLL_LOCK);
                break;
            case KeyEvent.VK_F1:
                keyAction.call(Input.Key.VK_F1);
                break;
            case KeyEvent.VK_F2:
                keyAction.call(Input.Key.VK_F2);
                break;
            case KeyEvent.VK_F3:
                keyAction.call(Input.Key.VK_F3);
                break;
            case KeyEvent.VK_F4:
                keyAction.call(Input.Key.VK_F4);
                break;
            case KeyEvent.VK_F5:
                keyAction.call(Input.Key.VK_F5);
                break;
            case KeyEvent.VK_F6:
                keyAction.call(Input.Key.VK_F6);
                break;
            case KeyEvent.VK_F7:
                keyAction.call(Input.Key.VK_F7);
                break;
            case KeyEvent.VK_F8:
                keyAction.call(Input.Key.VK_F8);
                break;
            case KeyEvent.VK_F9:
                keyAction.call(Input.Key.VK_F9);
                break;
            case KeyEvent.VK_F10:
                keyAction.call(Input.Key.VK_F10);
                break;
            case KeyEvent.VK_F11:
                keyAction.call(Input.Key.VK_F11);
                break;
            case KeyEvent.VK_F12:
                keyAction.call(Input.Key.VK_F12);
                break;
            case KeyEvent.VK_PRINTSCREEN:
                keyAction.call(Input.Key.VK_PRINTSCREEN);
                break;
            case KeyEvent.VK_INSERT:
                keyAction.call(Input.Key.VK_INSERT);
                break;
            case KeyEvent.VK_HELP:
                keyAction.call(Input.Key.VK_HELP);
                break;
            case KeyEvent.VK_META:
                keyAction.call(Input.Key.VK_META);
                break;
            case KeyEvent.VK_BACK_QUOTE:
                keyAction.call(Input.Key.VK_BACK_QUOTE);
                break;
            case KeyEvent.VK_QUOTE:
                keyAction.call(Input.Key.VK_QUOTE);
                break;
            case KeyEvent.VK_KP_UP:
                keyAction.call(Input.Key.VK_KP_UP);
                break;
            case KeyEvent.VK_KP_DOWN:
                keyAction.call(Input.Key.VK_KP_DOWN);
                break;
            case KeyEvent.VK_KP_LEFT:
                keyAction.call(Input.Key.VK_KP_LEFT);
                break;
            case KeyEvent.VK_KP_RIGHT:
                keyAction.call(Input.Key.VK_KP_RIGHT);
                break;
            case KeyEvent.VK_AMPERSAND:
                keyAction.call(Input.Key.VK_AMPERSAND);
                break;
            case KeyEvent.VK_ASTERISK:
                keyAction.call(Input.Key.VK_ASTERISK);
                break;
            case KeyEvent.VK_QUOTEDBL:
                keyAction.call(Input.Key.VK_QUOTEDBL);
                break;
            case KeyEvent.VK_LESS:
                keyAction.call(Input.Key.VK_LESS);
                break;
            case KeyEvent.VK_GREATER:
                keyAction.call(Input.Key.VK_GREATER);
                break;
            case KeyEvent.VK_BRACELEFT:
                keyAction.call(Input.Key.VK_BRACELEFT);
                break;
            case KeyEvent.VK_BRACERIGHT:
                keyAction.call(Input.Key.VK_BRACERIGHT);
                break;
            case KeyEvent.VK_AT:
                keyAction.call(Input.Key.VK_AT);
                break;
            case KeyEvent.VK_COLON:
                keyAction.call(Input.Key.VK_COLON);
                break;
            case KeyEvent.VK_CIRCUMFLEX:
                keyAction.call(Input.Key.VK_CIRCUMFLEX);
                break;
            case KeyEvent.VK_DOLLAR:
                keyAction.call(Input.Key.VK_DOLLAR);
                break;
            case KeyEvent.VK_EURO_SIGN:
                keyAction.call(Input.Key.VK_EURO_SIGN);
                break;
            case KeyEvent.VK_EXCLAMATION_MARK:
                keyAction.call(Input.Key.VK_EXCLAMATION_MARK);
                break;
            case KeyEvent.VK_INVERTED_EXCLAMATION_MARK:
                keyAction.call(Input.Key.VK_INVERTED_EXCLAMATION_MARK);
                break;
            case KeyEvent.VK_LEFT_PARENTHESIS:
                keyAction.call(Input.Key.VK_LEFT_PARENTHESIS);
                break;
            case KeyEvent.VK_NUMBER_SIGN:
                keyAction.call(Input.Key.VK_NUMBER_SIGN);
                break;
            case KeyEvent.VK_PLUS:
                keyAction.call(Input.Key.VK_PLUS);
                break;
            case KeyEvent.VK_RIGHT_PARENTHESIS:
                keyAction.call(Input.Key.VK_RIGHT_PARENTHESIS);
                break;
            case KeyEvent.VK_UNDERSCORE:
                keyAction.call(Input.Key.VK_UNDERSCORE);
                break;
            case KeyEvent.VK_WINDOWS:
                keyAction.call(Input.Key.VK_WINDOWS);
                break;
            case KeyEvent.VK_CONTEXT_MENU:
                keyAction.call(Input.Key.VK_CONTEXT_MENU);
                break;
        }
    }

    @Override
    public void dispose() {
        viewBuffer.flush();
        canvas = null;
        frame.dispose();
        frame = null;
    }

    private void updateViewBuffer() {
        viewBuffer = graphicsConfiguration.createCompatibleVolatileImage(
                bounds.width,
                bounds.height,
                Transparency.OPAQUE
        );
        viewBuffer.setAccelerationPriority(1);
    }

    @Override
    public void init(PlatformInit platformInit) {
        this.platformInit = platformInit;
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        stretch = platformInit.isStretchViewport();


        canvas = new SwingImplPanel();

        startFrame();

        bounds.setSize(platformInit.getViewportSize().getWidth(), platformInit.getViewportSize().getHeight());

        updateViewBuffer();
    }

    @Override
    public void updateWindowIcon(Texture icon) {
        frame.setIconImage((Image) icon.getImageRef());
    }

    public void startFrame() {
        verticalSync = platformInit.isVerticalSync();
        if (frame == null) {
            frame = new JFrame(graphicsConfiguration);
            frame.addKeyListener(this);
            frame.addMouseListener(this);
            frame.addMouseWheelListener(this);
            frame.add(canvas);
        }

        if (platformInit.isUndecorated() || (platformInit.isFullscreen() && device.isFullScreenSupported())) {
            frame.setUndecorated(true);
            frame.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight());
        } else {
            frame.setUndecorated(false);
            frame.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight() + 31);
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(platformInit.getTitle());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        if (platformInit.isFullscreen() && device.isFullScreenSupported()) {
            device.setFullScreenWindow(frame);
        }

    }

    @Override
    public void frame() {
        int w = viewBuffer.getWidth(canvas), h = viewBuffer.getHeight(canvas);
        if (stretch)
            bounds.setSize(canvas.getWidth(), canvas.getHeight());
        else bounds.setSize(onSizeBoundary(viewBuffer, canvas.getSize()));
        if(w != bounds.width || h != bounds.height) {
            updateViewBuffer();
        }
        graphics = (Graphics2D) viewBuffer.getGraphics();
        defaultTransform = graphics.getTransform();
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo != null) {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            mousePosition.set((mousePoint.x - frame.getX() - frame.getWidth() / 2.0) / bounds.getWidth() * w, -(mousePoint.y - frame.getY() - frame.getHeight() / 2.0 - frame.getInsets().top / 4.0) / bounds.getHeight() * h);
            mousePosition.set(Global.clamp(mousePosition.getX(), -w / 2.0, w / 2.0), Global.clamp(mousePosition.getY(), -h / 2.0, h / 2.0));
        } else {
            mousePosition.set(0, 0);
        }
    }

    @Override
    public void draw(DrawInstruction drawInstruction) {
        int w = (int) (drawInstruction.getSize().getWidth() / platformInit.getViewportSize().getWidth() * bounds.getWidth()),
                h = (int) (drawInstruction.getSize().getHeight() / platformInit.getViewportSize().getHeight() * bounds.getHeight()),
                x = (int) ((drawInstruction.getPosition().getX() - drawInstruction.getSize().getWidth() / 2.0 - camera.getX()) / platformInit.getViewportSize().getWidth() * bounds.getWidth()),
                y = (int) ((-drawInstruction.getPosition().getY() - drawInstruction.getSize().getHeight() / 2.0 + camera.getY()) / platformInit.getViewportSize().getHeight() * bounds.getHeight());
        x += viewBuffer.getWidth(canvas) / 2;
        y += viewBuffer.getHeight(canvas) / 2;
        if (drawInstruction.getRotation() != 0)
            graphics.rotate(Math.toRadians(-drawInstruction.getRotation()), x + w / 2.0, y + h / 2.0);
        switch (drawInstruction.getType()) {
            case RECTANGLE:
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillRect(x, y, w, h);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawRect(x, y, w, h);
                }
                break;
            case OVAL:
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillOval(x, y, w, h);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawOval(x, y, w, h);
                }
                break;
            case IMAGE:
                graphics.drawImage((Image) drawInstruction.getRenderRef(), x, y, w, h, canvas);
                break;
            case TEXT:
                graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                graphics.setFont((Font) drawInstruction.getFont().getFontRef());
                graphics.drawString((String) drawInstruction.getRenderRef(), x, (int) (y + h / 1.5));
                break;
            case SIMPLE_LINE:
                graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                graphics.drawLine(x, y, w + x, h + y);
                break;
            case EQUILATERAL_TRIANGLE:
                int[] xs = new int[]{x, x + w / 2, x + w};
                int[] ys = new int[]{y + h, y, y + h};
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillPolygon(xs, ys, 3);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawPolygon(xs, ys, 3);
                }
                break;
            default:
                throw new UnsupportedDrawInstructionException(drawInstruction.getType().name());
        }
    }

    @Override
    public void resetRotation() {
        graphics.setTransform(new AffineTransform(defaultTransform));
    }

    @Override
    public boolean shouldClose() {
        if (frame == null)
            return true;
        if (!frame.isDisplayable()) {
            dispose();
            return true;
        }
        return false;
    }

    @Override
    public double getStringWidth(String text, Object fontRef) {
        return graphics.getFontMetrics((Font) fontRef).stringWidth(text);
    }

    @Override
    public double getStringHeight(String text, Object fontRef) {
        return graphics.getFontMetrics((Font) fontRef).getHeight();
    }

    @Override
    public void conclude() {
        graphics.dispose();
        if (verticalSync)
            canvas.paintImmediately(0, 0, canvas.getWidth(), canvas.getHeight());
        else
            frame.repaint();
        Toolkit.getDefaultToolkit().sync();
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

    public JComponent getCanvas() {
        return canvas;
    }

    public void setCanvas(JComponent canvas) {
        this.canvas = canvas;
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

    public AffineTransform getDefaultTransform() {
        return defaultTransform;
    }

    public void setDefaultTransform(AffineTransform defaultTransform) {
        this.defaultTransform = defaultTransform;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                pressingMouseButtons.add(Input.MouseButton.BUTTON1);
                break;
            case MouseEvent.BUTTON2:
                pressingMouseButtons.add(Input.MouseButton.BUTTON2);
                break;
            case MouseEvent.BUTTON3:
                pressingMouseButtons.add(Input.MouseButton.BUTTON3);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                pressingMouseButtons.remove(Input.MouseButton.BUTTON1);
                break;
            case MouseEvent.BUTTON2:
                pressingMouseButtons.remove(Input.MouseButton.BUTTON2);
                break;
            case MouseEvent.BUTTON3:
                pressingMouseButtons.remove(Input.MouseButton.BUTTON3);
                break;
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

    public KeyAction getAddKeyAction() {
        return addKeyAction;
    }

    public KeyAction getRemoveKeyAction() {
        return removeKeyAction;
    }

    public Dimension getBounds() {
        return bounds;
    }

    public MouseButtonAction getAddBtnAction() {
        return addBtnAction;
    }

    public MouseButtonAction getRemoveBtnAction() {
        return removeBtnAction;
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    @Override
    public TwoAnchorRepresentation getZoomScale() {
        return zoomScale;
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
                frame.dispose();
                startFrame();
            }
        }
    }

    private interface KeyAction {
        void call(Input.Key key);
    }

    private interface MouseButtonAction {
        void call(Input.MouseButton button);
    }

    class SwingImplPanel extends JPanel {

        public SwingImplPanel() {
            setIgnoreRepaint(true);
            setDoubleBuffered(true);
            setOpaque(true);
        }

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(viewBuffer, (getWidth() - bounds.width) / 2, (getHeight() - bounds.height) / 2, bounds.width, bounds.height, canvas);
            g.dispose();
        }
    }

    public Image getViewBuffer() {
        return viewBuffer;
    }

    public void setViewBuffer(Image viewBuffer) {
        this.viewBuffer = viewBuffer;
    }

    public boolean isVerticalSync() {
        return verticalSync;
    }

    public void setVerticalSync(boolean verticalSync) {
        this.verticalSync = verticalSync;
    }

    public PlatformInit getPlatformInit() {
        return platformInit;
    }

    public void setPlatformInit(PlatformInit platformInit) {
        this.platformInit = platformInit;
    }
}
