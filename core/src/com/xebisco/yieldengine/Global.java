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

package com.xebisco.yieldengine;

import com.xebisco.yieldengine.defaults.DefaultFileIOManager;
import com.xebisco.yieldengine.platform.ApplicationModule;
import com.xebisco.yieldengine.platform.ApplicationPlatform;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A static class containing various utility functions and constants.
 */
public final class Global {

    /**
     * Constants representing different axis.
     */
    public static final String HORIZONTAL = "Horizontal", VERTICAL = "Vertical", JUMP = "Jump";
    /**
     * A constant representing the revision number.
     */
    public static final long REV = 511;

    /**
     * A static class containing platform-specific implementations.
     */
    public static final class Platforms {
        /**
         * Creates an instance of the platform using the OpenGL and OpenAL implementations.
         *
         * @return An instance of the platform using the modules.
         * @throws ClassNotFoundException If any of the modules cannot be found.
         */
        public static ApplicationPlatform openGLOpenAL() throws ClassNotFoundException {
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
                modules.put(ApplicationModule.FILE_IO_MANAGER, new DefaultFileIOManager());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            return new ApplicationPlatform(modules);
        }
    }

    /**
     * A static instance of the {@link Random} class.
     */
    public static final Random RANDOM = new Random();

    /**
     * A static string representing the application save ID.
     */
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
     *
     * @return The default directory for application data.
     */
    public static String defaultDirectory() {
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
