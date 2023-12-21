package com.xebisco.yield.openglimpl;

import com.xebisco.yield.*;
import com.xebisco.yield.manager.GraphicsManager;
import com.xebisco.yield.manager.PCInputManager;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Paint;
import com.xebisco.yield.texture.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class OpenGLGraphicsManager implements GraphicsManager, PCInputManager {

    private final Vector2D mouse = new Vector2D();

    private Transform2D camera;
    private Vector2D viewportSize;

    private OpenGLShaderProgram default2DShader;

    private Mesh2D squareMesh;

    private Matrix4f viewMatrix;


    private final Collection<Input.Key> pressingKeys = new ArrayList<>();
    private final Collection<Input.MouseButton> pressingMouseButtons = new ArrayList<>();


    private long windowHandler;

    @Override
    public void init(PlatformInit platformInit) {

        viewportSize = platformInit.viewportSize();

        if (!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        windowHandler = glfwCreateWindow((int) platformInit.windowSize().width(), (int) platformInit.windowSize().height(), platformInit.title(), 0, 0);

        glfwSetKeyCallback(windowHandler, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                Input.Key k = GLUtils.intToKey(key);
                if (action == GLFW_PRESS) pressingKeys.add(k);
                else if (action == GLFW_RELEASE) pressingKeys.remove(k);
            }
        });
        glfwSetMouseButtonCallback(windowHandler, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                Input.MouseButton k = GLUtils.intToMouseButton(button);
                if (action == GLFW_PRESS) pressingMouseButtons.add(k);
                else if (action == GLFW_RELEASE) pressingMouseButtons.remove(k);
            }
        });
        glfwSetCursorPosCallback(windowHandler, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    IntBuffer w = stack.mallocInt(1), h = stack.mallocInt(1);
                    glfwGetWindowSize(window, w, h);
                    mouse.set(x * w.get(), y * h.get());
                }
            }
        });
        glfwSetWindowSizeCallback(windowHandler, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                GL11.glViewport(0, 0, w, h);
            }
        });


        glfwMakeContextCurrent(windowHandler);

        if (platformInit.verticalSync()) glfwSwapInterval(1);
        else glfwSwapInterval(0);

        GL.createCapabilities();

        glfwShowWindow(windowHandler);

        default2DShader = new OpenGLShaderProgram(OpenGLGraphicsManager.class.getResourceAsStream("default2d.vert"), OpenGLGraphicsManager.class.getResourceAsStream("default2d.frag"));
        squareMesh = new Mesh2D(new float[]{-100, 100, 100, 100, 100, -100, -100, -100}, new float[]{0, 0, 1, 0, 1, 1, 0, 1}, new int[]{0, 1, 2, 2, 3, 0});
    }

    @Override
    public void updateWindowIcon(Texture icon) {

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
                default2DShader.bind();

                default2DShader.setUniform("transformationMatrix", new Matrix4f().identity().translate(new Vector3f((float) paint.transformation().position().x(), (float) paint.transformation().position().y(), 0)).rotateZ((float) Math.toRadians(paint.transformation().zRotation())).scaleXY((float) paint.transformation().scale().x(), (float) paint.transformation().scale().y()));
                default2DShader.setUniform("viewMatrix", viewMatrix);
                default2DShader.setUniform("color", new Vector4f((float) paint.color().red(), (float) paint.color().green(), (float) paint.color().blue(), (float) paint.color().alpha()));

                if(paint.hasImage()) {
                    default2DShader.setUniform("texture_sampler", 0);
                    default2DShader.setUniform("ignoreTexture", 0);
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, (int) paint.drawObj());
                } else {
                    default2DShader.setUniform("ignoreTexture", 1);
                }

                glBindVertexArray(squareMesh.vao);

                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);

                glDrawElements(GL_TRIANGLES, squareMesh.vertexCount, GL_UNSIGNED_INT, 0);

                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);

                glBindVertexArray(0);
                glBindTexture(GL_TEXTURE_2D, 0);

                default2DShader.unbind();
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void main(String[] args) {

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
}
