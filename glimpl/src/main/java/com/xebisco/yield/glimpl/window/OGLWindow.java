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

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

import static org.lwjgl.opengl.GL.*;
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
            glClear(GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void paintGL() {
            clear();

            if (requestedResize) {
                glViewport(-resWidth / 2, -resHeight / 2, resWidth / 2, resHeight / 2);
                requestedResize = false;
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
