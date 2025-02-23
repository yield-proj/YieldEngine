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

import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.utils.concurrency.RunThread;
import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.core.graphics.IPainter;
import com.xebisco.yieldengine.core.graphics.IPainterReceiver;
import com.xebisco.yieldengine.glimpl.YLDG1GLImpl;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL20.*;

public class OGLPanel implements IPainterReceiver {

    private final List<Runnable> callInOpenGLThread = new ArrayList<>();

    private boolean requestedResize = true;
    private final List<Runnable> closeHooks = new ArrayList<>();

    private final OGLCanvasImpl canvas;
    private final JPanel contentPane = new JPanel(new BorderLayout());
    private final Graphics graphics = new Graphics(new YLDG1GLImpl());
    private Queue<IPainter> painters = new LinkedList<>();
    private RunThread glThread = new RunThread();
    private boolean ignoreCloseHooks;

    public static OGLPanel newWindow(int width, int height) {
        Global.debug("Creating SWING UI window.");
        JFrame frame = new JFrame("Yield Engine");
        OGLPanel panel = new OGLPanel();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        frame.add(panel.getContentPane(), BorderLayout.CENTER);

        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            if (frame.getContentPane().getHeight() != height) frame.setSize(width, height + frame.getInsets().top);
        });

        return panel;
    }

    public OGLPanel(GLData glData) {
        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setRequestedResize(true);
            }
        });

        canvas = new OGLCanvasImpl(glData);
    }

    public void runCloseHooks() {
        Global.debug("Running GL implementation close hooks.");
        closeHooks.forEach(Runnable::run);
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            glThread.interrupt();
        });
    }

    public void start() {
        contentPane.add(canvas, BorderLayout.CENTER);
        contentPane.setFocusable(true);

        canvas.setFocusable(false);

        Global.debug("Starting GL implementation thread.");
        glThread.start();
        //paint(null);

        SwingUtilities.invokeLater(() -> {
            Global.debug("Updating SWING UI Component Tree.");
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(contentPane));
            contentPane.requestFocusInWindow();
            SwingUtilities.getWindowAncestor(contentPane).addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if(!ignoreCloseHooks) runCloseHooks();
                }
            });
        });
    }

    public OGLPanel() {
        this(new GLData());
    }

    @Override
    public void paint(Queue<IPainter> painters) {
        glThread.run(() -> {
            this.painters = painters;
            canvas.render();
            getContentPane().revalidate();
            return null;
        });
    }

    public class OGLCanvasImpl extends AWTGLCanvas {

        public OGLCanvasImpl(GLData data) {
            super(data);
        }

        @Override
        public void initGL() {
            Global.debug("Starting OGL.");
            createCapabilities();
            graphics.getG1().initContext();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        @Override
        public void paintGL() {
            if (callInOpenGLThread.isEmpty()) {
                if (requestedResize) {
                    Global.debug("Updating viewport to (" + getWidth() + "x" + getHeight() + ")");
                    glViewport(0, 0, getWidth(), getHeight());
                    requestedResize = false;
                }

                if (painters != null) {
                    IPainter painter;
                    while ((painter = painters.poll()) != null) {
                        painter.onPaint(graphics);
                    }
                }

                swapBuffers();
            } else {
                try {
                    while (!callInOpenGLThread.isEmpty()) {
                        try {
                            callInOpenGLThread.get(0).run();
                        } catch (NullPointerException ignore) {
                        }
                        callInOpenGLThread.remove(0);
                    }
                } catch (ConcurrentModificationException e) {
                    Global.debug(e.toString());
                }
            }
        }
    }

    public boolean isRequestedResize() {
        return requestedResize;
    }

    public OGLPanel setRequestedResize(boolean requestedResize) {
        this.requestedResize = requestedResize;
        return this;
    }

    public boolean isIgnoreCloseHooks() {
        return ignoreCloseHooks;
    }

    public OGLPanel setIgnoreCloseHooks(boolean ignoreCloseHooks) {
        this.ignoreCloseHooks = ignoreCloseHooks;
        return this;
    }

    public OGLCanvasImpl getCanvas() {
        return canvas;
    }

    public List<Runnable> getCallInOpenGLThread() {
        return callInOpenGLThread;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Queue<IPainter> getPainters() {
        return painters;
    }

    public OGLPanel setPainters(Queue<IPainter> painters) {
        this.painters = painters;
        return this;
    }

    public List<Runnable> getCloseHooks() {
        return closeHooks;
    }

    public RunThread getGlThread() {
        return glThread;
    }

    public OGLPanel setGlThread(RunThread glThread) {
        this.glThread = glThread;
        return this;
    }
}
