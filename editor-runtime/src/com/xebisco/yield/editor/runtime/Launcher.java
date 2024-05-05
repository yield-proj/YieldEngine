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

package com.xebisco.yield.editor.runtime;

import com.xebisco.yield.Application;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.PlatformInit;
import com.xebisco.yield.assets.decompressing.AssetsDecompressing;
import com.xebisco.yield.manager.ApplicationManager;
import com.xebisco.yield.platform.ApplicationModule;
import com.xebisco.yield.platform.ApplicationPlatform;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class Launcher {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String dataFolder = "data";
        if (args.length > 0) {
            dataFolder = args[0];
        }
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        AssetsDecompressing as = new AssetsDecompressing(new File(dataFolder));
        new Application(manager, customOpenGLOpenAL(as), new PlatformInit(PlatformInit.PC_DEFAULT).setWindowIconPath("icon.png"));
        manager.runAndWait();
        as.close();
    }

    public static ApplicationPlatform customOpenGLOpenAL(AssetsDecompressing assetsDecompressing) throws ClassNotFoundException {
        Map<ApplicationModule, Object> modules = new HashMap<>();
        Class<?> openGLGraphicsManagerClass = Class.forName("com.xebisco.yield.openglimpl.GLFWOpenGLGraphicsManager");
        Class<?> openGLFontManagerClass = Class.forName("com.xebisco.yield.openglimpl.OpenGLFontManager");
        Class<?> openGLTextureManagerClass = Class.forName("com.xebisco.yield.openglimpl.OpenGLTextureManager");
        Class<?> openALAudioManagerClass = Class.forName("com.xebisco.yield.openalimpl.OpenALAudio");
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