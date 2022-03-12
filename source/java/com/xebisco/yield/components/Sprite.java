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
