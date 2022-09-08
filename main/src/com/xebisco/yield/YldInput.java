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

package com.xebisco.yield;

import com.xebisco.yield.render.MultipleFingerPointers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * It's a class that handles input
 */
public class YldInput {
    private final YldGame game;
    private final Set<Integer> pressing = new HashSet<>();

    private final Vector2 mouse = new Vector2();

    private final Set<Vector3> nonTouchableGamePointers = new HashSet<>();

    private final HashMap<String, YldPair<YldPair<Integer, Integer>, YldPair<Integer, Integer>>> axis = new HashMap<>();

    public YldInput(YldGame game) {
        this.game = game;
        addAxis("Vertical", Key.UP, Key.W, Key.DOWN, Key.S);
        addAxis("Horizontal", Key.LEFT, Key.A, Key.RIGHT, Key.D);
        addAxis("Jump", null, Key.SPACE);
        addAxis("Fire1", null, Key.MOUSE_1);
        addAxis("Fire2", null, Key.SHIFT);
    }

    public YldInput(YldGame game, YldInput input) {
        this.game = game;
        if (input != null)
            input.getAxis().forEach((s, a) -> getAxis().put(s, a));
        addAxis("Vertical", Key.UP, Key.W, Key.DOWN, Key.S);
        addAxis("Horizontal", Key.LEFT, Key.A, Key.RIGHT, Key.D);
        addAxis("Jump", null, Key.SPACE);
        addAxis("Fire1", null, Key.MOUSE_1);
        addAxis("Fire2", null, Key.SHIFT);
    }

    /**
     * If the key is null, return false. Otherwise, return whether the key is being pressed.
     *
     * @param key The key you want to check if it's pressed.
     * @return A boolean value.
     */
    public boolean isPressed(Integer key) {
        if (key == null)
            return false;
        return game.getHandler().getRenderMaster().pressing().contains(key);
    }

    /**
     * If the key is being pressed, and it wasn't pressed last frame, then return true
     *
     * @param key The key you want to check.
     * @return A boolean value.
     */
    public boolean isJustPressed(int key) {
        boolean pressing = game.getHandler().getRenderMaster().pressing().contains(key);
        if (pressing) {
            if (!this.pressing.contains(key)) {
                this.pressing.add(key);
                return true;
            }
        } else {
            this.pressing.remove(key);
        }
        return false;
    }

    /**
     * Is the pointer touching the spacial?
     * The function isTouching() takes a Spacial as a parameter. It then returns a boolean value. The boolean value is true
     * if the pointer is touching the spacial, and false if it is not.
     *
     * @param spacial The spacial you want to check if the pointer is touching.
     * @return A boolean value.
     */
    public boolean isTouching(Spacial spacial) {
        return spacial.colliding(getMouse().x + game.getScene().getView().getTransform().position.x + game.getScene().getView().getPosition().x, getMouse().y + game.getScene().getView().getTransform().position.y + game.getScene().getView().getPosition().y);
    }

    /**
     * Get the mouse position and return it as a Vector2.
     *
     * @return The mouse position.
     */
    public Vector2 getMouse() {
        mouse.x = game.getHandler().getRenderMaster().mouseX();
        mouse.y = game.getHandler().getRenderMaster().mouseY();
        return mouse;
    }

    /**
     * Returns a set of integers representing the keys that are currently being pressed.
     *
     * @return A set of integers.
     */
    public Set<Integer> getPressing() {
        return pressing;
    }

    /**
     * If the primary key is pressed, return -1. If the secondary key is pressed, return 1. If neither are pressed, return
     * 0
     *
     * @param axisName The name of the axis you want to get the value of.
     * @return A float value.
     */
    public float getAxis(String axisName) {
        float value = 0;
        YldPair<YldPair<Integer, Integer>, YldPair<Integer, Integer>> axisPair = axis.get(axisName);
        Integer primary = axisPair.getFirst().getFirst(), altPrimary = axisPair.getFirst().getSecond(), secondary = axisPair.getSecond().getFirst(), altSecondary = axisPair.getSecond().getSecond();
        if (isPressed(primary) || isPressed(altPrimary)) {
            value = -1;
        } else if (isPressed(secondary) || isPressed(altSecondary)) {
            value = 1;
        }
        return value;
    }

    /**
     * Add an axis to the list of axes.
     * The first parameter is the name of the axis. The next four parameters are the primary and secondary keys for the
     * axis.
     *
     * @param name            The name of the axis.
     * @param primaryKey      The key that is used to move the axis in the negative direction.
     * @param altPrimaryKey   The key that is used when the primary key is not pressed.
     * @param secondaryKey    The key that is used to move the axis in the positive direction.
     * @param altSecondaryKey The key that is used when the secondary key is not pressed.
     */
    public void addAxis(String name, Integer primaryKey, Integer altPrimaryKey, Integer secondaryKey, Integer altSecondaryKey) {
        axis.put(name, new YldPair<>(new YldPair<>(primaryKey, altPrimaryKey), new YldPair<>(secondaryKey, altSecondaryKey)));
    }

    /**
     * Add an axis to the list of axes.
     * The first parameter is the name of the axis. The next two parameters are the primary and secondary keys for the
     * axis.
     *
     * @param name         The name of the axis.
     * @param primaryKey   The key that is used to move the axis in the negative direction.
     * @param secondaryKey The key that is used to move the axis in the positive direction.
     */
    public void addAxis(String name, Integer primaryKey, Integer secondaryKey) {
        addAxis(name, primaryKey, null, secondaryKey, null);
    }

    /**
     * Getter for the axes list.
     */
    public HashMap<String, YldPair<YldPair<Integer, Integer>, YldPair<Integer, Integer>>> getAxis() {
        return axis;
    }

    /**
     * This function returns the game object.
     *
     * @return The game object.
     */
    public YldGame getGame() {
        return game;
    }

    /**
     * If the game is touchable, return the touch points, otherwise return the mouse position
     *
     * @return A set of Vector3 objects.
     */
    public Set<Vector3> getPointers() {
        if (game.getHandler().getRenderMaster() instanceof MultipleFingerPointers)
            return ((MultipleFingerPointers) game.getHandler().getRenderMaster()).fingerPointers();
        else {
            nonTouchableGamePointers.clear();
            nonTouchableGamePointers.add(new Vector3(getMouse()));
            return nonTouchableGamePointers;
        }
    }
}
