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

import com.xebisco.yield.graphics.SampleGraphics;

/**
 * A YldExtension object have the purpose to add features, or 'extends' the Yield Game Engine functionalities.
 * @since 4_alpha1
 * @author Xebisco
 */
public class YldExtension extends YldB
{
    /**
     * Called when the extension is created.
     */
    @Override
    public void create()
    {

    }

    /**
     * Called on every frame of the application, before the YldGame.
     *
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    @Override
    public void update(float delta)
    {

    }

    /**
     * Called when the extension is destroyed.
     */
    @Override
    public final void onDestroy()
    {

    }

    /**
     * Called on every frame of the application, to render custom graphics on the screen.
     * @param graphics Is a implementation of AWTGraphics(CPU Mode) or SlickGraphics(GPU Mode) with the same API to draw custom graphics onto the screen.
     */
    public void render(SampleGraphics graphics)
    {

    }
}
