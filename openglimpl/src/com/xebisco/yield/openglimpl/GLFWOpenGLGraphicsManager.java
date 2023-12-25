package com.xebisco.yield.openglimpl;

import com.xebisco.yield.Input;
import com.xebisco.yield.PlatformInit;
import com.xebisco.yield.texture.Texture;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.stb.STBImage.stbi_load;

public class GLFWOpenGLGraphicsManager extends AbstractOpenGLGraphicsManager {
    @Override
    public void init(PlatformInit platformInit) {
        setViewportSize(platformInit.viewportSize());

        if (!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        setWindowHandler(glfwCreateWindow((int) platformInit.windowSize().width(), (int) platformInit.windowSize().height(), platformInit.title(), 0, 0));

        glfwSetKeyCallback(windowHandler(), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                Input.Key k = GLUtils.intToKey(key);
                if (action == GLFW_PRESS) pressingKeys().add(k);
                else if (action == GLFW_RELEASE) pressingKeys().remove(k);
            }
        });
        glfwSetMouseButtonCallback(windowHandler(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                Input.MouseButton k = GLUtils.intToMouseButton(button);
                if (action == GLFW_PRESS) pressingMouseButtons().add(k);
                else if (action == GLFW_RELEASE) pressingMouseButtons().remove(k);
            }
        });
        glfwSetCursorPosCallback(windowHandler(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    IntBuffer w = stack.mallocInt(1), h = stack.mallocInt(1);
                    glfwGetWindowSize(window, w, h);
                    mouse().set(x * w.get(), y * h.get());
                }
            }
        });
        glfwSetWindowSizeCallback(windowHandler(), new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                GL11.glViewport(0, 0, w, h);
            }
        });


        glfwMakeContextCurrent(windowHandler());

        if (platformInit.verticalSync()) glfwSwapInterval(1);
        else glfwSwapInterval(0);

        glfwShowWindow(windowHandler());

        initAll();
    }

    @Override
    public void updateWindowIcon(Texture icon) {
        ByteBuffer image;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = stbi_load(icon.path(), w, h, comp, 4);
            width = w.get();
            height = h.get();
        }
        GLFWImage imageB = GLFWImage.malloc();
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        assert image != null;
        imageB.set(width, height, image);
        imagebf.put(0, imageB);
        glfwSetWindowIcon(windowHandler(), imagebf);
    }
}
