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

import org.jbox2d.common.Vec2;

import java.lang.reflect.InvocationTargetException;

/**
 * Static collection of util methods.
 */
public final class Global {
    public static final String HORIZONTAL = "Horizontal", VERTICAL = "Vertical";
    public static String APP_SAVE_ID;

    /**
     * This function returns an instance of ApplicationPlatform using classes from the com.xebisco.yield.swingimpl package.
     *
     * @return An instance of the `ApplicationPlatform` class is being returned.
     */
    public static ApplicationPlatform swingPlatform() throws ClassNotFoundException {
        Class<?> swingPlatformClass = Class.forName("com.xebisco.yield.swingimpl.SwingPlatform");
        Class<?> clipAudioClass = Class.forName("com.xebisco.yield.swingimpl.ClipAudio");
        try {
            Object swingPlatform = swingPlatformClass.getConstructor().newInstance(), clipAudio = clipAudioClass.getConstructor().newInstance();
            return new ApplicationPlatform(
                    (FontLoader) swingPlatform,
                    (TextureManager) swingPlatform,
                    (InputManager) swingPlatform,
                    null,
                    (MouseCheck) swingPlatform,
                    (AudioManager) clipAudio,
                    (ViewportZoomScale) swingPlatform,
                    (ToggleFullScreen) swingPlatform,
                    (PlatformGraphics) swingPlatform
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This function returns an instance of ApplicationPlatform using classes from the com.xebisco.yield.openglimpl package.
     *
     * @return An instance of the `ApplicationPlatform` class is being returned.
     */
    public static ApplicationPlatform openGLPlatform() throws ClassNotFoundException {
        Class<?> openglPlatformClass = Class.forName("com.xebisco.yield.openglimpl.OpenGLPlatform");
        Class<?> imageLoaderClass = Class.forName("com.xebisco.yield.openglimpl.ImageLoader");
        Class<?> openalAudioClass = Class.forName("com.xebisco.yield.openglimpl.OpenALAudio");
        Class<?> fontLoaderClass = Class.forName("com.xebisco.yield.openglimpl.FontLoader");
        try {
            Object openglPlatform = openglPlatformClass.getConstructor().newInstance(), imageLoader = imageLoaderClass.getConstructor().newInstance(), openalAudio = openalAudioClass.getConstructor().newInstance(), fontLoader = fontLoaderClass.getConstructor().newInstance();
            return new ApplicationPlatform(
                    (FontLoader) fontLoader,
                    (TextureManager) imageLoader,
                    null,
                    (CheckKey) openglPlatform,
                    (MouseCheck) openglPlatform,
                    (AudioManager) openalAudio,
                    (ViewportZoomScale) openglPlatform,
                    (ToggleFullScreen) openglPlatform,
                    (PlatformGraphics) openglPlatform
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The function converts a TwoAnchorRepresentation object to a Vec2 object with the same x and y values.
     *
     * @param twoAnchorRepresentation The generated TwoAnchorRepresentation.
     */
    public static Vec2 toVec2(TwoAnchorRepresentation twoAnchorRepresentation) {
        return new Vec2((float) twoAnchorRepresentation.getX(), (float) twoAnchorRepresentation.getY());
    }

    /**
     * The function converts a Vec2 object to a TwoAnchorRepresentation object with the same x and y values.
     *
     * @param vec2 The generated TwoAnchorRepresentation.
     */
    public static Vector2D toVector2D(Vec2 vec2) {
        return new Vector2D(vec2.x, vec2.y);
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
    public static Size2D onSizeBoundary(Size2D size, Size2D boundary) {
        double new_width;
        double new_height;

        new_height = boundary.getHeight();
        new_width = (new_height * size.getWidth()) / size.getHeight();
        if (new_width > boundary.getWidth()) {
            new_width = boundary.getWidth();
            new_height = (new_width * size.getHeight()) / size.getWidth();
        }

        return new Size2D(new_width, new_height);
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
