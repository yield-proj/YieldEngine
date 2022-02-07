package com.xebisco.yield.filters;

import com.xebisco.yield.FilterAdapter;
import com.xebisco.yield.Obj;
import com.xebisco.yield.Yld;

import java.awt.*;

public class ColorFilter extends FilterAdapter {

    private int red, green, blue;

    public ColorFilter(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void process(Obj rend) {
        int r = rend.color.getRed() + red, g = rend.color.getGreen() + green, b = rend.color.getBlue() + blue;
        if(r < 0)
            r = 0;
        if(g < 0)
            g = 0;
        if(b < 0)
            b = 0;
        if(r > 255)
            r = 255;
        if(g > 255)
            g = 255;
        if(b > 255)
            b = 255;
        rend.drawColor = new Color(r, g, b);
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
