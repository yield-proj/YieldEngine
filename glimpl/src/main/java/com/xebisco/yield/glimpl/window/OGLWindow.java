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

package com.xebisco.yield.glimpl.window;

import com.xebisco.yield.core.rendering.ArrayContext;
import com.xebisco.yield.core.rendering.InvalidShaderTypeException;
import com.xebisco.yield.core.rendering.Uniform;
import com.xebisco.yield.core.rendering.VertexArray;
import com.xebisco.yield.glimpl.mem.OGLArrayMemory;
import com.xebisco.yield.glimpl.shader.ShaderCreationException;
import com.xebisco.yield.glimpl.shader.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL20.*;

public class OGLWindow {
    private boolean requestedResize = true;

    private final OGLCanvasImpl canvas;
    private final JFrame frame;
    private final int resWidth, resHeight;

    public OGLWindow(GLData glData, int width, int height, int resWidth, int resHeight) {
        this.resWidth = resWidth;
        this.resHeight = resHeight;
        frame = new JFrame("Yield OGL Window");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        canvas = new OGLCanvasImpl(glData);

        frame.add(canvas, BorderLayout.CENTER);

        frame.setVisible(true);

        frame.transferFocus();

        paint();
    }

    class OGLCanvasImpl extends AWTGLCanvas {

        public OGLCanvasImpl(GLData data) {
            super(data);
        }

        @Override
        public void initGL() {
            createCapabilities();
            glClearColor(0, 0, 0, 1);
        }

        private void clear() {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }

        @Override
        public void paintGL() {
            clear();

            if (requestedResize) {
                //glViewport(-resWidth / 2, -resHeight / 2, resWidth / 2, resHeight / 2);
                glViewport(-1, -1, 1, 1);
                requestedResize = false;
            }

            OGLRenderer renderer = new OGLRenderer();

            OGLArrayMemory arrayMemory = new OGLArrayMemory();
            ArrayContext ac = arrayMemory.createArrayContext(new int[]{0, 1, 3, 3, 1, 2});

            VertexArray positionsArray = arrayMemory.createVertexArray(new Vector3fc[]{
                    new Vector3f(-0.5f,  0.5f, 0.0f),
                    new Vector3f(-0.5f, -0.5f, 0.0f),
                    new Vector3f(0.5f, -0.5f, 0.0f),
                    new Vector3f(0.5f,  0.5f, 0.0f)
            }, ac);

            try {
                ShaderProgram shaderProgram = ShaderProgram.create("#version 330\n" + "\n" + "layout (location =0) in vec3 position;\n" + "\n" + "void main()\n" + "{\n" + "    gl_Position = vec4(position, 1.0);\n" + "}", "#version 330\n" + "\n" + "out vec4 fragColor;\n" + "\n" + "void main()\n" + "{\n" + "    fragColor = vec4(1.0, 1.0, 1.0, 1.0);\n" + "}");

                renderer.render(shaderProgram, new Uniform[]{}, new VertexArray[]{positionsArray}, ac);
            } catch (ShaderCreationException e) {
                throw new RuntimeException(e);
            }

            swapBuffers();
        }
    }

    public void paint() {
        if (!canvas.isValid()) {
            GL.setCapabilities(null);
            return;
        }
        canvas.render();
    }
}
