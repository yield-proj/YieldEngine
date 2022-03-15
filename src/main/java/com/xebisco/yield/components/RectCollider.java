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

package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

public class RectCollider extends Component
{
    private Vector2 size = new Vector2(64, 64), upLeft = new Vector2(), off = new Vector2(), middle = new Vector2();
    private boolean autoMid = true;
    private int[] layers = new int[] {0};

    @Override
    public void update(float delta)
    {
        if (autoMid)
        {
            middle.x = size.x / 2f;
            middle.y = size.y / 2f;
        }
        upLeft.x = getTransform().position.x - middle.x - off.x;
        upLeft.y = getTransform().position.y - middle.y - off.y;
    }

    public Vector2 getSize()
    {
        return size;
    }

    public void setSize(Vector2 size)
    {
        this.size = size;
    }

    public Vector2 getUpLeft()
    {
        return upLeft;
    }

    public void setUpLeft(Vector2 upLeft)
    {
        this.upLeft = upLeft;
    }

    public Vector2 getOff()
    {
        return off;
    }

    public void setOff(Vector2 off)
    {
        this.off = off;
    }

    public Vector2 getMiddle()
    {
        return middle;
    }

    public void setMiddle(Vector2 middle)
    {
        this.middle = middle;
    }

    public boolean isAutoMid()
    {
        return autoMid;
    }

    public void setAutoMid(boolean autoMid)
    {
        this.autoMid = autoMid;
    }

    public int[] getLayers()
    {
        return layers;
    }

    public void setLayers(int[] layers)
    {
        this.layers = layers;
    }
}
