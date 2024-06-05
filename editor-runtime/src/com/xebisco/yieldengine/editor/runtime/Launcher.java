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

package com.xebisco.yieldengine.editor.runtime;

import com.xebisco.yieldengine.Application;
import com.xebisco.yieldengine.ContextTime;
import com.xebisco.yieldengine.PlatformInit;
import com.xebisco.yieldengine.assets.decompressing.AssetsDecompressing;
import com.xebisco.yieldengine.manager.ApplicationManager;
import com.xebisco.yieldengine.platform.ApplicationModule;
import com.xebisco.yieldengine.platform.ApplicationPlatform;
import com.xebisco.yieldengine.utils.Loading;
import com.xebisco.yieldengine.utils.Pair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

class Launcher {
    public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        String dataFolder = "data";
        if (args.length > 1) {
            dataFolder = args[1];
        }
        Scenes.start();
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        AssetsDecompressing as = new AssetsDecompressing(new File(dataFolder));
        PlatformInit platformInit = new PlatformInit(PlatformInit.PC_DEFAULT);
        HashMap<String, HashMap<String, Serializable>> config;
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream("config.ser"))) {
            //noinspection unchecked
            config = (HashMap<String, HashMap<String, Serializable>>) oi.readObject();
        }
        //noinspection unchecked
        Loading.applyPropsToObject((List<Pair<Pair<String, String>, String[]>>) config.get("Window").get("PlatformInit"), platformInit);
        Application app = new Application(manager, customOpenGLOpenAL(as), platformInit);
        AtomicReference<String> startScene = new AtomicReference<>();
        if (args.length > 0) {
            startScene.set(args[0]);
        } else {
            //noinspection unchecked
            ((List<Pair<Pair<String, String>, String[]>>) config.get("Application").get("EditorProject")).forEach(pair -> {
                if (pair.first().first().equals("startScene")) {
                    startScene.set(pair.second()[0]);
                }
            });
        }
        if (startScene.get() == null) throw new IllegalStateException("No start scene");
        app.setScene(Scenes.load(startScene.get(), app));
        manager.runAndWait();
        as.close();
        Scenes.close();
    }

    public static ApplicationPlatform customOpenGLOpenAL(AssetsDecompressing assetsDecompressing) throws ClassNotFoundException {
        Map<ApplicationModule, Object> modules = new HashMap<>();
        Class<?> openGLGraphicsManagerClass = Class.forName("com.xebisco.yieldengine.openglimpl.GLFWOpenGLGraphicsManager");
        Class<?> openGLFontManagerClass = Class.forName("com.xebisco.yieldengine.openglimpl.OpenGLFontManager");
        Class<?> openGLTextureManagerClass = Class.forName("com.xebisco.yieldengine.openglimpl.OpenGLTextureManager");
        Class<?> openALAudioManagerClass = Class.forName("com.xebisco.yieldengine.openalimpl.OpenALAudio");
        try {
            Object openGLGraphicsManager = openGLGraphicsManagerClass.getConstructor().newInstance();
            Object openGLFontManager = openGLFontManagerClass.getConstructor().newInstance();
            Object openGLTextureManager = openGLTextureManagerClass.getConstructor().newInstance();
            Object openALAudioManager = openALAudioManagerClass.getConstructor().newInstance();

            modules.put(ApplicationModule.GRAPHICS_MANAGER, openGLGraphicsManager);
            modules.put(ApplicationModule.PC_INPUT_MANAGER, openGLGraphicsManager);
            modules.put(ApplicationModule.AUDIO_MANAGER, openALAudioManager);
            modules.put(ApplicationModule.TEXTURE_MANAGER, openGLTextureManager);
            modules.put(ApplicationModule.FONT_MANAGER, openGLFontManager);
            modules.put(ApplicationModule.FILE_IO_MANAGER, new EditorIOManager(assetsDecompressing));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return new ApplicationPlatform(modules);
    }
}