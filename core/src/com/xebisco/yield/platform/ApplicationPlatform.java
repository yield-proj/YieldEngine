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

package com.xebisco.yield.platform;

import com.xebisco.yield.manager.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * The class ApplicationPlatform contains various managers and utilities for a Yield application.
 */
public record ApplicationPlatform(Map<ApplicationModule, Object> modules) implements Closeable {
    //FONT_MANAGER, TEXTURE_MANAGER, PC_INPUT_MANAGER, AUDIO_MANAGER, GRAPHICS_MANAGER
    public FontManager fontManager() {
        return (FontManager) modules.get(ApplicationModule.FONT_MANAGER);
    }
    public TextureManager textureManager() {
        return (TextureManager) modules.get(ApplicationModule.TEXTURE_MANAGER);
    }
    public PCInputManager pcInputManager() {
        return (PCInputManager) modules.get(ApplicationModule.PC_INPUT_MANAGER);
    }
    public AudioManager audioManager() {
        return (AudioManager) modules.get(ApplicationModule.AUDIO_MANAGER);
    }
    public GraphicsManager graphicsManager() {
        return (GraphicsManager) modules.get(ApplicationModule.GRAPHICS_MANAGER);
    }

    @Override
    public void close() throws IOException {
        for(Object mod : modules.values()) {
            if(mod instanceof Closeable c) {
                c.close();
            }
        }
    }
}
