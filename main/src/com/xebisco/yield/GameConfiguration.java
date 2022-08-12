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
 * @since 4_alpha1
 * @author Xebisco
 */
public class GameConfiguration extends WindowConfiguration
{
    public int fps = 60, ppm = 16;
    public boolean fpsLock = true;
    public String appName, renderMasterName;

    public RenderMaster renderMaster;
    public int framesToGarbageCollection = 300;
}
