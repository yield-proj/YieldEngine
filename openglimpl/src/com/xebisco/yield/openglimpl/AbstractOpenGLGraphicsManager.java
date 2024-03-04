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

import com.xebisco.yield.*;
import com.xebisco.yield.font.FontCharacter;
import com.xebisco.yield.manager.GraphicsManager;
import com.xebisco.yield.manager.PCInputManager;
import com.xebisco.yield.openglimpl.shader.*;
import com.xebisco.yield.openglimpl.shader.types.*;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Paint;
import com.xebisco.yield.utils.Pair;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class AbstractOpenGLGraphicsManager implements GraphicsManager, PCInputManager {

    private final Vector2D mouse = new Vector2D();

    private final Map<String, OpenGLShaderProgram> loadedShaders = new HashMap<>();
    private final Map<Object, AbstractMesh> loadedMeshes = new HashMap<>();

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
            draw(caller);
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

                glBindVertexArray(squareMesh.vao());

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

    public void draw(Object o) {
        boolean useBlend = o.getClass().isAnnotationPresent(BlendFunc.class);
        if (useBlend) {
            GL11.glEnable(GL11.GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        OpenGLShaderProgram shader = new OpenGLShaderProgram(AbstractOpenGLGraphicsManager.class.getResourceAsStream("/" + o.getClass().getAnnotation(ConnectToShader.class).vert()), AbstractOpenGLGraphicsManager.class.getResourceAsStream("/" + o.getClass().getAnnotation(ConnectToShader.class).frag()));

        //Create mesh if not existent
        if (!loadedMeshes.containsKey(o)) {
            List<Pair<float[], Pair<Integer, Integer>>> attribArrays = new ArrayList<>();
            List<int[]> elements = new ArrayList<>();
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(AttribArray.class)) {
                    if (!Modifier.isFinal(field.getModifiers())) {
                        throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be final!");
                    }
                    if (!Modifier.isStatic(field.getModifiers())) {
                        throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be static!");
                    }
                    if (!Utils.isClassCompatibleAttribShaderType(field.getType())) {
                        throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be of a compatible attrib array type!");
                    }

                    Object[] lo;
                    try {
                        lo = (Object[]) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    switch (field.getType().getSimpleName()) {
                        case "Vec2[]" -> {
                            float[] fa = new float[lo.length * 2];
                            for (int i = 0; i < lo.length; i++) {
                                fa[i * 2] = ((Vec2) lo[i]).x();
                                fa[i * 2 + 1] = ((Vec2) lo[i]).y();
                            }
                            attribArrays.add(new Pair<>(fa, new Pair<>(2, field.getAnnotation(AttribArray.class).index())));
                        }
                        case "Vec3[]" -> {
                            float[] fa = new float[lo.length * 3];
                            for (int i = 0; i < lo.length; i++) {
                                fa[i * 3] = ((Vec3) lo[i]).x();
                                fa[i * 3 + 1] = ((Vec3) lo[i]).y();
                                fa[i * 3 + 2] = ((Vec3) lo[i]).z();
                            }
                            attribArrays.add(new Pair<>(fa, new Pair<>(3, field.getAnnotation(AttribArray.class).index())));
                        }
                        case "Vec4[]" -> {
                            float[] fa = new float[lo.length * 4];
                            for (int i = 0; i < lo.length; i++) {
                                fa[i * 4] = ((Vec4) lo[i]).x();
                                fa[i * 4 + 1] = ((Vec4) lo[i]).y();
                                fa[i * 4 + 2] = ((Vec4) lo[i]).z();
                                fa[i * 4 + 3] = ((Vec4) lo[i]).w();
                            }
                            attribArrays.add(new Pair<>(fa, new Pair<>(4, field.getAnnotation(AttribArray.class).index())));
                        }
                        default -> throw new IllegalStateException(field.getType().getSimpleName());
                    }
                } else {
                    if (field.isAnnotationPresent(Element.class)) {
                        if (!Modifier.isFinal(field.getModifiers())) {
                            throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be final!");
                        }
                        if (!Modifier.isStatic(field.getModifiers())) {
                            throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be static!");
                        }
                        if (!(field.getType().equals(int[].class) || field.getType().equals(Integer[].class))) {
                            throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be an int array!");
                        }

                        try {
                            elements.add((int[]) field.get(null));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            loadedMeshes.put(o, abstractMesh(attribArrays, elements));
            System.gc();
        }

        shader.bind();

        //Load uniforms

        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Uniform.class)) {
                try {
                    Object v = field.get(o);
                    if (!Utils.isClassCompatibleShaderType(field.getType())) {
                        throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be of a compatible type!");
                    }
                    String name = field.getAnnotation(Uniform.class).name();
                    if (name.isEmpty()) name = field.getName();
                    switch (field.getType().getSimpleName()) {
                        case "Vec2" -> shader.setUniform(name, (Vec2) v);
                        case "Vec3" -> shader.setUniform(name, (Vec3) v);
                        case "Vec4" -> shader.setUniform(name, (Vec4) v);
                        case "Mat2" -> shader.setUniform(name, (Mat2) v);
                        case "Mat3" -> shader.setUniform(name, (Mat3) v);
                        case "Mat4" -> shader.setUniform(name, (Mat4) v);
                        case "Sampler2D" -> shader.setUniform(name, (Sampler2D) v);
                        case "byte", "Byte" -> shader.setUniform(name, (byte) v);
                        case "short", "Short" -> shader.setUniform(name, (short) v);
                        case "int", "Integer" -> shader.setUniform(name, (int) v);
                        case "long", "Long" -> shader.setUniform(name, (long) v);
                        case "float", "Float" -> shader.setUniform(name, (float) v);
                        default ->
                                throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName());
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (field.isAnnotationPresent(Texture2D.class)) {
                try {
                    Object v = field.get(o);
                    if (!(v instanceof AbstractTexture))
                        throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " needs to be an instance of AbstractTexture!");
                    switch (field.getAnnotation(Texture2D.class).activate()) {
                        case 0 -> glActiveTexture(GL_TEXTURE0);
                        case 1 -> glActiveTexture(GL_TEXTURE1);
                        case 2 -> glActiveTexture(GL_TEXTURE2);
                        case 3 -> glActiveTexture(GL_TEXTURE3);
                        case 4 -> glActiveTexture(GL_TEXTURE4);
                        case 5 -> glActiveTexture(GL_TEXTURE5);
                        case 6 -> glActiveTexture(GL_TEXTURE6);
                        case 7 -> glActiveTexture(GL_TEXTURE7);
                        case 8 -> glActiveTexture(GL_TEXTURE8);
                        case 9 -> glActiveTexture(GL_TEXTURE9);
                        case 10 -> glActiveTexture(GL_TEXTURE10);
                        case 11 -> glActiveTexture(GL_TEXTURE11);
                        case 12 -> glActiveTexture(GL_TEXTURE12);
                        case 13 -> glActiveTexture(GL_TEXTURE13);
                        case 14 -> glActiveTexture(GL_TEXTURE14);
                        case 15 -> glActiveTexture(GL_TEXTURE15);
                        case 16 -> glActiveTexture(GL_TEXTURE16);
                        case 17 -> glActiveTexture(GL_TEXTURE17);
                        case 18 -> glActiveTexture(GL_TEXTURE18);
                        case 19 -> glActiveTexture(GL_TEXTURE19);
                        case 20 -> glActiveTexture(GL_TEXTURE20);
                        case 21 -> glActiveTexture(GL_TEXTURE21);
                        case 22 -> glActiveTexture(GL_TEXTURE22);
                        case 23 -> glActiveTexture(GL_TEXTURE23);
                        case 24 -> glActiveTexture(GL_TEXTURE24);
                        case 25 -> glActiveTexture(GL_TEXTURE25);
                        case 26 -> glActiveTexture(GL_TEXTURE26);
                        case 27 -> glActiveTexture(GL_TEXTURE27);
                        case 28 -> glActiveTexture(GL_TEXTURE28);
                        case 29 -> glActiveTexture(GL_TEXTURE29);
                        case 30 -> glActiveTexture(GL_TEXTURE30);
                        case 31 -> glActiveTexture(GL_TEXTURE31);
                        default ->
                                throw new IllegalStateException(o.getClass().getSimpleName() + ": " + field.getName() + " 31 >= activate >= 0");
                    }

                    glBindTexture(GL_TEXTURE_2D, (int) ((AbstractTexture) v).imageRef());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        AbstractMesh mesh = loadedMeshes.get(o);
        glBindVertexArray(mesh.vao());

        for(int i = 0; i < mesh.vbos().size() - 1; i++) glEnableVertexAttribArray(i);

        glDrawElements(DrawMode.toGL(o.getClass().getAnnotation(ConnectToShader.class).mode()), o.getClass().getAnnotation(ConnectToShader.class).count(), DataType.toGL(o.getClass().getAnnotation(ConnectToShader.class).type()), o.getClass().getAnnotation(ConnectToShader.class).indices());

        for(int i = 0; i < mesh.vbos().size() - 1; i++) glEnableVertexAttribArray(i);

        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);

        shader.unbind();


        if (useBlend)
            GL11.glDisable(GL11.GL_BLEND);
    }

    private static AbstractMesh abstractMesh(List<Pair<float[], Pair<Integer, Integer>>> toLoad, List<int[]> elements) {
        AbstractMesh mesh = new AbstractMesh() {
            @Override
            public void init() {
                int i = 0;
                int vbo;
                for (Pair<float[], Pair<Integer, Integer>> tl : toLoad) {
                    vbo = glGenBuffers();
                    FloatBuffer buffer = MemoryUtil.memCallocFloat(tl.first().length);
                    buffer.put(tl.first());
                    buffer.flip();
                    glBindBuffer(GL_ARRAY_BUFFER, vbo);
                    glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
                    if(tl.second().second() >= 0) i = tl.second().second();
                    glEnableVertexAttribArray(i);
                    glVertexAttribPointer(i, tl.second().first(), GL_FLOAT, false, 0, 0);
                    i++;
                    MemoryUtil.memFree(buffer);
                }

                for (int[] tl : elements) {
                    vbo = glGenBuffers();
                    vbos.add(vbo);
                    IntBuffer indicesBuffer = MemoryUtil.memCallocInt(tl.length);
                    indicesBuffer.put(tl).flip();
                    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
                    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
                    MemoryUtil.memFree(indicesBuffer);
                }

                glBindBuffer(GL_ARRAY_BUFFER, 0);
                glBindVertexArray(0);
            }
        };
        mesh.init();
        return mesh;
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
