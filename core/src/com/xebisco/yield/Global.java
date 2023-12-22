/*
 * Copyright [2022-2023] [Xebisco]
 *
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

package com.xebisco.yield;

import com.xebisco.yield.platform.ApplicationModule;
import com.xebisco.yield.platform.ApplicationPlatform;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Static collection of util methods.
 */
public final class Global {

    public static final String HORIZONTAL = "Horizontal", VERTICAL = "Vertical", JUMP = "Jump";

    public static final class Platforms {
        public static ApplicationPlatform openGLOpenAL() throws ClassNotFoundException {
            Map<ApplicationModule, Object> modules = new HashMap<>();
            Class<?> openGLGraphicsManagerClass = Class.forName("com.xebisco.yield.openglimpl.OpenGLGraphicsManager");
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
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            return new ApplicationPlatform(modules);
        }
    }

    public static final Random RANDOM = new Random();

    public static String APP_SAVE_ID;

    /**
     * If value is greater than max, return max, otherwise return the greatest of value and min.
     *
     * @param value The value to clamp.
     * @param min   The minimum value that the returned value can be.
     * @param max   The maximum value that the returned value can be.
     * @return The value of the variable value if it is less than the variable max, otherwise the value of the variable
     * max.
     */
    public static int clamp(int value, int min, int max) {
        return value > max ? max : Math.max(value, min);
    }

    /**
     * If value is greater than max, return max, otherwise return the greatest of value and min.
     *
     * @param value The value to clamp.
     * @param min   The minimum value that the returned value can be.
     * @param max   The maximum value that the returned value can be.
     * @return The value of the variable value if it is less than the variable max, otherwise the value of the variable
     * max.
     */
    public static long clamp(long value, long min, long max) {
        return value > max ? max : Math.max(value, min);
    }

    /**
     * If value is greater than max, return max, otherwise return the greatest of value and min.
     *
     * @param value The value to clamp.
     * @param min   The minimum value that the returned value can be.
     * @param max   The maximum value that the returned value can be.
     * @return The value of the variable value if it is less than the variable max, otherwise the value of the variable
     * max.
     */
    public static double clamp(double value, double min, double max) {
        return value > max ? max : Math.max(value, min);
    }

    /**
     * If value is greater than max, return max, otherwise return the greatest of value and min.
     *
     * @param value The value to clamp.
     * @param min   The minimum value that the returned value can be.
     * @param max   The maximum value that the returned value can be.
     * @return The value of the variable value if it is less than the variable max, otherwise the value of the variable
     * max.
     */
    public static float clamp(float value, float min, float max) {
        return value > max ? max : Math.max(value, min);
    }

    /**
     * The function resizes a given size to fit within a specified boundary while maintaining its aspect ratio.
     *
     * @param size     The size of the object that needs to be resized to fit within the boundary.
     * @param boundary The boundary parameter is a Size2D object that represents the maximum size that the input size can
     *                 be scaled to while maintaining its aspect ratio.
     * @return A new Size2D object with the adjusted width and height values based on the given size and boundary.
     */
    public static Vector2D onSizeBoundary(Vector2D size, Vector2D boundary) {
        double new_width;
        double new_height;

        new_height = boundary.height();
        new_width = (new_height * size.width()) / size.height();
        if (new_width > boundary.width()) {
            new_width = boundary.width();
            new_height = (new_width * size.height()) / size.width();
        }

        return new Vector2D(new_width, new_height);
    }

    /**
     * This function returns the default directory for application data.
     * @return The default directory for application data.
     */
    public static String defaultDirectory()
    {
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN"))
            return System.getenv("APPDATA");
        else if (OS.contains("MAC"))
            return System.getProperty("user.home") + "/Library/Application Support";
        else if (OS.contains("NUX"))
            return System.getProperty("user.home");
        return System.getProperty("user.dir");
    }
}
