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

package com.xebisco.yield.glimpl;

import com.xebisco.yield.core.rendering.ArrayContext;
import com.xebisco.yield.core.rendering.Uniform;
import com.xebisco.yield.core.rendering.VertexArray;
import com.xebisco.yield.glimpl.mem.OGLArrayMemory;
import com.xebisco.yield.glimpl.shader.ShaderCreationException;
import com.xebisco.yield.glimpl.shader.ShaderProgram;
import com.xebisco.yield.glimpl.window.OGLRenderer;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL46.*;

public class AWTTest {
    static ArrayContext ac;
    static VertexArray positionsArray;
    static ShaderProgram shaderProgram;

    public static void main(String[] args) {
        JFrame frame = new JFrame("AWT test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(600, 600));
        GLData data = new GLData();
        AWTGLCanvas canvas;
        OGLRenderer renderer = new OGLRenderer();
        OGLArrayMemory arrayMemory = new OGLArrayMemory();
        frame.add(canvas = new AWTGLCanvas(data) {
            private static final long serialVersionUID = 1L;
            @Override
            public void initGL() {
                System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
                createCapabilities();
                glClearColor(0.3f, 0.4f, 0.5f, 1);

                ac = arrayMemory.createArrayContext(new int[]{0, 1, 3, 3, 1, 2});

                positionsArray = arrayMemory.createVertexArray(new Vector3fc[]{
                        new Vector3f(-0.5f,  0.5f, 0.0f),
                        new Vector3f(-0.5f, -0.5f, 0.0f),
                        new Vector3f(0.5f, -0.5f, 0.0f),
                        new Vector3f(0.5f,  0.5f, 0.0f)
                }, ac);

                try {
                    shaderProgram =  ShaderProgram.create("", "");
                } catch (ShaderCreationException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void paintGL() {
                int w = getWidth();
                int h = getHeight();
                float aspect = (float) w / h;
                glClear(GL_COLOR_BUFFER_BIT);
                glViewport(0, 0, 1, 1);

                renderer.render(shaderProgram, new Uniform[]{}, new VertexArray[]{}, ac);

                swapBuffers();
            }
        }, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.transferFocus();
        Runnable renderLoop = new Runnable() {
			@Override
            public void run() {
				if (!canvas.isValid()) {
                    GL.setCapabilities(null);
                    return;
                }
				canvas.render();
				SwingUtilities.invokeLater(this);
			}
		};
		SwingUtilities.invokeLater(renderLoop);
    }
}