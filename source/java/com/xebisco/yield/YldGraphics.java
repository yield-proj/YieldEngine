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

package com.xebisco.yield;

import com.xebisco.yield.components.Text;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class YldGraphics {

    public final ArrayList<Obj> shapeRends = new ArrayList<>();
    private Color color = Colors.CYAN;
    private Font font = new Font("arial", Font.BOLD, 20);
    private ArrayList<YldFilter> filters = new ArrayList<>();

    public Obj sample() {
        return rect(0, 0, 0, 0);
    }

    public Obj rect(float x, float y, float width, float height, boolean filled) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.RECT, filled, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj oval(float x, float y, float width, float height, boolean filled) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.OVAL, filled, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj tint(float x, float y) {
        Obj shape = new Obj((int) x, 0, (int) y, 0, Obj.ShapeType.POINT, false, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj line(float x1, float y1, float x2, float y2) {
        Obj shape = new Obj((int) x1, (int) x2, (int) y1, (int) y2, Obj.ShapeType.LINE, false, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    public Obj text(String text, float x, float y) {
        Obj shape = new Obj((int) x, 0, (int) y, 0, Obj.ShapeType.TEXT, false, color, text, font);
        shapeRends.add(shape);
        return shape;
    }

    public Obj text(String text) {
        return text(text, 0, 0);
    }

    public Obj img(Texture texture, float x, float y, float width, float height) {
        BufferedImage image = texture.getImage();
        Obj obj = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.RECT, true, Colors.BLACK, texture.getRelativePath(), null);
        obj.image = image;
        return obj;
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
