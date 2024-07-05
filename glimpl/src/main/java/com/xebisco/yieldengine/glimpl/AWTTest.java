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

package com.xebisco.yieldengine.glimpl;

import com.xebisco.yieldengine.core.rendering.ArrayContext;
import com.xebisco.yieldengine.core.rendering.VertexArray;
import com.xebisco.yieldengine.glimpl.shader.ShaderProgram;

public class AWTTest {
    static ArrayContext ac;
    static VertexArray positionsArray;
    static ShaderProgram shaderProgram;
    static long window;

    public static void main(String[] args) {

       /* GLFWErrorCallback.createPrint(System.err).set();


        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(300, 300, "Hello World!", 0, 0);
        if ( window == 0 )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();


        OGLRenderer renderer = new OGLRenderer();
        OGLArrayMemory arrayMemory = new OGLArrayMemory();

        ac = arrayMemory.createArrayContext(new int[]{0, 1, 3, 3, 1, 2});

        positionsArray = arrayMemory.createVertexArray(new Vector3fc[]{
                new Vector3f(-0.5f,  0.5f, 0.0f),
                new Vector3f(-0.5f, -0.5f, 0.0f),
                new Vector3f(0.5f, -0.5f, 0.0f),
                new Vector3f(0.5f,  0.5f, 0.0f)
        }, ac);

        try {
            shaderProgram =  ShaderProgram.create("#version 330\n" +
                    "\n" +
                    "layout (location=0) in vec3 position;\n" +
                    "\n" +
                    "out vec3 outColor;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_Position = vec4(position, 1.0);\n" +
                    "    outColor = vec3(1.0, 1.0, 1.0);\n" +
                    "}", "#version 330\n" +
                    "\n" +
                    "in  vec3 outColor;\n" +
                    "out vec4 fragColor;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    fragColor = vec4(outColor, 1.0);\n" +
                    "}");
            shaderProgram.link();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClearColor(1, 0, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            int[] w = new int[1], h = new int[1];

            glfwGetWindowSize(window, w, h);

            glViewport(0, 0, w[0], h[0]);

            renderer.render(shaderProgram, new Uniform[]{}, new VertexArray[]{positionsArray}, ac);

            glfwSwapBuffers(window);
        }*/
    }
}