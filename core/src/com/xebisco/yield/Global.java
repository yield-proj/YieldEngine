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

    /**
     * This function returns an instance of a SwingPlatform class for Java's Swing graphics.
     *
     * @return An instance of the class `SwingPlatform` that implements the `PlatformGraphics` interface.
     */
    public static PlatformGraphics swingPlatform() throws ClassNotFoundException {
        //noinspection unchecked
        Class<? extends PlatformGraphics> swingPlatformImplClass = (Class<? extends PlatformGraphics>) Class.forName("com.xebisco.yield.swingimpl.SwingPlatform");
        try {
            return swingPlatformImplClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
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
}
