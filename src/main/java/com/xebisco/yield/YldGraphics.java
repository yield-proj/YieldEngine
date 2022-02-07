package com.xebisco.yield;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class YldGraphics {

    public final ArrayList<Obj> shapeRends = new ArrayList<>();
    private Color color = Color.CYAN;
    private Font font = new Font("arial", Font.BOLD, 20);
    private ArrayList<YldFilter> filters = new ArrayList<>();

    public Obj rect(float x, float y, float width, float height, boolean filled) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.RECT, filled, color, null, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj oval(float x, float y, float width, float height, boolean filled) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.OVAL, filled, color, null, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj tint(float x, float y) {
        Obj shape = new Obj((int) x, 0, (int) y, 0, Obj.ShapeType.POINT, false, color, null, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj line(float x1, float y1, float x2, float y2) {
        Obj shape = new Obj((int) x1, (int) x2, (int) y1, (int) y2, Obj.ShapeType.LINE, false, color, null, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj text(String text, float x, float y) {
        Obj shape = new Obj((int) x, 0, (int) y, 0, Obj.ShapeType.TEXT, false, color, text, font, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj text(String text) {
        return text(text, 0, 0);
    }

    public Obj img(Image image, float x, float y, float width, float height) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.RECT, false, color, null, null, image);
        shapeRends.add(shape);
        return shape;
    }

    public Obj img(String internalPath, float x, float y, float width, float height) {
        Image image = new ImageIcon(Objects.requireNonNull(YldGraphics.class.getResource(internalPath))).getImage();
        return img(image, x, y, width, height);
    }

    public Obj img(String internalPath, float x, float y) {
        Image image = new ImageIcon(Objects.requireNonNull(YldGraphics.class.getResource(internalPath))).getImage();
        return img(image, x, y, image.getWidth(null), image.getHeight(null));
    }

    public Obj rect(float x, float y, float width, float height) {
        return rect(x, y, width, height, true);
    }

    public Obj oval(float x, float y, float width, float height) {
        return oval(x, y, width, height, true);
    }

    public void addFilter(YldFilter filter) {
        filters.add(filter);
    }

    public ArrayList<YldFilter> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<YldFilter> filters) {
        this.filters = filters;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
