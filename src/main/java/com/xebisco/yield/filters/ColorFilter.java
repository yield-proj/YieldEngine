package com.xebisco.yield.filters;

import com.xebisco.yield.FilterAdapter;
import com.xebisco.yield.Obj;
import com.xebisco.yield.Color;

public class ColorFilter extends FilterAdapter {

    private float red, green, blue;

    public ColorFilter(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void process(Obj rend) {
        float r = rend.color.getR() + red, g = rend.color.getG() + green, b = rend.color.getB() + blue;
        if(r < 0)
            r = 0;
        if(g < 0)
            g = 0;
        if(b < 0)
            b = 0;
        if(r > 1)
            r = 1;
        if(g > 1)
            g = 1;
        if(b > 1)
            b = 1;
        rend.drawColor = new Color(r, g, b);
    }

    public float getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
