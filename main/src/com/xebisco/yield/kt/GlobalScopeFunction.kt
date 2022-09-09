/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.kt

import com.xebisco.yield.*
import org.jbox2d.common.Vec2

/**
 * It launches the game
 *
 * @param game The game class that extends YldGame.
 * @param configuration This is the configuration of the game. It contains the title, width, height, and other settings.
 */
fun launch(game: YldGame, configuration: GameConfiguration) {
    YldGame.launch(game, configuration)
}

/**
 * It launches the game
 *
 * @param game The game class that extends YldGame.
 */
fun launch(game: YldGame) {
    YldGame.launch(game)
}

const val YIELD_VERSION = Yld.VERSION

fun log(o: Any) {
    Yld.log(o)
}

fun message(o: Any) {
    Yld.message(o)
}

/**
 * The memory in use in the actual Java Virtual Machine.
 *
 * @return The memory in use.
 */
fun jvmMemory(): Int {
    return Yld.MEMORY()
}

/**
 * The free memory of the actual Java Virtual Machine.
 *
 * @return The free memory.
 */
fun jvmMaxMemory(): Int {
    return Yld.MAX_MEMORY()
}

/**
 * If there's an exceptionThrower, throw the exception. Otherwise, print the stack trace
 *
 * @param e The exception to throw.
 */
fun throwException(e: Exception) {
    Yld.throwException(e)
}


/**
 * `debug` sets the `debug` property of the `Yld` object to the value passed in
 *
 * @param value The value to be set.
 */
fun debug(value: Boolean) {
    Yld.debug = value
}

/**
 * If Yield Game Engine is in debug mode ot not.
 */
fun debug(): Boolean {
    return Yld.debug
}

/**
 * Executes an instance of a YldAction if the debug variable is set to TRUE.
 *
 * @param action The action to be performed.
 */
fun debug(action: YldAction) {
    Yld.debug(action)
}

/**
 * Executes an instance of a YldAction if the debug variable is set to FALSE.
 *
 * @param action The action to be performed.
 */
fun release(action: YldAction) {
    Yld.release(action)
}

/**
 * Converts a Vector2 to a Box2D Vec2.
 *
 * @param vector2 The vector2 to convert to a Vec2.
 * @return A new Vec2 object with the x and y values of the Vector2 object.
 */
fun toBox2DVec2(vector2: Vector2): Vec2 {
    return Yld.toVec2(vector2)
}

/**
 * Converts a Box2D Vec2 to a Vector2.
 *
 * @param vec2 The vector2 to convert to a Vector2.
 * @return A new Vector2 object.
 */
fun toYieldVector2(vec2: Vec2): Vector2 {
    return Yld.toVector2(vec2)
}

/**
 * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
 *
 * @param value The value to clamp.
 * @param min The minimum value that the returned value can be.
 * @param max The maximum value that the returned value can be.
 * @return The max value of the two values.
 */
fun clamp(value: Int, min: Int, max: Int): Int {
    return Yld.clamp(value, min, max)
}

/**
 * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
 *
 * @param value The value to clamp.
 * @param min The minimum value that the returned value can be.
 * @param max The maximum value that the returned value can be.
 * @return The max value of the two values.
 */
fun clamp(value: Long, min: Long, max: Long): Long {
    return Yld.clamp(value, min, max)
}

/**
 * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
 *
 * @param value The value to clamp.
 * @param min The minimum value that the returned value can be.
 * @param max The maximum value that the returned value can be.
 * @return The max value of the two values.
 */
fun clamp(value: Float, min: Float, max: Float): Float {
    return Yld.clamp(value, min, max)
}

/**
 * If the value is greater than the max, return the max, otherwise return the greatest of the value and the min.
 *
 * @param value The value to clamp.
 * @param min The minimum value that the returned value can be.
 * @param max The maximum value that the returned value can be.
 * @return The max value of the two values.
 */
fun clamp(value: Double, min: Double, max: Double): Double {
    return Yld.clamp(value, min, max)
}

/**
 * If the value is less than zero, return the negative of the value, otherwise return the value.
 *
 * @param value The value to be modified.
 * @return The absolute value of the input.
 */
fun mod(value: Int): Int {
    return Yld.mod(value)
}

/**
 * If the value is less than zero, return the negative of the value, otherwise return the value.
 *
 * @param value The value to be modified.
 * @return The absolute value of the input.
 */
fun mod(value: Long): Long {
    return Yld.mod(value)
}

/**
 * If the value is less than zero, return the negative of the value, otherwise return the value.
 *
 * @param value The value to be modified.
 * @return The absolute value of the input.
 */
fun mod(value: Float): Float {
    return Yld.mod(value)
}

/**
 * If the value is less than zero, return the negative of the value, otherwise return the value.
 *
 * @param value The value to be modified.
 * @return The absolute value of the input.
 */
fun mod(value: Double): Double {
    return Yld.mod(value)
}