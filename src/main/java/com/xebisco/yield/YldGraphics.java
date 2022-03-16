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

    /**
     * The list of the actual graphic objects on this YldGraphics instance.
     */
    public final ArrayList<Obj> shapeRends = new ArrayList<>();
    private Color color = Colors.CYAN;
    private Font font = new Font("arial", Font.BOLD, 20);
    private ArrayList<YldFilter> filters = new ArrayList<>();

    /**
     * Returns an empty rectangle.
     * @return The created rectangle.
     */
    public Obj sample() {
        return rect(0, 0, 0, 0);
    }

    /**
     * Creates and adds a rectangle to the shapeRends list.
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param filled If the rectangle is filled.
     * @return The created rectangle.
     */
    public Obj rect(float x, float y, float width, float height, boolean filled) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.RECT, filled, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    /**
     * Creates and adds an oval to the shapeRends list.
     * @param x The x position of the oval
     * @param y The y position of the oval
     * @param width The width of the oval
     * @param height The height of the oval
     * @param filled If the oval is filled.
     * @return The created oval.
     */
    public Obj oval(float x, float y, float width, float height, boolean filled) {
        Obj shape = new Obj((int) x, (int) width, (int) y, (int) height, Obj.ShapeType.OVAL, filled, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    /**
     * Creates and adds a pixel like graphical object to the shapeRends list.
     * @param x The x position of the pixel.
     * @param y The y position of the pixel.
     * @return The created pixel.
     */
    public Obj tint(float x, float y) {
        Obj shape = new Obj((int) x, 0, (int) y, 0, Obj.ShapeType.POINT, false, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    /**
     * Create and adds a line graphical object to the shapeRends list.
     * @param x1 The x1 position of the line.
     * @param y1 The y1 position of the line.
     * @param x2 The x2 position of the line.
     * @param y2 The y2 position of the line.
     * @return The created line.
     */
    public Obj line(float x1, float y1, float x2, float y2) {
        Obj shape = new Obj((int) x1, (int) x2, (int) y1, (int) y2, Obj.ShapeType.LINE, false, color, null, null);
        shapeRends.add(shape);
        return shape;
    }

    /**
     * Creates and adds a text graphical object to the shapeRends list.
     * @param text The contents of the text.
     * @param x The x position of the text.
     * @param y The y position of the text.
     * @return The created text.
     */
    public Obj text(String text, float x, float y) {
        Obj shape = new Obj((int) x, 0, (int) y, 0, Obj.ShapeType.TEXT, false, color, text, font);
        shapeRends.add(shape);
        return shape;
    }

    public Obj text(String text) {
        return text(text, 0, 0);
    }

    public Obj img(Texture texture, float x, float y, float width, float height) {
        Image image = texture.getImage();
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
