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

package com.xebisco.yieldengine.platform;

import com.xebisco.yieldengine.manager.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * Represents the platform of the application, containing various managers.
 */
public record ApplicationPlatform(Map<ApplicationModule, Object> modules) implements Closeable {
    //FONT_MANAGER, TEXTURE_MANAGER, PC_INPUT_MANAGER, AUDIO_MANAGER, GRAPHICS_MANAGER

    /**
     * Get the font manager instance.
     *
     * @return The font manager instance.
     */
    public FontManager fontManager() {
        return (FontManager) modules.get(ApplicationModule.FONT_MANAGER);
    }

    /**
     * Get the texture manager instance.
     *
     * @return The texture manager instance.
     */
    public TextureManager textureManager() {
        return (TextureManager) modules.get(ApplicationModule.TEXTURE_MANAGER);
    }

    /**
     * Get the PC input manager instance.
     *
     * @return The PC input manager instance.
     */
    public PCInputManager pcInputManager() {
        return (PCInputManager) modules.get(ApplicationModule.PC_INPUT_MANAGER);
    }

    /**
     * Get the audio manager instance.
     *
     * @return The audio manager instance.
     */
    public AudioManager audioManager() {
        return (AudioManager) modules.get(ApplicationModule.AUDIO_MANAGER);
    }

    /**
     * Get the graphics manager instance.
     *
     * @return The graphics manager instance.
     */
    public GraphicsManager graphicsManager() {
        return (GraphicsManager) modules.get(ApplicationModule.GRAPHICS_MANAGER);
    }

    /**
     * Get the file input/output manager instance.
     *
     * @return The file input/output manager instance.
     */
    public FileIOManager ioManager() {
        return (FileIOManager) modules.get(ApplicationModule.FILE_IO_MANAGER);
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
