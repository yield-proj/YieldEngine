package com.xebisco.yield.components;

import com.xebisco.yield.Obj;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGraphics;
import com.xebisco.yield.Color;
import com.xebisco.yield.slick.SlickTexture;

public class Sprite extends Rectangle
{
    @Override
    public void process(Obj obj)
    {
        super.process(obj);
        if (getEntity().getMaterial().getTexture() instanceof SlickTexture)
        {
            SlickTexture texture = (SlickTexture) getEntity().getMaterial().getTexture();
            if (game.getSlickGame() != null)
            {
                if (texture.getSlickImage() == null || getObj().slickImage != texture.getSlickImage())
                {
                    game.getSlickGame().toLoadTexture = texture;
                }
                getObj().slickImage = texture.getSlickImage();
            }
            else
            {
                getObj().image = getEntity().getMaterial().getTexture().getImage();
            }
        }
        else
        {
            getObj().image = getEntity().getMaterial().getTexture().getImage();
        }
    }

    @Override
    public void previous(YldGraphics graphics)
    {
        if (getEntity().getMaterial().getTexture() != null)
        {
            getObj().value = getEntity().getMaterial().getTexture().getRelativePath();
            getObj().x2 = getEntity().getMaterial().getTexture().getImage().getWidth(null);
            getObj().y2 = getEntity().getMaterial().getTexture().getImage().getHeight(null);
        }
    }

    @Override
    public Color getColor()
    {
        return getEntity().getMaterial().getColor();
    }
}
