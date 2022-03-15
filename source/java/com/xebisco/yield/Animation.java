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

public class Animation
{

    private final Texture[] frames;
    private final String name;
    private int actFrame = 0, frameDelay = 0, microsecondDelay = 1667;

    public Animation(String name, Texture... frames)
    {
        this.frames = frames;
        this.name = name;
    }

    public Animation(Texture... frames)
    {
        this.frames = frames;
        this.name = "default";
    }

    public Texture[] getFrames()
    {
        return frames;
    }

    public int getActFrame()
    {
        return actFrame;
    }

    public void setActFrame(int actFrame)
    {
        this.actFrame = actFrame;
    }

    public int getFrameDelay()
    {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay)
    {
        this.frameDelay = frameDelay;
    }

    public String getName()
    {
        return name;
    }

    public int getMicrosecondDelay()
    {
        return microsecondDelay;
    }

    public void setMicrosecondDelay(int microsecondDelay)
    {
        this.microsecondDelay = microsecondDelay;
    }
}
