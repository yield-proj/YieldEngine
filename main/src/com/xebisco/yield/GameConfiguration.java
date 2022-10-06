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

import com.xebisco.yield.config.WindowConfiguration;
import com.xebisco.yield.render.RenderMaster;

/**
 * This is the standard configuration class used to launch YldGames, all the properties can be modified.
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public class GameConfiguration extends WindowConfiguration {
    /**
     * Frames per second for the game handler.
     */
    public int fps = 60;

    /**
     * Pixels per meter.
     */
    public int ppm = 16;

    /**
     * A flag to load the Yield logo when the game is launched.
     */
    public boolean loadYieldLogo = true;

    /**
     * Fps lock for the game handler.
     */
    public boolean fpsLock = true;

    /**
     * App name for saving files.
     */
    public String appName;
    /**
     * A class path to a render master implementation.
     */
    public String renderMasterName;

    /**
     * A render master implementation.
     */
    public RenderMaster renderMaster;

    /**
     * A flag to unload all textures from memory when changing scene.
     */
    public boolean unloadAllTexturesWhenChangeScene = true;

    /**
     * A flag to unload all textures from memory when changing to a process scene.
     */
    public boolean unloadAllTexturesWhenChangeToProcessScene = true;
    /**
     * A flag to unload all audio players when changing scene.
     */
    public boolean unloadAllAudioPlayersWhenChangeScene = true;

    /**
     * The number of frames before the garbage collector is called.
     */
    public int framesToGarbageCollection = 300;
}
