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

import com.xebisco.yield.*;

public class Sprite extends Rectangle
{
    private boolean flipped;

    @Override
    public void process(Obj obj)
    {
        super.process(obj);
        if (getMaterial().getTexture() != null)
        {
            getObj().slickImage = getEntity().getMaterial().getTexture().getSlickImage();
            getObj().image = getEntity().getMaterial().getTexture().getImage();
        }
        getObj().color = getMaterial().getColor();
        getObj().drawColor = getMaterial().getColor();
    }

    @Override
    public void previous(YldGraphics graphics)
    {
        if (getEntity().getMaterial().getTexture() != null)
        {
            getObj().value = getEntity().getMaterial().getTexture().getRelativePath();
            getObj().x2 = getEntity().getMaterial().getTexture().getWidth();
            getObj().y2 = getEntity().getMaterial().getTexture().getHeight();
        }
    }

    public Sprite setSizeAsTexture(Texture texture)
    {
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        return this;
    }

    @Override
    public Sprite setSize(float width, float height)
    {
        super.setSize(width, height);
        return this;
    }

    @Override
    public Color getColor()
    {
        return getEntity().getMaterial().getColor();
    }

    public boolean isFlipped()
    {
        return flipped;
    }

    public void setFlipped(boolean flipped)
    {
        this.flipped = flipped;
    }
}
