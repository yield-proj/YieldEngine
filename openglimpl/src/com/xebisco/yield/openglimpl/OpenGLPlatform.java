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

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.xebisco.yield.*;
import java.util.Collection;
import java.util.HashSet;

public class OpenGLPlatform implements PlatformGraphics, FontLoader, TextureManager, InputManager, ViewportZoomScale, ToggleFullScreen, GLEventListener, KeyListener, MouseListener {

    private GLWindow window;
    private final Vector2D mousePosition = new Vector2D();
    private Vector2D camera = new Vector2D();
    private PlatformInit platformInit;

    private final HashSet<Input.Key> pressingKeys = new HashSet<>();
    private final HashSet<Input.MouseButton> pressingMouseButtons = new HashSet<>();
    private final KeyAction addKeyAction = pressingKeys::add, removeKeyAction = pressingKeys::remove;

    private interface KeyAction {
        void call(Input.Key key);
    }

    private interface MouseButtonAction {
        void call(Input.MouseButton button);
    }

    private TwoAnchorRepresentation scale = new TwoAnchorRepresentation(1, 1);


    @Override
    public void init(PlatformInit platformInit) {

        this.platformInit = platformInit;

        window = GLWindow.create(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
        window.setSize((int) platformInit.getViewportSize().getWidth(), (int) platformInit.getViewportSize().getHeight());
        window.setUndecorated(platformInit.isUndecorated());
        window.setFullscreen(platformInit.isFullscreen());
        window.setTitle(platformInit.getTitle());
        window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

        window.addKeyListener(this);
        window.addMouseListener(this);

        window.setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        if (platformInit.isVerticalSync())
            gl.setSwapInterval(1);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glOrtho(
                -platformInit.getViewportSize().getWidth() / 2.,
                platformInit.getViewportSize().getWidth() / 2.,
                -platformInit.getViewportSize().getHeight() / 2.,
                platformInit.getViewportSize().getHeight() / 2.,
                -1,
                1
        );
    }

    @Override
    public void dispose() {

    }

    @Override
    public Object loadFont(Font font) {
        return null;
    }

    @Override
    public void unloadFont(Font font) {

    }

    @Override
    public double getStringWidth(String text, Object fontRef) {
        return 0;
    }

    @Override
    public double getStringHeight(String text, Object fontRef) {
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
        return 0;
    }

    @Override
    public double getMouseY() {
        return 0;
    }

    @Override
    public void updateWindowIcon(Texture icon) {

    }

    @Override
    public void frame() {

    }

    @Override
    public void draw(DrawInstruction drawInstruction) {

    }

    @Override
    public boolean shouldClose() {
        return !window.isVisible();
    }

    @Override
    public void conclude() {
        window.display();
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
    public Object loadTexture(Texture texture) {
        return null;
    }

    @Override
    public void unloadTexture(Texture texture) {

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
    public void setFullScreen(boolean fullScreen) {

    }

    @Override
    public void setZoomScale(TwoAnchorRepresentation scale) {
        this.scale = scale;
    }

    @Override
    public TwoAnchorRepresentation getZoomScale() {
        return scale;
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
                (double) e.getX() / window.getWidth() * platformInit.getViewportSize().getWidth(),
                (double) e.getY() / window.getHeight() * platformInit.getViewportSize().getHeight()
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
}
