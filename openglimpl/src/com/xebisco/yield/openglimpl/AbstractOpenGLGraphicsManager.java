package com.xebisco.yield.openglimpl;

import com.xebisco.yield.Color;
import com.xebisco.yield.Input;
import com.xebisco.yield.Transform2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.font.FontCharacter;
import com.xebisco.yield.manager.GraphicsManager;
import com.xebisco.yield.manager.PCInputManager;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Paint;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class AbstractOpenGLGraphicsManager implements GraphicsManager, PCInputManager {

    private final Vector2D mouse = new Vector2D();

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
    public void draw(Form form, Paint paint) {
        GL11.glEnable(GL11.GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        switch (form) {
            case SQUARE -> {
                OpenGLShaderProgram shader = default2DShader;
                boolean isChar = paint.text() != null;
                if (isChar) shader = defaultText2DShader;


                shader.bind();

                shader.setUniform("transformationMatrix", new Matrix4f().identity().translate(new Vector3f((float) paint.transformation().position().x(), (float) paint.transformation().position().y(), 0)).rotateZ((float) Math.toRadians(paint.transformation().zRotation())).scaleXY((float) (paint.transformation().scale().x() * paint.rectSize().width()), (float) (paint.transformation().scale().y() * paint.rectSize().height())));
                shader.setUniform("viewMatrix", viewMatrix);
                shader.setUniform("color", new Vector4f((float) paint.color().red(), (float) paint.color().green(), (float) paint.color().blue(), (float) paint.color().alpha()));

                if (paint.hasImage()) {
                    shader.setUniform("texture_sampler", 0);
                    if (!isChar)
                        shader.setUniform("ignoreTexture", 0);
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, (int) paint.drawObj());
                } else {
                    shader.setUniform("ignoreTexture", 1);
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
                        t.scale().set(character.texture().size().width() / 100., character.texture().size().height() / 100.);

                        p.setDrawObj(character.texture().imageRef());
                        p.setTransformation(t);
                        draw(Form.SQUARE, p);
                    }
                }
            }

        }
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
