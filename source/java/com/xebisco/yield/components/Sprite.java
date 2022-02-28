package com.xebisco.yield.components;

import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;
import com.xebisco.yield.Color;

public class Sprite extends Rectangle {
    @Override
    public void process(Obj obj) {
        super.process(obj);
        if(getEntity().getMaterial().getTexture() != null) {
            obj.image = getEntity().getMaterial().getTexture().getImage();
        }
    }

    @Override
    public void previous(YldGraphics graphics) {
        if(getEntity().getMaterial().getTexture() != null) {
            getObj().x2 = getEntity().getMaterial().getTexture().getImage().getWidth(null);
            getObj().y2 = getEntity().getMaterial().getTexture().getImage().getHeight(null);
        }
    }

    @Override
    public Color getColor() {
        return getEntity().getMaterial().getColor();
    }
}
