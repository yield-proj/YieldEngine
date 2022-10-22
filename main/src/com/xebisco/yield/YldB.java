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

import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.engine.YldEngineAction;

/**
 * Sample methods for the majority of the Yield Game Engine classes.
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public abstract class YldB {

    protected YldGame game;

    /**
     * When the object is created.
     */
    public abstract void create();

    /**
     * This function is called once per frame.
     *
     * @param delta The time in seconds since the last frame.
     */
    public abstract void update(float delta);

    /**
     * This function is called when this object is destroyed.
     */
    public abstract void onDestroy();

    /**
     * It returns the file path of the file relative to the game's root directory
     *
     * @param relativeFile The relative file to be converted to an absolute file.
     * @return A relative file.
     */
    public final RelativeFile relativeFile(RelativeFile relativeFile) {
        return game.getHandler().getRenderMaster().relativeFile(relativeFile);
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
     * This function sets the game variable to the game variable passed in.
     *
     * @param game The game object value to set.
     */
    public void setGame(YldGame game) {
        this.game = game;
    }
}
