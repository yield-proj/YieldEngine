/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield.swingimpl;

import com.xebisco.yield.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class SwingPlatformGraphics implements PlatformGraphics, FontLoader, TextureLoader, InputManager, KeyListener, MouseListener, MouseWheelListener {
    private final HashSet<Input.Key> pressingKeys = new HashSet<>();
    private final HashSet<Input.MouseButton> pressingMouseButtons = new HashSet<>();
    private final Point2D mousePosition = new Point2D();
    private final KeyAction addKeyAction = pressingKeys::add;
    private final KeyAction removeKeyAction = pressingKeys::remove;
    private final Dimension bounds = new Dimension();
    private final MouseButtonAction addBtnAction = pressingMouseButtons::add;
    private final MouseButtonAction removeBtnAction = pressingMouseButtons::remove;
    private VolatileImage renderImage;
    private GraphicsDevice device;
    private GraphicsConfiguration graphicsConfiguration;
    private JPanel panel;
    private JFrame frame;
    private Graphics2D graphics;
    private AffineTransform defaultTransform = new AffineTransform();
    private boolean stretch;

    public static Dimension onSizeBoundary(Image image, Dimension boundary) {
        int original_width = image.getWidth(null);
        int original_height = image.getHeight(null);
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

    public static Color awtColor(com.xebisco.yield.Color yieldColor) {
        return new Color(yieldColor.getRGBA(), true);
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
        Image i;
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
        return i;
    }

    @Override
    public void unloadTexture(Texture texture) {
        texture.setImageRef(null);
    }

    @Override
    public int getImageWidth(Object imageRef) {
        return 0;
    }

    @Override
    public int getImageHeight(Object imageRef) {
        return 0;
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
        renderImage.flush();
        panel = null;
        frame.dispose();
        frame = null;
    }

    @Override
    public void init(PlatformInit platformInit) {
        System.setProperty("sun.java2d.opengl", "True");
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        stretch = platformInit.isStretchViewport();
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        graphicsConfiguration = device.getDefaultConfiguration();
        panel = new SwingImplPanel();
        frame = new JFrame(graphicsConfiguration);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseWheelListener(this);
        frame.add(panel);
        renderImage = graphicsConfiguration.createCompatibleVolatileImage(
                (int) platformInit.getResolution().getWidth(),
                (int) platformInit.getResolution().getHeight(),
                Transparency.OPAQUE
        );
        if (platformInit.isUndecorated()) {
            frame.setUndecorated(true);
            frame.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight());
        } else {
            frame.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight() + 31);
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(platformInit.getTitle());
        frame.setLocationRelativeTo(null);
        frame.setIconImage((Image) platformInit.getWindowIcon().getImageRef());
        frame.setVisible(true);
        if (platformInit.isFullscreen() && device.isFullScreenSupported()) {
            device.setFullScreenWindow(frame);
        }
    }

    @Override
    public void frame() {
        graphics = renderImage.createGraphics();
        defaultTransform = graphics.getTransform();
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo != null) {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            mousePosition.set((mousePoint.x - frame.getX() - frame.getWidth() / 2.0) / bounds.getWidth() * renderImage.getWidth(), -(mousePoint.y - frame.getY() - frame.getHeight() / 2.0 - frame.getInsets().top / 4.0) / bounds.getHeight() * renderImage.getHeight());
            mousePosition.set(Global.clamp(mousePosition.getX(), -renderImage.getWidth() / 2.0, renderImage.getWidth() / 2.0), Global.clamp(mousePosition.getY(), -renderImage.getHeight() / 2.0, renderImage.getHeight() / 2.0));
        } else {
            mousePosition.set(0, 0);
        }
    }

    @Override
    public void draw(DrawInstruction drawInstruction) {
        int x = (int) (drawInstruction.getPosition().getX() - drawInstruction.getSize().getWidth() / 2.0) + renderImage.getWidth() / 2,
                y = (int) (-drawInstruction.getPosition().getY() - drawInstruction.getSize().getHeight() / 2.0) + renderImage.getHeight() / 2,
                w = (int) drawInstruction.getSize().getWidth(),
                h = (int) drawInstruction.getSize().getHeight();
        if (drawInstruction.getRotation() != 0)
            graphics.rotate(Math.toRadians(-drawInstruction.getRotation()), x + w / 2.0, y + h / 2.0);
        switch (drawInstruction.getType()) {
            case RECTANGLE -> {
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillRect(x, y, w, h);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawRect(x, y, w, h);
                }
            }
            case OVAL -> {
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillOval(x, y, w, h);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawOval(x, y, w, h);
                }
            }
            case IMAGE -> graphics.drawImage((Image) drawInstruction.getRenderRef(), x, y, w, h, null);
            case TEXT -> {
                graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                graphics.setFont((Font) drawInstruction.getFont().getFontRef());
                graphics.drawString((String) drawInstruction.getRenderRef(), x, y + h / 2);
            }
            case SIMPLE_LINE -> {
                graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                graphics.drawLine(x, y, w, h);
            }
            default -> throw new UnsupportedDrawInstructionException(drawInstruction.getType().name());
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
        panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
    }

    public VolatileImage getRenderImage() {
        return renderImage;
    }

    public void setRenderImage(VolatileImage renderImage) {
        this.renderImage = renderImage;
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

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
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

    private interface KeyAction {
        void call(Input.Key key);
    }

    private interface MouseButtonAction {
        void call(Input.MouseButton button);
    }

    class SwingImplPanel extends JPanel {
        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (stretch)
                bounds.setSize(getWidth(), getHeight());
            else bounds.setSize(onSizeBoundary(renderImage, getSize()));
            g.drawImage(renderImage, (getWidth() - bounds.width) / 2, (getHeight() - bounds.height) / 2, bounds.width, bounds.height, this);
            g.dispose();
        }
    }
}
