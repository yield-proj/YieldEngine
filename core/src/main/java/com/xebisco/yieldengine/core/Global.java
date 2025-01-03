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

package com.xebisco.yieldengine.core;

import com.xebisco.yieldengine.core.input.IKeyDevice;
import com.xebisco.yieldengine.core.input.IMouseDevice;
import com.xebisco.yieldengine.core.input.Input;
import com.xebisco.yieldengine.core.io.DefaultAbsolutePathGetter;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.audio.IAudioLoader;
import com.xebisco.yieldengine.core.io.audio.IAudioPlayer;
import com.xebisco.yieldengine.core.io.text.IFontLoader;
import com.xebisco.yieldengine.core.io.texture.ITextureLoader;
import com.xebisco.yieldengine.core.render.IRenderer;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Global {
    private static Scene currentScene;
    public static final Random RANDOM = new Random();

    /*public static LoopContext getOpenGLOpenALLoopContext(int width, int height) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        System.setProperty("sun.java2d.opengl", "True");
        Class<?> soundManagerClass = Class.forName("com.xebisco.yieldengine.alimpl.SoundManager");
        soundManagerClass.getMethod("initAL").invoke(null);
        Class<?> panelClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLPanel");
        Object panel;
        if(width >= 0 && height >= 0) panel = panelClass.getMethod("newWindow", int.class, int.class).invoke(null, width, height);
        else panel = panelClass.getConstructor().newInstance();
        panelClass.getMethod("start").invoke(panel);
        Class<?> keyDeviceClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLKeyDevice"), mouseDevice = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLMouseDevice");
        Input.setInstance(new Input((IKeyDevice) keyDeviceClass.getConstructor(panelClass).newInstance(panel), (IMouseDevice) mouseDevice.getConstructor(panelClass).newInstance(panel)));
        Class<?> textureLoader = Class.forName("com.xebisco.yieldengine.glimpl.mem.OGLTextureLoader"), fontLoader = Class.forName("com.xebisco.yieldengine.glimpl.mem.OGLFontLoader"), audioLoader = Class.forName("com.xebisco.yieldengine.alimpl.OALAudioLoader"), audioPlayer = Class.forName("com.xebisco.yieldengine.alimpl.OALAudioPlayer");
        IO.setInstance(new IO(new DefaultAbsolutePathGetter(), (ITextureLoader) textureLoader.getConstructor(panelClass).newInstance(panel), (IFontLoader) fontLoader.getConstructor(panelClass).newInstance(panel), (IAudioLoader) audioLoader.getConstructor().newInstance(), (IAudioPlayer) audioPlayer.getConstructor().newInstance()));
        Class<?> rendererClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLRenderer");
        Object renderer = rendererClass.getConstructor(panelClass).newInstance(panel);
        Render.setInstance(new Render((IRenderer) renderer));
        LoopContext l = new LoopContext("GAME");
        l.setDeviceObject(panel);
        //noinspection unchecked
        List<Runnable> closeHooks = (List<Runnable>) panelClass.getMethod("getCloseHooks").invoke(panel);
        closeHooks.add(() -> l.getRunning().set(false));
        Method soundManagerGetRunning = soundManagerClass.getMethod("getRunning");
        closeHooks.add(() -> {
            try {
                Object running = soundManagerGetRunning.invoke(null);
                running.getClass().getMethod("set", boolean.class).invoke(running, false);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        closeHooks.add(() -> Render.getInstance().dispose());
        closeHooks.add(() -> IO.getInstance().dispose());
        return l;
    }*/

    public static LoopContext getOpenGLOpenALLoopContext(int width, int height) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
        Object panel = getOpenGLOpenALLCP0(width, height);
        Thread.sleep(200);
        return getOpenGLOpenALLCP1(panel);
    }

    public static Object getOpenGLOpenALLCP0(int width, int height) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> soundManagerClass = Class.forName("com.xebisco.yieldengine.alimpl.SoundManager");
        soundManagerClass.getMethod("initAL").invoke(null);
        Class<?> panelClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLPanel");
        Object panel;
        if (width >= 0 && height >= 0)
            panel = panelClass.getMethod("newWindow", int.class, int.class).invoke(null, width, height);
        else panel = panelClass.getConstructor().newInstance();
        Class<?> keyDeviceClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLKeyDevice"), mouseDevice = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLMouseDevice");
        Input.setInstance(new Input((IKeyDevice) keyDeviceClass.getConstructor(panelClass).newInstance(panel), (IMouseDevice) mouseDevice.getConstructor(panelClass).newInstance(panel)));
        Class<?> textureLoader = Class.forName("com.xebisco.yieldengine.glimpl.mem.OGLTextureLoader"), fontLoader = Class.forName("com.xebisco.yieldengine.glimpl.mem.OGLFontLoader"), audioLoader = Class.forName("com.xebisco.yieldengine.alimpl.OALAudioLoader"), audioPlayer = Class.forName("com.xebisco.yieldengine.alimpl.OALAudioPlayer");
        IO.setInstance(new IO(new DefaultAbsolutePathGetter(), (ITextureLoader) textureLoader.getConstructor(panelClass).newInstance(panel), (IFontLoader) fontLoader.getConstructor(panelClass).newInstance(panel), (IAudioLoader) audioLoader.getConstructor().newInstance(), (IAudioPlayer) audioPlayer.getConstructor().newInstance()));
        Class<?> rendererClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLRenderer");
        Object renderer = rendererClass.getConstructor(panelClass).newInstance(panel);
        Render.setInstance(new Render((IRenderer) renderer));
        return panel;
    }

    public static LoopContext getOpenGLOpenALLCP1(Object panel) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> soundManagerClass = Class.forName("com.xebisco.yieldengine.alimpl.SoundManager");
        Class<?> panelClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLPanel");
        LoopContext l = new LoopContext("GAME");
        l.setDeviceObject(panel);
        //noinspection unchecked
        List<Runnable> closeHooks = (List<Runnable>) panelClass.getMethod("getCloseHooks").invoke(panel);
        closeHooks.add(() -> l.getRunning().set(false));
        Method soundManagerGetRunning = soundManagerClass.getMethod("getRunning");
        closeHooks.add(() -> {
            try {
                Object running = soundManagerGetRunning.invoke(null);
                running.getClass().getMethod("set", boolean.class).invoke(running, false);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        closeHooks.add(() -> Render.getInstance().dispose());
        closeHooks.add(() -> IO.getInstance().dispose());
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(200);
                panel.getClass().getMethod("start").invoke(panel);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(e -> {
            throw new RuntimeException(e);
        });
        return l;
    }

    public static Vector2f rotatePointAroundCenter(double angle, Vector2fc center, Vector2fc point) {
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        return new Vector2f((float) ((point.x() - center.x()) * cosA - (point.y() - center.y()) * sinA + center.x()), (float) ((point.x() - center.x()) * sinA - (point.y() - center.y()) * cosA + center.y()));
    }

    public static Vector4f rgb(int color) {
        Vector4f vec = new Vector4f();
        vec.x = ((color >> 16) & 0xFF) / 255f;
        vec.y = ((color >> 8) & 0xFF) / 255f;
        vec.z = ((color) & 0xFF) / 255f;
        vec.w = 1;
        return vec;
    }

    public static Vector4f argb(int color) {
        Vector4f vec = new Vector4f();
        vec.x = ((color >> 24) & 0xFF) / 255f;
        vec.y = ((color >> 16) & 0xFF) / 255f;
        vec.z = ((color >> 8) & 0xFF) / 255f;
        vec.w = ((color) & 0xFF) / 255f;
        return vec;
    }

    public int argb(Vector4fc color) {
        return to8BitInt(color.w(), color.x(), color.y(), color.z());
    }

    private static int to8BitInt(double alpha, double red, double green, double blue) {
        return ((int) (alpha * 255)) << 24 + ((int) (red * 255)) << 16 + ((int) (green * 255)) << 8 + ((int) (blue * 255));
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void setCurrentScene(Scene currentScene) {
        if (Global.currentScene != null) Global.currentScene.dispose();
        Global.currentScene = currentScene;
    }
}
