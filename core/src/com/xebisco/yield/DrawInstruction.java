/*
 * Copyright [2022-2023] [Xebisco]
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

import java.util.ArrayList;
import java.util.List;

/**
 * It's a data structure that holds all the information needed to draw a rectangle, oval, text, line, or image
 */
public class DrawInstruction {
    private int[] verticesX;
    private int[] verticesY;
    private Object imageRef, fontRef;
    private String text;
    private double stroke;
    private Color color;
    private double rotation;

    private double x, y, scaleX, scaleY;

    private List<DrawInstruction> childrenInstructions = new ArrayList<>();

    public DrawInstruction() {
    }

    public DrawInstruction(int[] verticesX, int[] verticesY) {
        this.verticesX = verticesX;
        this.verticesY = verticesY;
    }

    public int[] getVerticesX() {
        return verticesX;
    }

    public void setVerticesX(int[] verticesX) {
        this.verticesX = verticesX;
    }

    public int[] getVerticesY() {
        return verticesY;
    }

    public void setVerticesY(int[] verticesY) {
        this.verticesY = verticesY;
    }

    public Object getImageRef() {
        return imageRef;
    }

    public void setImageRef(Object imageRef) {
        this.imageRef = imageRef;
    }

    public Object getFontRef() {
        return fontRef;
    }

    public void setFontRef(Object fontRef) {
        this.fontRef = fontRef;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getStroke() {
        return stroke;
    }

    public void setStroke(double stroke) {
        this.stroke = stroke;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public List<DrawInstruction> getChildrenInstructions() {
        return childrenInstructions;
    }

    public void setChildrenInstructions(List<DrawInstruction> childrenInstructions) {
        this.childrenInstructions = childrenInstructions;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }
}
