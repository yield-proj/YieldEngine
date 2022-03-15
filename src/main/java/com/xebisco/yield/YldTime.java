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

import com.xebisco.yield.engine.GameHandler;

public class YldTime
{
    private float delta, fps;
    private YldGame game;

    public YldTime(YldGame game)
    {
        this.game = game;
    }

    public float getDelta()
    {
        return delta;
    }

    public float getFps()
    {
        return fps;
    }

    public float getTargetFPS()
    {
        return game.getConfiguration().fps;
    }

    public void setTargetFPS(float fps)
    {
        if (game.getSlickApp() != null)
            game.getSlickApp().setTargetFrameRate((int) fps);
        else
            game.getHandler().setFps((int) fps);
    }

    public YldGame getGame()
    {
        return game;
    }

    public void setGame(YldGame game)
    {
        this.game = game;
    }

    public void setDelta(float delta)
    {
        this.delta = delta;
    }

    public void setFps(float fps)
    {
        this.fps = fps;
    }
}
