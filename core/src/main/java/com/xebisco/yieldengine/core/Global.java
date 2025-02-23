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

import com.xebisco.yieldengine.core.components.Text;
import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.core.graphics.IPainterReceiver;
import com.xebisco.yieldengine.core.input.IKeyDevice;
import com.xebisco.yieldengine.core.input.IMouseDevice;
import com.xebisco.yieldengine.core.input.Input;
import com.xebisco.yieldengine.core.io.DefaultAbsolutePathGetter;
import com.xebisco.yieldengine.core.io.IAbsolutePathGetter;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.audio.IAudioLoader;
import com.xebisco.yieldengine.core.io.audio.IAudioPlayer;
import com.xebisco.yieldengine.core.io.text.IFontLoader;
import com.xebisco.yieldengine.core.io.texture.ITextureLoader;
import com.xebisco.yieldengine.utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

/**
 * The Global class provides utility methods and factories for the Yield Engine.
 * This class serves as a central access point for core engine functionality
 * and common operations.
 */
public class Global {
    private static Scene currentScene;
    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = new Logger();
    private static String[] tags = new String[] {
            "default",
            "player"
    };
    private static String[] layers = new String[] {
            "default",
            "ui",
            "post"
    };

    public static void log(Object msg) {
        LOGGER.log(msg);
    }

    public static void debug(Object msg) {
        LOGGER.debug(msg);
    }

    /**
     * Creates a text entity factory with the specified text content.
     * This factory can be used to create text entities in a scene.
     *
     * @param contents The text content to be displayed
     * @return An EntityFactory that creates text entities
     */
    public static EntityFactory textFactory(String contents) {
        return () -> new Entity(new EntityHeader("TEXT"), new Transform()).addComponents(new Text(contents));
    }

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

    /**
     * Creates and returns a new OpenGL and OpenAL-based loop context with specified dimensions.
     *
     * @param width The width of the window in pixels
     * @param height The height of the window in pixels
     * @return A new LoopContext configured for OpenGL and OpenAL
     * @throws Exception If initialization fails
     */
    public static LoopContext getOpenGLOpenALLoopContext(int width, int height) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
        Object panel = getOpenGLOpenALLCP0(width, height);
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
        Graphics.setPainterReceiver((IPainterReceiver) panel);
        Class<?> keyDeviceClass = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLKeyDevice"), mouseDevice = Class.forName("com.xebisco.yieldengine.glimpl.window.OGLMouseDevice");
        Input.setInstance(new Input((IKeyDevice) keyDeviceClass.getConstructor(panelClass).newInstance(panel), (IMouseDevice) mouseDevice.getConstructor(panelClass).newInstance(panel)));
        return panel;
    }

    public static LoopContext getOpenGLOpenALLCP1(Object panel, IAbsolutePathGetter absolutePathGetter) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
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
        closeHooks.add(() -> IO.getInstance().dispose());
        panel.getClass().getMethod("start").invoke(panel);
        Class<?> textureLoader = Class.forName("com.xebisco.yieldengine.glimpl.mem.OGLTextureLoader"), fontLoader = Class.forName("com.xebisco.yieldengine.glimpl.mem.OGLFontLoader"), audioLoader = Class.forName("com.xebisco.yieldengine.alimpl.OALAudioLoader"), audioPlayer = Class.forName("com.xebisco.yieldengine.alimpl.OALAudioPlayer");
        IO.setInstance(new IO(absolutePathGetter, (ITextureLoader) textureLoader.getConstructor(panelClass).newInstance(panel), (IFontLoader) fontLoader.getConstructor(panelClass).newInstance(panel), (IAudioLoader) audioLoader.getConstructor().newInstance(), (IAudioPlayer) audioPlayer.getConstructor().newInstance()));
        return l;
    }

    public static LoopContext getOpenGLOpenALLCP1(Object panel) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
        return getOpenGLOpenALLCP1(panel, new DefaultAbsolutePathGetter());
    }

    /**
     * A mathematical utility method that rotates a point around a center point by a given angle using trigonometry.
     *
     * @param angle The angle of rotation in radians
     * @param center The center point around which rotation is performed
     * @param point The point to be rotated
     * @return The rotated point
     */
    public static Vector2f rotatePointAroundCenter(double angle, Vector2fc center, Vector2fc point) {
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        return new Vector2f((float) ((point.x() - center.x()) * cosA - (point.y() - center.y()) * sinA + center.x()), (float) ((point.x() - center.x()) * sinA - (point.y() - center.y()) * cosA + center.y()));
    }

    /**
     * Returns the current active scene in the engine.
     *
     * @return The current scene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Sets the current active scene in the engine.
     * This method should be called before initializing the scene.
     *
     * @param currentScene The scene to be set as current
     */
    public static void setCurrentScene(Scene currentScene) {
        if (Global.currentScene != null) Global.currentScene.dispose();
        Global.currentScene = currentScene;
    }

    public static String[] getTags() {
        return tags;
    }

    public static void setTags(String[] tags) {
        Global.tags = tags;
    }

    public static String[] getLayers() {
        return layers;
    }

    public static void setLayers(String[] layers) {
        Global.layers = layers;
    }
}
