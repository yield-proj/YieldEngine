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

import com.xebisco.yieldengine.concurrency.LockProcess;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.glimpl.StaticDrawImplementation;
import com.xebisco.yieldengine.glimpl.shader.ShaderCreationException;
import com.xebisco.yieldengine.glimpl.shader.ShaderLinkException;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL20.*;

public class OGLWindow {

    private final Set<Runnable> callInOpenGLThread = new HashSet<>();

    public class RepaintManager implements Runnable {
        @Override
        public void run() {
            paint();
            while (frame.isDisplayable()) {
                try {
                    repaintLock.aWait();
                    do {
                        paint();
                    } while (!callInOpenGLThread.isEmpty());
                    repaintLock = new LockProcess();
                    if (logicLock != null) logicLock.unlock();
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private boolean requestedResize = true;

    private final OGLCanvasImpl canvas;
    private final JFrame frame;
    private final List<Runnable> closeHooks = new ArrayList<>();
    private List<DrawInstruction> instructions;
    private LockProcess repaintLock = new LockProcess(), logicLock, resourcesLock = new LockProcess();
    private final RepaintManager repaintManager = new RepaintManager();
    private final Thread repaintManagerThread = new Thread(repaintManager, "REPAINT_MANAGER");

    public OGLWindow(GLData glData, int width, int height) {
        frame = new JFrame("Yield OGL Window");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeHooks.forEach(Runnable::run);
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    repaintLock.unlock();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.dispose();
                    repaintLock.unlock();
                });
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setRequestedResize(true);
            }
        });
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);

        canvas = new OGLCanvasImpl(glData);

        frame.add(canvas, BorderLayout.CENTER);

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

        frame.setVisible(true);

        if (frame.getContentPane().getHeight() != height) frame.setSize(width, height + frame.getInsets().top);

        frame.transferFocus();

        closeHooks.add(repaintManagerThread::interrupt);

        SwingUtilities.invokeLater(repaintManagerThread::start);
    }

    public OGLWindow(int width, int height) {
        this(new GLData(), width, height);
    }

    class OGLCanvasImpl extends AWTGLCanvas {

        public OGLCanvasImpl(GLData data) {
            super(data);
        }

        @Override
        public void initGL() {
            createCapabilities();
            try {
                StaticDrawImplementation.init();
            } catch (ShaderCreationException | ShaderLinkException e) {
                throw new RuntimeException(e);
            }

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        @Override
        public void paintGL() {

            if (callInOpenGLThread.isEmpty()) {
                if (requestedResize) {
                    glViewport(0, 0, getWidth(), getHeight());
                    requestedResize = false;
                }

                if (instructions != null) {
                    for (DrawInstruction ins : instructions)
                        StaticDrawImplementation.drawInstruction(ins);
                    instructions.clear();
                }

                swapBuffers();
            } else {
                try {
                    callInOpenGLThread.removeIf(r -> {
                        r.run();
                        return true;
                    });
                } catch (ConcurrentModificationException ignore) {
                }
            }
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

    public List<Runnable> getCloseHooks() {
        return closeHooks;
    }

    public List<DrawInstruction> getInstructions() {
        return instructions;
    }

    public OGLWindow setInstructions(List<DrawInstruction> instructions) {
        this.instructions = instructions;
        return this;
    }

    public LockProcess getRepaintLock() {
        return repaintLock;
    }

    public OGLWindow setRepaintLock(LockProcess repaintLock) {
        this.repaintLock = repaintLock;
        return this;
    }

    public RepaintManager getRepaintManager() {
        return repaintManager;
    }

    public Thread getRepaintManagerThread() {
        return repaintManagerThread;
    }

    public LockProcess getLogicLock() {
        return logicLock;
    }

    public OGLWindow setLogicLock(LockProcess logicLock) {
        this.logicLock = logicLock;
        return this;
    }

    public Set<Runnable> getCallInOpenGLThread() {
        return callInOpenGLThread;
    }

    public void setResourcesLock(LockProcess resourcesLock) {
        this.resourcesLock = resourcesLock;
    }
}
