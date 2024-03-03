/*
 * Copyright [2022-2024] [Xebisco]
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

import com.xebisco.yield.Color;
import com.xebisco.yield.Input;
import com.xebisco.yield.Transform2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.font.FontCharacter;
import com.xebisco.yield.manager.GraphicsManager;
import com.xebisco.yield.manager.PCInputManager;
import com.xebisco.yield.openglimpl.shader.BlendFunc;
import com.xebisco.yield.openglimpl.shader.ConnectToShader;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Paint;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class AbstractOpenGLGraphicsManager implements GraphicsManager, PCInputManager {

    private final Vector2D mouse = new Vector2D();

    private final Map<String, OpenGLShaderProgram> loadedShaders = new HashMap<>();

    private Transform2D camera;
    private Vector2D viewportSize;

    private OpenGLShaderProgram default2DShader;
    private OpenGLShaderProgram defaultText2DShader;

    private Mesh2D squareMesh;

    private Matrix4f viewMatrix;


    private final Collection<Input.Key> pressingKeys = new ArrayList<>();
    private final Collection<Input.MouseButton> pressingMouseButtons = new ArrayList<>();

    private long windowHandler;

    protected void initAll() {
        GL.createCapabilities();

        default2DShader = new OpenGLShaderProgram(AbstractOpenGLGraphicsManager.class.getResourceAsStream("default2d.vert"), AbstractOpenGLGraphicsManager.class.getResourceAsStream("default2d.frag"));
        defaultText2DShader = new OpenGLShaderProgram(AbstractOpenGLGraphicsManager.class.getResourceAsStream("default2d.vert"), AbstractOpenGLGraphicsManager.class.getResourceAsStream("textdefault2d.frag"));
        squareMesh = new Mesh2D(new float[]{-1, 1, 1, 1, 1, -1, -1, -1}, new float[]{0, 0, 1, 0, 1, 1, 0, 1}, new int[]{0, 1, 2, 2, 3, 0});
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandler);
    }

    @Override
    public void setCamera(Transform2D camera) {
        this.camera = camera;
    }

    @Override
    public void start(Color clearColor) {
        glfwPollEvents();
        GL11.glClearColor((float) clearColor.red(), (float) clearColor.green(), (float) clearColor.blue(), (float) clearColor.alpha());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        viewMatrix = GLUtils.orthoViewMatrix(camera, viewportSize);
    }

    @Override
    public void draw(Form form, Paint paint, Object caller) {
        if (caller != null && caller.getClass().isAnnotationPresent(ConnectToShader.class)) {
            draw((ConnectToShader) caller);
            return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        switch (form) {
            case SQUARE -> {
                OpenGLShaderProgram shader = default2DShader;
                boolean isChar = paint.text() != null;
                if (isChar) shader = defaultText2DShader;


                shader.bind();

                try {
                    shader.setUniform("transformationMatrix", new Matrix4f().identity().translate(new Vector3f((float) paint.transformation().position().x(), (float) paint.transformation().position().y(), 0)).rotateZ((float) Math.toRadians(paint.transformation().zRotation())).scaleXY((float) (paint.transformation().scale().x() * paint.rectSize().width()), (float) (paint.transformation().scale().y() * paint.rectSize().height())));
                } catch (IllegalArgumentException ignore) {
                }
                try {
                    shader.setUniform("viewMatrix", viewMatrix);
                } catch (IllegalArgumentException ignore) {
                }
                try {
                    shader.setUniform("color", new Vector4f((float) paint.color().red(), (float) paint.color().green(), (float) paint.color().blue(), (float) paint.color().alpha()));
                } catch (IllegalArgumentException ignore) {
                }

                if (paint.hasImage()) {
                    try {
                        shader.setUniform("texture_sampler", 0);
                    } catch (IllegalArgumentException ignore) {
                    }
                    if (!isChar) {
                        try {
                            shader.setUniform("ignoreTexture", 0);
                        } catch (IllegalArgumentException ignore) {
                        }
                    }
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, (int) paint.drawObj());
                } else {
                    try {
                        shader.setUniform("ignoreTexture", 1);
                    } catch (IllegalArgumentException ignore) {
                    }
                }

                glBindVertexArray(squareMesh.vao);

                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);

                glDrawElements(GL_TRIANGLES, squareMesh.vertexCount, GL_UNSIGNED_INT, 0);

                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);

                glBindVertexArray(0);
                glBindTexture(GL_TEXTURE_2D, 0);

                shader.unbind();
            }
            case TEXT -> {
                if (!paint.text().isEmpty()) {
                    double width = 0;
                    for (char c : paint.text().toCharArray()) {
                        FontCharacter character = paint.font().characterMap().get(c);
                        if (character == null) return;
                        width += character.advance() * 2;
                    }
                    double x = -width / 2;
                    double y = -paint.font().size() / 2;
                    for (char c : paint.text().toCharArray()) {
                        Paint p = new Paint();
                        p.setText("");
                        p.setHasImage(true);
                        p.setColor(paint.color());
                        Transform2D t = new Transform2D(paint.transformation());
                        FontCharacter character = paint.font().characterMap().get(c);
                        x += character.advance();
                        t.translate(x, character.top() * 2. - character.texture().size().height() + y);
                        x += character.advance();

                        p.setRectSize(new Vector2D(character.texture().size().width(), character.texture().size().height()));

                        p.setDrawObj(character.texture().imageRef());
                        p.setTransformation(t);
                        draw(Form.SQUARE, p, null);
                    }
                }
            }

        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void draw(ConnectToShader o) {
        boolean useBlend = o.getClass().isAnnotationPresent(BlendFunc.class);
        if (useBlend) {
            GL11.glEnable(GL11.GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        OpenGLShaderProgram shader = new OpenGLShaderProgram(AbstractOpenGLGraphicsManager.class.getResourceAsStream("/" + o.vert()), AbstractOpenGLGraphicsManager.class.getResourceAsStream("/" + o.frag()));



        if (useBlend)
            GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void finish() {
        glfwSwapBuffers(windowHandler);
    }

    @Override
    public void close() {
        glfwTerminate();
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
    public Vector2D mouse() {
        return mouse;
    }

    public Transform2D camera() {
        return camera;
    }

    public Vector2D viewportSize() {
        return viewportSize;
    }

    public AbstractOpenGLGraphicsManager setViewportSize(Vector2D viewportSize) {
        this.viewportSize = viewportSize;
        return this;
    }

    public OpenGLShaderProgram default2DShader() {
        return default2DShader;
    }

    public AbstractOpenGLGraphicsManager setDefault2DShader(OpenGLShaderProgram default2DShader) {
        this.default2DShader = default2DShader;
        return this;
    }

    public OpenGLShaderProgram defaultText2DShader() {
        return defaultText2DShader;
    }

    public AbstractOpenGLGraphicsManager setDefaultText2DShader(OpenGLShaderProgram defaultText2DShader) {
        this.defaultText2DShader = defaultText2DShader;
        return this;
    }

    public Mesh2D squareMesh() {
        return squareMesh;
    }

    public AbstractOpenGLGraphicsManager setSquareMesh(Mesh2D squareMesh) {
        this.squareMesh = squareMesh;
        return this;
    }

    public Matrix4f viewMatrix() {
        return viewMatrix;
    }

    public AbstractOpenGLGraphicsManager setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
        return this;
    }

    public Collection<Input.Key> pressingKeys() {
        return pressingKeys;
    }

    public Collection<Input.MouseButton> pressingMouseButtons() {
        return pressingMouseButtons;
    }

    public long windowHandler() {
        return windowHandler;
    }

    public AbstractOpenGLGraphicsManager setWindowHandler(long windowHandler) {
        this.windowHandler = windowHandler;
        return this;
    }
}
