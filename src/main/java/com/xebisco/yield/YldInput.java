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

import java.util.HashSet;
import java.util.Set;

public class YldInput {
    private final YldGame game;
    private final Set<Integer> pressing = new HashSet<>();

    public YldInput(YldGame game) {
        this.game = game;
    }

    public boolean isPressed(int key) {
        return game.getHandler().getRenderMaster().pressing().contains(key);
    }

    public boolean isJustPressed(int key) {
        boolean pressing = game.getHandler().getRenderMaster().pressing().contains(key);
        if(pressing) {
            if(!this.pressing.contains(key)) {
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

    public YldGame getGame() {
        return game;
    }
}
