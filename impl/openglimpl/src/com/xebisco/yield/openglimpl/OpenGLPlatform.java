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

import com.xebisco.yield.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLPlatform implements PlatformGraphics, CheckKey, MouseCheck, ViewportZoomScale, ToggleFullScreen, FontLoader {

    private long windowID = -1;
    private boolean setupThread = true;
    private PlatformInit platformInit;

    private Vector2D camera = new Vector2D();
    private boolean createdCapabilities;

    private TwoAnchorRepresentation zoomScale = new TwoAnchorRepresentation(1, 1);

    @Override
    public void dispose() {
        glfwTerminate();
    }

    @Override
    public void init(PlatformInit platformInit) {
        this.platformInit = platformInit;
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        updateWindow();

        if (!createdCapabilities) {
            createdCapabilities = true;
            GL.createCapabilities();
        }
    }

    @Override
    public void updateWindowIcon(Texture icon) {
        GLFWImage.Buffer gb = GLFWImage.create(1);

        GLFWImage iconGI = GLFWImage.create().set((int) icon.getSize().getWidth(), (int) icon.getSize().getWidth(), ((Image) icon.getImageRef()).getPixels());
        gb.put(0, iconGI);
        glfwSetWindowIcon(windowID, gb);
        gb.close();
        iconGI.close();
    }

    public void updateWindow() {
        if (windowID != -1)
            glfwDestroyWindow(windowID);
        long monitor = NULL;
        if (platformInit.isFullscreen())
            monitor = glfwGetPrimaryMonitor();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        windowID = glfwCreateWindow((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight(), platformInit.getTitle(), monitor, NULL);
        if (!platformInit.isFullscreen())
            try (MemoryStack stack = stackPush()) {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);

                glfwGetWindowSize(windowID, pWidth, pHeight);

                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

                assert vidmode != null;
                glfwSetWindowPos(
                        windowID,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            }
        if (platformInit.isVerticalSync())
            glfwSwapInterval(1);

        glfwShowWindow(windowID);
        glfwMakeContextCurrent(windowID);
    }

    private final IntBuffer windowWidthBuffer = BufferUtils.createIntBuffer(1), windowHeightBuffer = BufferUtils.createIntBuffer(1);

    private final Size2D windowSize = new Size2D(0, 0);

    @Override
    public void frame() {
        if (setupThread) {
            setupThread = false;

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-platformInit.getViewportSize().getWidth() / 2, platformInit.getViewportSize().getWidth() / 2, -platformInit.getViewportSize().getHeight() / 2, platformInit.getViewportSize().getHeight() / 2, 0.0f, 1.0f);
            glMatrixMode(GL_MODELVIEW);

            glClearColor(0, 0, 0, 1);

            glDisable(GL_TEXTURE_2D);

            glEnd();
        }
        glfwGetWindowSize(windowID, windowWidthBuffer, windowHeightBuffer);

        if (platformInit.isStretchViewport()) {
            glViewport(0, 0, (int) windowSize.getWidth(), (int) windowSize.getHeight());
        } else {
            Size2D viewport = Global.onSizeBoundary(platformInit.getViewportSize(), windowSize);

            glViewport((int) (windowSize.getWidth() / 2 - viewport.getWidth() / 2), (int) (windowSize.getHeight() / 2 - viewport.getHeight() / 2), (int) viewport.getWidth(), (int) viewport.getHeight());
        }
        windowWidthBuffer.position(0);
        windowHeightBuffer.position(0);
        windowSize.set(windowWidthBuffer.get(), windowHeightBuffer.get());
        windowWidthBuffer.position(0);
        windowHeightBuffer.position(0);


        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void draw(DrawInstruction drawInstruction) {
        float w, h, x = (float) (drawInstruction.getPosition().getX() - camera.getX()), y = (float) (drawInstruction.getPosition().getY() - camera.getY());

        glLoadIdentity();

        glTranslatef(x, y, 0);

        glRotatef((float) drawInstruction.getRotation(), 0, 0, 1);

        switch (drawInstruction.getType()) {
            case RECTANGLE:
                w = (float) (drawInstruction.getSize().getWidth() / 2);
                h = (float) (drawInstruction.getSize().getHeight() / 2);

                if (drawInstruction.isFilled()) {
                    glColor4f(
                            (float) drawInstruction.getInnerColor().getRed(),
                            (float) drawInstruction.getInnerColor().getGreen(),
                            (float) drawInstruction.getInnerColor().getBlue(),
                            (float) drawInstruction.getInnerColor().getAlpha()
                    );

                    glBegin(GL_QUADS);

                    glVertex2f(-w, h);
                    glVertex2f(w, h);
                    glVertex2f(w, -h);
                    glVertex2f(-w, -h);

                    glEnd();
                }

                if (drawInstruction.getBorderColor() != null) {
                    glLineWidth((float) drawInstruction.getBorderThickness());
                    glColor4f(
                            (float) drawInstruction.getBorderColor().getRed(),
                            (float) drawInstruction.getBorderColor().getGreen(),
                            (float) drawInstruction.getBorderColor().getBlue(),
                            (float) drawInstruction.getBorderColor().getAlpha()
                    );
                    glBegin(GL_LINE_LOOP);

                    glVertex2f(-w, h);
                    glVertex2f(w, h);
                    glVertex2f(w, -h);
                    glVertex2f(-w, -h);

                    glEnd();
                }

                break;
            case EQUILATERAL_TRIANGLE:
                w = (float) (drawInstruction.getSize().getWidth() / 2);
                h = (float) (drawInstruction.getSize().getHeight() / 2);
                if (drawInstruction.isFilled()) {
                    glColor4f(
                            (float) drawInstruction.getInnerColor().getRed(),
                            (float) drawInstruction.getInnerColor().getGreen(),
                            (float) drawInstruction.getInnerColor().getBlue(),
                            (float) drawInstruction.getInnerColor().getAlpha()
                    );

                    glBegin(GL_POLYGON);

                    glVertex2f(-w, -h);
                    glVertex2f(0, h);
                    glVertex2f(w, -h);

                    glEnd();
                }

                if (drawInstruction.getBorderColor() != null) {
                    glLineWidth((float) drawInstruction.getBorderThickness());
                    glColor4f(
                            (float) drawInstruction.getBorderColor().getRed(),
                            (float) drawInstruction.getBorderColor().getGreen(),
                            (float) drawInstruction.getBorderColor().getBlue(),
                            (float) drawInstruction.getBorderColor().getAlpha()
                    );
                    glBegin(GL_LINE_LOOP);

                    glVertex2f(-w, -h);
                    glVertex2f(0, h);
                    glVertex2f(w, -h);

                    glEnd();
                }

                break;
            case OVAL:
                w = (float) drawInstruction.getSize().getWidth();
                h = (float) drawInstruction.getSize().getHeight();
                double ap = 6.28318 / 60.;
                double theta = 0.;
                if (drawInstruction.isFilled()) {
                    glColor4f(
                            (float) drawInstruction.getInnerColor().getRed(),
                            (float) drawInstruction.getInnerColor().getGreen(),
                            (float) drawInstruction.getInnerColor().getBlue(),
                            (float) drawInstruction.getInnerColor().getAlpha()
                    );

                    glBegin(GL_POLYGON);

                    for (int i = 0; i < 60; i++) {
                        glVertex2f((float) (w / 2f * Math.cos(theta)), (float) (h / 2f * Math.sin(theta)));
                        theta += ap;
                    }

                    glEnd();
                }

                if (drawInstruction.getBorderColor() != null) {
                    glLineWidth((float) drawInstruction.getBorderThickness());
                    glColor4f(
                            (float) drawInstruction.getBorderColor().getRed(),
                            (float) drawInstruction.getBorderColor().getGreen(),
                            (float) drawInstruction.getBorderColor().getBlue(),
                            (float) drawInstruction.getBorderColor().getAlpha()
                    );
                    glBegin(GL_LINE_LOOP);

                    for (int i = 0; i < 60; i++) {
                        glVertex2f((float) (w / 2f * Math.cos(theta)), (float) (h / 2f * Math.sin(theta)));
                        theta += ap;
                    }

                    glEnd();
                }

                break;
            case IMAGE:
                w = (float) (drawInstruction.getSize().getWidth() / 2);
                h = (float) (drawInstruction.getSize().getHeight() / 2);

                glColor4f((float) 1, (float) 1, (float) 1, (float) drawInstruction.getInnerColor().getAlpha());

                glEnable(GL_TEXTURE_2D);
                glBindTexture(GL_TEXTURE_2D, ((Image) drawInstruction.getRenderRef()).getId());

                glBegin(GL_QUADS);
                glTexCoord2f(0, 0);
                glVertex2f(-w, h);
                glTexCoord2f(1, 0);
                glVertex2f(w, h);
                glTexCoord2f(1, 1);
                glVertex2f(w, -h);
                glTexCoord2f(0, 1);
                glVertex2f(-w, -h);

                glTexCoord2f(0, 0);

                glEnd();
                glDisable(GL_TEXTURE_2D);
                break;
        }
    }

    @Override
    public void conclude() {
        glfwSwapBuffers(windowID);
    }

    @Override
    public void resetRotation() {

    }

    @Override
    public boolean shouldClose() {
        if (windowID == -1) return false;
        return glfwWindowShouldClose(windowID);
    }


    @Override
    public Vector2D getCamera() {
        return camera;
    }

    @Override
    public void setCamera(Vector2D camera) {
        this.camera = camera;
    }

    /*@Override
    public double getStringWidth(String text, Object fontRef) {        //TODO
        float length = 0f;
        for (int i = 0; i < text.length(); i++) {
            IntBuffer advancewidth = BufferUtils.createIntBuffer(1);
            IntBuffer leftsidebearing = BufferUtils.createIntBuffer(1);
            STBTruetype.stbtt_GetCodepointHMetrics((STBTTFontinfo) fontRef, text.charAt(i), advancewidth, leftsidebearing);
            length += advancewidth.get(0);
        }
        return length;
    }*/

    @Override
    public Texture printScreenTexture() {
        return null;
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
    public boolean checkKey(Input.Key key) {
        if (windowID == -1) return false;
        return glfwGetKey(windowID, key(key)) == GLFW_PRESS;
    }

    @Override
    public boolean checkMouseButton(Input.MouseButton button) {
        if (windowID == -1) return false;
        return glfwGetMouseButton(windowID, mouseButton(button)) == GLFW_PRESS;
    }

    public int mouseButton(Input.MouseButton mouseButton) {
        switch (mouseButton) {
            case BUTTON1:
                return GLFW_MOUSE_BUTTON_1;
            case BUTTON2:
                return GLFW_MOUSE_BUTTON_2;
            case BUTTON3:
                return GLFW_MOUSE_BUTTON_3;
        }
        return GLFW_KEY_UNKNOWN;
    }

    public int key(Input.Key key) {
        switch (key) {
            case VK_ENTER:
                return GLFW_KEY_ENTER;
            case VK_BACK_SPACE:
                return GLFW_KEY_BACKSPACE;
            case VK_TAB:
                return GLFW_KEY_TAB;
            case VK_SHIFT:
                return GLFW_KEY_LEFT_SHIFT;
            case VK_CONTROL:
                return GLFW_KEY_LEFT_CONTROL;
            case VK_ALT:
                return GLFW_KEY_LEFT_ALT;
            case VK_PAUSE:
                return GLFW_KEY_PAUSE;
            case VK_CAPS_LOCK:
                return GLFW_KEY_CAPS_LOCK;
            case VK_ESCAPE:
                return GLFW_KEY_ESCAPE;
            case VK_SPACE:
                return GLFW_KEY_SPACE;
            case VK_PAGE_UP:
                return GLFW_KEY_PAGE_UP;
            case VK_PAGE_DOWN:
                return GLFW_KEY_PAGE_DOWN;
            case VK_END:
                return GLFW_KEY_END;
            case VK_HOME:
                return GLFW_KEY_HOME;
            case VK_LEFT:
                return GLFW_KEY_LEFT;
            case VK_UP:
                return GLFW_KEY_UP;
            case VK_RIGHT:
                return GLFW_KEY_RIGHT;
            case VK_DOWN:
                return GLFW_KEY_DOWN;
            case VK_COMMA:
                return GLFW_KEY_COMMA;
            case VK_MINUS:
                return GLFW_KEY_MINUS;
            case VK_PERIOD:
                return GLFW_KEY_PERIOD;
            case VK_SLASH:
                return GLFW_KEY_SLASH;
            case VK_0:
                return GLFW_KEY_0;
            case VK_1:
                return GLFW_KEY_1;
            case VK_2:
                return GLFW_KEY_2;
            case VK_3:
                return GLFW_KEY_3;
            case VK_4:
                return GLFW_KEY_4;
            case VK_5:
                return GLFW_KEY_5;
            case VK_6:
                return GLFW_KEY_6;
            case VK_7:
                return GLFW_KEY_7;
            case VK_8:
                return GLFW_KEY_8;
            case VK_9:
                return GLFW_KEY_9;
            case VK_SEMICOLON:
                return GLFW_KEY_SEMICOLON;
            case VK_EQUALS:
                return GLFW_KEY_EQUAL;
            case VK_A:
                return GLFW_KEY_A;
            case VK_B:
                return GLFW_KEY_B;
            case VK_C:
                return GLFW_KEY_C;
            case VK_D:
                return GLFW_KEY_D;
            case VK_E:
                return GLFW_KEY_E;
            case VK_F:
                return GLFW_KEY_F;
            case VK_G:
                return GLFW_KEY_G;
            case VK_H:
                return GLFW_KEY_H;
            case VK_I:
                return GLFW_KEY_I;
            case VK_J:
                return GLFW_KEY_J;
            case VK_K:
                return GLFW_KEY_K;
            case VK_L:
                return GLFW_KEY_L;
            case VK_M:
                return GLFW_KEY_M;
            case VK_N:
                return GLFW_KEY_N;
            case VK_O:
                return GLFW_KEY_O;
            case VK_P:
                return GLFW_KEY_P;
            case VK_Q:
                return GLFW_KEY_Q;
            case VK_R:
                return GLFW_KEY_R;
            case VK_S:
                return GLFW_KEY_S;
            case VK_T:
                return GLFW_KEY_T;
            case VK_U:
                return GLFW_KEY_U;
            case VK_V:
                return GLFW_KEY_V;
            case VK_W:
                return GLFW_KEY_W;
            case VK_X:
                return GLFW_KEY_X;
            case VK_Y:
                return GLFW_KEY_Y;
            case VK_Z:
                return GLFW_KEY_Z;
            case VK_OPEN_BRACKET:
                return GLFW_KEY_LEFT_BRACKET;
            case VK_BACK_SLASH:
                return GLFW_KEY_BACKSLASH;
            case VK_CLOSE_BRACKET:
                return GLFW_KEY_RIGHT_BRACKET;
            case VK_NUMPAD0:
                return GLFW_KEY_KP_0;
            case VK_NUMPAD1:
                return GLFW_KEY_KP_1;
            case VK_NUMPAD2:
                return GLFW_KEY_KP_2;
            case VK_NUMPAD3:
                return GLFW_KEY_KP_3;
            case VK_NUMPAD4:
                return GLFW_KEY_KP_4;
            case VK_NUMPAD5:
                return GLFW_KEY_KP_5;
            case VK_NUMPAD6:
                return GLFW_KEY_KP_6;
            case VK_NUMPAD7:
                return GLFW_KEY_KP_7;
            case VK_NUMPAD8:
                return GLFW_KEY_KP_8;
            case VK_NUMPAD9:
                return GLFW_KEY_KP_9;
            case VK_MULTIPLY:
                return GLFW_KEY_KP_MULTIPLY;
            case VK_ADD:
            case VK_PLUS:
                return GLFW_KEY_KP_ADD;
            case VK_SUBTRACT:
                return GLFW_KEY_KP_SUBTRACT;
            case VK_DECIMAL:
                return GLFW_KEY_KP_DECIMAL;
            case VK_DIVIDE:
                return GLFW_KEY_KP_DIVIDE;
            case VK_DELETE:
                return GLFW_KEY_DELETE;
            case VK_NUM_LOCK:
                return GLFW_KEY_NUM_LOCK;
            case VK_SCROLL_LOCK:
                return GLFW_KEY_SCROLL_LOCK;
            case VK_F1:
                return GLFW_KEY_F1;
            case VK_F2:
                return GLFW_KEY_F2;
            case VK_F3:
                return GLFW_KEY_F3;
            case VK_F4:
                return GLFW_KEY_F4;
            case VK_F5:
                return GLFW_KEY_F5;
            case VK_F6:
                return GLFW_KEY_F6;
            case VK_F7:
                return GLFW_KEY_F7;
            case VK_F8:
                return GLFW_KEY_F8;
            case VK_F9:
                return GLFW_KEY_F9;
            case VK_F10:
                return GLFW_KEY_F10;
            case VK_F11:
                return GLFW_KEY_F11;
            case VK_F12:
                return GLFW_KEY_F12;
            case VK_PRINTSCREEN:
                return GLFW_KEY_PRINT_SCREEN;
            case VK_INSERT:
                return GLFW_KEY_INSERT;
            case VK_WINDOWS:
                return GLFW_KEY_LEFT_SUPER;
            case VK_CONTEXT_MENU:
                return GLFW_KEY_MENU;
        }
        return GLFW_KEY_UNKNOWN;
    }

    @Override
    public void setZoomScale(TwoAnchorRepresentation zoomScale) {
        this.zoomScale = zoomScale;
    }

    @Override
    public TwoAnchorRepresentation getZoomScale() {
        return zoomScale;
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        dispose();
        platformInit.setFullscreen(fullScreen);
        init(platformInit);
    }

    @Override
    public Object loadFont(com.xebisco.yield.Font font) {
        return null;
    }

    @Override
    public void unloadFont(com.xebisco.yield.Font font) {

    }

    @Override
    public double getStringWidth(String text, Object fontRef) {
        return 0;
    }

    @Override
    public double getStringHeight(String text, Object fontRef) {
        return ((Font) fontRef).getSize();
    }
}
