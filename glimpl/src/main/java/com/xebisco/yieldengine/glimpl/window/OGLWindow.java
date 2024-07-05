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

package com.xebisco.yieldengine.glimpl.window;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL20.*;

public class OGLWindow {
    private boolean requestedResize = true;

    private final OGLCanvasImpl canvas;
    private final JFrame frame;
    private final int resWidth, resHeight;
    private final List<Runnable> closeHooks = new ArrayList<>();

    public OGLWindow(GLData glData, int width, int height, int resWidth, int resHeight) {
        this.resWidth = resWidth;
        this.resHeight = resHeight;
        frame = new JFrame("Yield OGL Window");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                closeHooks.forEach(Runnable::run);
            }
        });
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
                glViewport(0, 0, getWidth(), getHeight());
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

    public boolean isRequestedResize() {
        return requestedResize;
    }

    public OGLWindow setRequestedResize(boolean requestedResize) {
        this.requestedResize = requestedResize;
        return this;
    }

    public OGLCanvasImpl getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getResWidth() {
        return resWidth;
    }

    public int getResHeight() {
        return resHeight;
    }

    public List<Runnable> getCloseHooks() {
        return closeHooks;
    }
}
