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

import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.LoopContext;
import com.xebisco.yieldengine.core.Scene;
import com.xebisco.yieldengine.core.Time;
import com.xebisco.yieldengine.core.rendering.Render;
import com.xebisco.yieldengine.glimpl.mem.OGLArrayMemory;
import com.xebisco.yieldengine.glimpl.window.OGLRenderer;
import com.xebisco.yieldengine.glimpl.window.OGLWindow;
import org.lwjgl.opengl.awt.GLData;

public class Test2 {

    public static void main(String[] args) {
        Time.setTargetFPS(120);
        GLData data = new GLData();
        OGLWindow window = new OGLWindow(data, 1280, 720, 1280, 720);
        OGLRenderer renderer = new OGLRenderer(window);
        OGLArrayMemory arrayMemory = new OGLArrayMemory();
        Render.setInstance(new Render(arrayMemory, renderer));
        Global.setCurrentScene(new Scene());
        LoopContext l = new LoopContext("GAME");
        window.getCloseHooks().add(() -> l.getRunning().set(false));
        l.getThread().start();
    }
}
