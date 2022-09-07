/*
 * Copyright [2022] [Xebisco]
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

import com.xebisco.yield.render.ExceptionThrower;
import org.jbox2d.common.Vec2;

import java.lang.reflect.InvocationTargetException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * A class that hold global properties and methods of Yield Game Engine
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public final class Yld {
    private static ExceptionThrower exceptionThrower;
    /**
     * The version of the Yield Game Engine.
     */
    public static final String VERSION = "4 - 1.2.2.1 test";
    /**
     * All the Yield Game Engine messages.
     */

    private static final long BUILD = 2022;

    public final static MathContext roundDownContext = new MathContext(2, RoundingMode.DOWN);
    public final static MathContext roundUpContext = new MathContext(2, RoundingMode.UP);

    /**
     * Converts a Vector2 to a Box2D Vec2.
     *
     * @param vector2 The vector2 to convert to a Vec2.
     * @return A new Vec2 object with the x and y values of the Vector2 object.
     */
    public static Vec2 toVec2(Vector2 vector2) {
        return new Vec2(vector2.x, vector2.y);
    }

    /**
     * Converts a Box2D Vec2 to a Vector2.
     *
     * @param vector2 The vector2 to convert to a Vector2.
     * @return A new Vector2 object.
     */
    public static Vector2 toVector2(Vec2 vector2) {
        return new Vector2(vector2.x, vector2.y);
    }

    public static final ArrayList<String> MESSAGES = new ArrayList<>();
    /**
     * The standard Random library instance.
     */
    public static final Random RAND = new Random();
    /**
     * If Yield Game Engine is in debug mode ot not.
     */
    public static boolean debug;

    /**
     * The memory in use in the actual Java Virtual Machine.
     *
     * @return The memory in use.
     */
    public static int MEMORY() {
        return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }

    /**
     * The free memory of the actual Java Virtual Machine.
     *
     * @return The free memory.
     */
    public static int MAX_MEMORY() {
        return (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
    }

    /**
     * If there's an exceptionThrower, throw the exception. Otherwise, print the stack trace
     *
     * @param e The exception to throw.
     */
    public static void throwException(Exception e) {
        e.printStackTrace();
        if (exceptionThrower != null)
            exceptionThrower.throwException(e);
    }

    /**
     * Adds a message to the messages list, and logs to the standard output.
     *
     * @param msg The message to be added.
     */
    public static void message(Object msg) {
        //YieldOverlay.setShow(true);
        msg = "(" + new Date() + ") " + msg;
        MESSAGES.add(0, msg.toString());
        if (Yld.MESSAGES.size() > 9) {
            Yld.MESSAGES.remove(Yld.MESSAGES.size() - 1);
        }
        System.out.println(msg);
    }

    /**
     * Executes an instance of a YldAction if the debug variable is set to TRUE.
     *
     * @param action The action to be performed.
     */
    public static void debug(YldAction action) {
        if (debug) {
            action.onAction();
        }
    }

    /**
     * Executes an instance of a YldAction if the debug variable is set to FALSE.
     *
     * @param action The action to be performed.
     */
    public static void release(YldAction action) {
        if (!debug)
            action.onAction();
    }

    /**
     * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
     *
     * @param value The value to clamp.
     * @param min The minimum value that the returned value can be.
     * @param max The maximum value that the returned value can be.
     * @return The max value of the two values.
     */
    public static int clamp(int value, int min, int max) {
        if (value > max) return max;
        return Math.max(value, min);
    }

    /**
     * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
     *
     * @param value The value to clamp.
     * @param min The minimum value that the returned value can be.
     * @param max The maximum value that the returned value can be.
     * @return The max value of the two values.
     */
    public static long clamp(long value, long min, long max) {
        if (value > max) return max;
        return Math.max(value, min);
    }

    /**
     * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
     *
     * @param value The value to clamp.
     * @param min The minimum value that the returned value can be.
     * @param max The maximum value that the returned value can be.
     * @return The max value of the two values.
     */
    public static double clamp(double value, double min, double max) {
        if (value > max) return max;
        return Math.max(value, min);
    }

    /**
     * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
     *
     * @param value The value to clamp.
     * @param min The minimum value that the returned value can be.
     * @param max The maximum value that the returned value can be.
     * @return The max value of the two values.
     */
    public static float clamp(float value, float min, float max) {
        if (value > max) return max;
        return Math.max(value, min);
    }

    /**
     * If the value is less than zero, return the negative of the value, otherwise return the value.
     *
     * @param value The value to be modified.
     * @return The absolute value of the input.
     */
    public static float mod(float value) {
        if(value < 0)
            return -value;
        else return value;
    }

    /**
     * If the value is less than zero, return the negative of the value, otherwise return the value.
     *
     * @param value The value to be modified.
     * @return The absolute value of the input.
     */
    public static int mod(int value) {
        if(value < 0)
            return -value;
        else return value;
    }

    /**
     * If the value is less than zero, return the negative of the value, otherwise return the value.
     *
     * @param value The value to be modified.
     * @return The absolute value of the input.
     */
    public static long mod(long value) {
        if(value < 0)
            return -value;
        else return value;
    }

    /**
     * If the value is less than zero, return the negative of the value, otherwise return the value.
     *
     * @param value The value to be modified.
     * @return The absolute value of the input.
     */
    public static double mod(double value) {
        if(value < 0)
            return -value;
        else return value;
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param value The value to calculate the cosine of.
     * @return The cosine of the value.
     */
    public static float cos(float value) {
        return (float) Math.cos(value);
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param value The value to calculate the cosine of.
     * @return The cosine of the value.
     */
    public static int cos(int value) {
        return (int) Math.cos(value);
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param value The value to calculate the cosine of.
     * @return The cosine of the value.
     */
    public static long cos(long value) {
        return (long) Math.cos(value);
    }

    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param value The value to calculate the cosine of.
     * @return The cosine of the value.
     */
    public static double cos(double value) {
        return Math.cos(value);
    }

    /**
     * Logs an Object to the standard output.
     *
     * @param msg The message to be logged.
     */
    public static void log(Object msg) {
        System.out.println(msg);
    }

    /**
     * Closes the Java Virtual Machine.
     */
    public static void exit() {
        System.exit(0);
    }

    /**
     * This function returns an object that can throw exceptions.
     *
     * @return The exceptionThrower object.
     */
    public static ExceptionThrower getExceptionThrower() {
        return exceptionThrower;
    }

    /**
     * Sets the ExceptionThrower that will be used to throw exceptions
     *
     * @param exceptionThrower This is the class that will be used to throw exceptions.
     */
    public static void setExceptionThrower(ExceptionThrower exceptionThrower) {
        Yld.exceptionThrower = exceptionThrower;
    }

    /**
     * It loads a class from a string, instantiates it, and sets it as the exception thrower
     *
     * @param classPath The path to the class that implements the ExceptionThrower interface.
     */
    public static void loadExceptionThrower(String classPath) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        setExceptionThrower((ExceptionThrower) Class.forName(classPath).getDeclaredConstructor().newInstance());
    }
}
