package com.xebisco.yield;

import java.awt.*;

public class Obj {
    public int x, x2, y, y2, index, rotationV, rotationX, rotationY;
    public ShapeType type;
    public boolean filled, center, active = true;
    public Color color, drawColor;
    public String value;
    public Font font;
    public Image image;

    public Obj(int x, int x2, int y, int y2, ShapeType type, boolean filled, Color color, String value, Font font, Image image) {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
        this.type = type;
        this.filled = filled;
        this.color = color;
        this.value = value;
        this.font = font;
        this.image = image;
        drawColor = color;
    }

    public void center() {
        if(View.getActView() != null) {
            x = View.getActView().getWidth() / 2 - x2 / 2;
            y = View.getActView().getHeight() / 2 - y2 / 2;
        }
    }

    public enum ShapeType {
        OVAL, RECT, LINE, POINT, TEXT
    }
}
