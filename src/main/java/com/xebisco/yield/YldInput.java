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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class YldInput {
    private final YldGame game;
    private final Set<Integer> pressing = new HashSet<>();

    private final HashMap<String, YldPair<YldPair<Integer, Integer>, YldPair<Integer, Integer>>> axis = new HashMap<>();

    public YldInput(YldGame game) {
        this.game = game;
        addAxis("Vertical", Key.UP, Key.W, Key.DOWN, Key.S);
        addAxis("Horizontal", Key.LEFT, Key.A, Key.RIGHT, Key.D);
        addAxis("Jump", null, Key.SPACE);
        addAxis("Fire1", null, Key.MOUSE_1);
        addAxis("Fire2", null, Key.SHIFT);
    }

    public boolean isPressed(Integer key) {
        if (key == null)
            return false;
        return game.getHandler().getRenderMaster().pressing().contains(key);
    }

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

    public boolean isTouching(Spacial spacial) {
        return spacial.colliding(getMouse().x, getMouse().y);
    }

    public Vector2 getMouse() {
        return new Vector2(game.getHandler().getRenderMaster().mouseX(), game.getHandler().getRenderMaster().mouseY());
    }

    public Set<Integer> getPressing() {
        return pressing;
    }

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

    public void addAxis(String name, Integer primaryKey, Integer altPrimaryKey, Integer secondaryKey, Integer altSecondaryKey) {
        axis.put(name, new YldPair<>(new YldPair<>(primaryKey, altPrimaryKey), new YldPair<>(secondaryKey, altSecondaryKey)));
    }

    public void addAxis(String name, Integer primaryKey, Integer secondaryKey) {
        addAxis(name, primaryKey, null, secondaryKey, null);
    }

    public HashMap<String, YldPair<YldPair<Integer, Integer>, YldPair<Integer, Integer>>> getAxis() {
        return axis;
    }

    public YldGame getGame() {
        return game;
    }
}
