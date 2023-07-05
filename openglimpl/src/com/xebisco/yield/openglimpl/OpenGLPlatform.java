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

package com.xebisco.yield.openglimpl;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.xebisco.yield.Font;
import com.xebisco.yield.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class OpenGLPlatform implements GraphicsManager, FontManager, TextureManager, SpritesheetTextureManager, InputManager, ViewportZoomScale, ToggleFullScreen, GLEventListener, KeyListener, MouseListener {

    private GLWindow window;
    private final Vector2D mousePosition = new Vector2D();
    private Vector2D camera = new Vector2D();
    private PlatformInit platformInit;

    private final FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, true);

    private final Set<OpenGLImage> toLoadImages = new HashSet<>(), toDestroyImages = new HashSet<>();

    private final HashSet<Input.Key> pressingKeys = new HashSet<>();
    private final HashSet<Input.MouseButton> pressingMouseButtons = new HashSet<>();
    private final KeyAction addKeyAction = pressingKeys::add, removeKeyAction = pressingKeys::remove;
    private GLProfile profile;

    private List<DrawInstruction> drawInstructions;

    @Override
    public Object loadSpritesheetTexture(SpritesheetTexture spritesheetTexture) {
        return loadAWTBufferedImage(new BufferedInputStream(spritesheetTexture.getInputStream()));
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
        return i;
    }

    @Override
    public void unloadSpritesheetTexture(SpritesheetTexture spritesheetTexture) {
        ((Image) spritesheetTexture.getSpritesheetImageRef()).flush();
        spritesheetTexture.setSpritesheetImageRef(null);
    }

    @Override
    public Texture getTextureFromRegion(int x, int y, int width, int height, SpritesheetTexture spritesheetTexture) {
        return new Texture(AWTTextureIO.newTexture(profile, ((BufferedImage) spritesheetTexture.getSpritesheetTextureManager()).getSubimage(x, y, width, height), false), null, this);
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

    private TwoAnchorRepresentation scale = new TwoAnchorRepresentation(1, 1);


    @Override
    public void init(PlatformInit platformInit) {
        this.platformInit = platformInit;

        System.setProperty("newt.window.icons", platformInit.getWindowIconPath() + " " + platformInit.getWindowIconPath());

        window = GLWindow.create(new GLCapabilities(profile = GLProfile.get(GLProfile.GL2)));
        window.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight());
        window.setUndecorated(platformInit.isUndecorated());
        window.setTitle(platformInit.getTitle());

        window.addKeyListener(this);
        window.addMouseListener(this);
        window.addGLEventListener(this);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setPosition(screen.width / 2 - window.getWidth() / 2, screen.height / 2 - window.getHeight() / 2);

        window.setVisible(true);

        setFullScreen(platformInit.isFullscreen());
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glEnable(GL2.GL_BLEND);
        gl.setSwapInterval(0);
        gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        try {
            toLoadImages.removeIf(image -> {
                image.setTexture(TextureIO.newTexture(image.getTextureData()));
                return true;
            });
        } catch (ConcurrentModificationException ignore) {
        }

        toDestroyImages.removeIf(image -> {
            image.getTexture().destroy(gl);
            return true;
        });

        if (drawInstructions != null)
            try {
                for (DrawInstruction di : drawInstructions) {
                    gl.glLoadIdentity();

                    gl.glTranslated(-camera.getX(), -camera.getY(), 0);
                    gl.glScaled(scale.getX(), scale.getY(), 1);
                    draw(di, gl);
                }
            } catch (ConcurrentModificationException ignore) {

            }
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(
                -platformInit.getViewportSize().getWidth() / 2.,
                platformInit.getViewportSize().getWidth() / 2.,
                -platformInit.getViewportSize().getHeight() / 2.,
                platformInit.getViewportSize().getHeight() / 2.,
                -1,
                1
        );
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        if (platformInit.isStretchViewport()) {
            gl.glViewport(0, 0, window.getWidth(), window.getHeight());
        } else {
            Size2D viewport = Global.onSizeBoundary(platformInit.getViewportSize(), new Size2D(window.getWidth(), window.getHeight()));

            gl.glViewport((int) (window.getWidth() / 2 - viewport.getWidth() / 2), (int) (window.getHeight() / 2 - viewport.getHeight() / 2), (int) viewport.getWidth(), (int) viewport.getHeight());
        }
    }

    public void draw(DrawInstruction di, GL2 gl) {
        gl.glTranslatef((float) di.getX(), (float) di.getY(), 0);
        if (di.isRotateBeforeScale()) {
            gl.glRotatef((float) di.getRotation(), 0, 0, 1);
            gl.glScalef((float) di.getScaleX(), (float) di.getScaleY(), 0);
        } else {
            gl.glScalef((float) di.getScaleX(), (float) di.getScaleY(), 0);
            gl.glRotatef((float) di.getRotation(), 0, 0, 1);
        }

        if (di.getVerticesX() == null && di.getVerticesY() == null) {
            if (di.getColor() != null) {
                gl.glClearColor((float) di.getColor().getRed(), (float) di.getColor().getGreen(), (float) di.getColor().getBlue(), (float) di.getColor().getAlpha());
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            }
        } else {
            gl.glColor4d(di.getColor().getRed(), di.getColor().getGreen(), di.getColor().getBlue(), di.getColor().getAlpha());

            if (di.getImageRef() != null && ((OpenGLImage) di.getImageRef()).getTexture() != null) {
                gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
                com.jogamp.opengl.util.texture.Texture t = ((OpenGLImage) di.getImageRef()).getTexture();
                t.enable(gl);
                t.bind(gl);
                gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
                gl.glBegin(GL2.GL_QUADS);
                if (di.getVerticesX().length != 4 || di.getVerticesY().length != 4)
                    throw new IllegalVerticesCountException("OpenGL image rendering supports only rectangles");
                gl.glTexCoord2i(0, 1);
                gl.glVertex2i(di.getVerticesX()[0], di.getVerticesY()[0]);
                gl.glTexCoord2i(1, 1);
                gl.glVertex2i(di.getVerticesX()[1], di.getVerticesY()[1]);
                gl.glTexCoord2i(1, 0);
                gl.glVertex2i(di.getVerticesX()[2], di.getVerticesY()[2]);
                gl.glTexCoord2i(0, 0);
                gl.glVertex2i(di.getVerticesX()[3], di.getVerticesY()[3]);
                gl.glEnd();
                t.disable(gl);
            } else if (di.getFontRef() != null) {
                TextRenderer renderer = (TextRenderer) di.getFontRef();
                gl.glEnable(GL2.GL_TEXTURE_2D);
                renderer.begin3DRendering();
                Rectangle2D bounds = renderer.getBounds(di.getText());
                renderer.draw(di.getText(), (int) (-bounds.getWidth() / 2), (int) (-bounds.getHeight() / 2));
                renderer.end3DRendering();
                gl.glDisable(GL2.GL_TEXTURE_2D);
            } else {
                if (di.getStroke() == 0)
                    gl.glBegin(GL2.GL_POLYGON);
                else {
                    gl.glLineWidth((float) di.getStroke());
                    gl.glBegin(GL2.GL_LINE_LOOP);
                }
                for (int i1 = 0; i1 < di.getVerticesX().length; i1++) {
                    gl.glVertex2i(di.getVerticesX()[i1], di.getVerticesY()[i1]);
                }
                gl.glEnd();
            }
        }

        for (DrawInstruction child : di.getChildrenInstructions()) {
            draw(child, gl);
        }
    }

    @Override
    public void dispose() {
        window.destroy();
    }

    @Override
    public Object loadFont(Font font) {
        try {
            return new TextRenderer(java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, font.getInputStream()).deriveFont((float) font.getSize()));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unloadFont(Font font) {
        ((TextRenderer) font.getFontRef()).dispose();
    }

    @Override
    public double getStringWidth(String text, Object fontRef) {
        return ((TextRenderer) fontRef).getFont().getStringBounds(text, frc).getWidth();
    }

    @Override
    public double getStringHeight(String text, Object fontRef) {
        return ((TextRenderer) fontRef).getFont().getStringBounds(text, frc).getHeight();
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
    public void updateWindowIcon(Texture icon) {

    }

    @Override
    public void frame() {

    }

    @Override
    public void draw(List<DrawInstruction> drawInstructions) {
        setDrawInstructions(drawInstructions);

        window.windowRepaint(0, 0, window.getWidth(), window.getHeight());
    }

    @Override
    public boolean shouldClose() {
        return !window.isVisible();
    }

    @Override
    public void setCamera(Vector2D camera) {
        this.camera = camera;
    }

    @Override
    public Object loadTexture(Texture texture) {
        try {
            OpenGLImage image = new OpenGLImage(AWTTextureIO.newTextureData(profile, ImageIO.read(texture.getInputStream()), false));
            toLoadImages.add(image);
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unloadTexture(Texture texture) {
        toDestroyImages.add((OpenGLImage) texture.getImageRef());
    }

    @Override
    public int getImageWidth(Object imageRef) {
        return ((OpenGLImage) imageRef).getTextureData().getWidth();
    }

    @Override
    public int getImageHeight(Object imageRef) {
        return ((OpenGLImage) imageRef).getTextureData().getHeight();
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        window.setFullscreen(fullScreen);
    }

    @Override
    public void setZoomScale(TwoAnchorRepresentation scale) {
        this.scale = scale;
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
            case KeyEvent.VK_LEFT_BRACE:
                keyAction.call(Input.Key.VK_BRACELEFT);
                break;
            case KeyEvent.VK_RIGHT_BRACE:
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
    public void keyPressed(KeyEvent e) {
        key(addKeyAction, e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isAutoRepeat())
            key(removeKeyAction, e);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

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
        if (!e.isAutoRepeat())
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
    public void mouseMoved(MouseEvent e) {
        mousePosition.set(
                (double) e.getX() / window.getWidth() * platformInit.getViewportSize().getWidth() - platformInit.getViewportSize().getWidth() / 2.,
                (double) e.getY() / window.getHeight() * platformInit.getViewportSize().getHeight() - platformInit.getViewportSize().getHeight() / 2.
        );
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        if (e.getRotation()[1] < 0) {
            pressingMouseButtons.add(Input.MouseButton.SCROLL_UP);
        } else {
            pressingMouseButtons.add(Input.MouseButton.SCROLL_DOWN);
        }
    }

    public GLWindow getWindow() {
        return window;
    }

    public void setWindow(GLWindow window) {
        this.window = window;
    }

    public PlatformInit getPlatformInit() {
        return platformInit;
    }

    public void setPlatformInit(PlatformInit platformInit) {
        this.platformInit = platformInit;
    }

    public FontRenderContext getFrc() {
        return frc;
    }

    public Set<OpenGLImage> getToLoadImages() {
        return toLoadImages;
    }

    public Set<OpenGLImage> getToDestroyImages() {
        return toDestroyImages;
    }

    public KeyAction getAddKeyAction() {
        return addKeyAction;
    }

    public KeyAction getRemoveKeyAction() {
        return removeKeyAction;
    }

    public GLProfile getProfile() {
        return profile;
    }

    public void setProfile(GLProfile profile) {
        this.profile = profile;
    }

    public List<DrawInstruction> getDrawInstructions() {
        return drawInstructions;
    }

    public void setDrawInstructions(List<DrawInstruction> drawInstructions) {
        this.drawInstructions = drawInstructions;
    }
}
