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

package com.xebisco.yield.components;

import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;

public class Rectangle extends Shape
{
    private boolean filled = true;
    private float width = 64, height = 64;

    @Override
    public void parameters(YldGraphics graphics)
    {

    }

    @Override
    public void previous(YldGraphics graphics)
    {

    }

    @Override
    public void process(Obj obj)
    {
        obj.x = (int) (getEntity().getTransform().position.x - width / 2 * getTransform().scale.x);
        obj.y = (int) (getEntity().getTransform().position.y - height / 2 * getTransform().scale.y);
        obj.x2 = (int) (getEntity().getTransform().position.x - width / 2 * getTransform().scale.x + getEntity().getTransform().scale.x * width);
        obj.y2 = (int) (getEntity().getTransform().position.y - height / 2 * getTransform().scale.y + getEntity().getTransform().scale.y * height);
        obj.filled = filled;
        obj.type = Obj.ShapeType.RECT;
    }

    public Rectangle setSize(float width, float height)
    {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public boolean isFilled()
    {
        return filled;
    }

    public void setFilled(boolean filled)
    {
        this.filled = filled;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }
}
