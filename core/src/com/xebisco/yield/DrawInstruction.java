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
public class DrawInstruction implements Cloneable {
    private int[] verticesX, verticesY;
    private Object imageRef, fontRef;
    private String text;
    private Color color;
    //Only work on top-level entities
    private boolean ignoreCameraPosition, ignoreViewportScale, rotateBeforeScale = true;

    private double x, y, scaleX = 1, scaleY = 1, centerOffsetX, centerOffsetY, stroke, rotation;

    private List<DrawInstruction> childrenInstructions = new ArrayList<>();

    public DrawInstruction() {
    }

    public DrawInstruction(int[] verticesX, int[] verticesY) {
        this.verticesX = verticesX;
        this.verticesY = verticesY;
    }

    public int[] verticesX() {
        return verticesX;
    }

    public DrawInstruction setVerticesX(int[] verticesX) {
        this.verticesX = verticesX;
        return this;
    }

    public int[] verticesY() {
        return verticesY;
    }

    public DrawInstruction setVerticesY(int[] verticesY) {
        this.verticesY = verticesY;
        return this;
    }

    public Object imageRef() {
        return imageRef;
    }

    public DrawInstruction setImageRef(Object imageRef) {
        this.imageRef = imageRef;
        return this;
    }

    public Object fontRef() {
        return fontRef;
    }

    public DrawInstruction setFontRef(Object fontRef) {
        this.fontRef = fontRef;
        return this;
    }

    public String text() {
        return text;
    }

    public DrawInstruction setText(String text) {
        this.text = text;
        return this;
    }

    public double stroke() {
        return stroke;
    }

    public DrawInstruction setStroke(double stroke) {
        this.stroke = stroke;
        return this;
    }

    public Color color() {
        return color;
    }

    public DrawInstruction setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean ignoreCameraPosition() {
        return ignoreCameraPosition;
    }

    public DrawInstruction setIgnoreCameraPosition(boolean ignoreCameraPosition) {
        this.ignoreCameraPosition = ignoreCameraPosition;
        return this;
    }

    public boolean ignoreViewportScale() {
        return ignoreViewportScale;
    }

    public DrawInstruction setIgnoreViewportScale(boolean ignoreViewportScale) {
        this.ignoreViewportScale = ignoreViewportScale;
        return this;
    }

    public double rotation() {
        return rotation;
    }

    public DrawInstruction setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public boolean rotateBeforeScale() {
        return rotateBeforeScale;
    }

    public DrawInstruction setRotateBeforeScale(boolean rotateBeforeScale) {
        this.rotateBeforeScale = rotateBeforeScale;
        return this;
    }

    public double x() {
        return x;
    }

    public DrawInstruction setX(double x) {
        this.x = x;
        return this;
    }

    public double y() {
        return y;
    }

    public DrawInstruction setY(double y) {
        this.y = y;
        return this;
    }

    public double scaleX() {
        return scaleX;
    }

    public DrawInstruction setScaleX(double scaleX) {
        this.scaleX = scaleX;
        return this;
    }

    public double scaleY() {
        return scaleY;
    }

    public DrawInstruction setScaleY(double scaleY) {
        this.scaleY = scaleY;
        return this;
    }

    public double centerOffsetX() {
        return centerOffsetX;
    }

    public double centerOffsetY() {
        return centerOffsetY;
    }

    public List<DrawInstruction> childrenInstructions() {
        return childrenInstructions;
    }

    public DrawInstruction setChildrenInstructions(List<DrawInstruction> childrenInstructions) {
        this.childrenInstructions = childrenInstructions;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DrawInstruction clone() {
        DrawInstruction clone = new DrawInstruction();
        if (verticesX() != null) {
            clone.setVerticesX(new int[verticesX().length]);
            System.arraycopy(verticesX(), 0, clone.verticesX(), 0, verticesX().length);
        }
        if (verticesY() != null) {
            clone.setVerticesY(new int[verticesY().length]);
            System.arraycopy(verticesY(), 0, clone.verticesY(), 0, verticesY().length);
        }
        clone.setX(x());
        clone.setY(y());
        clone.setText(text());
        clone.setIgnoreViewportScale(ignoreViewportScale());
        clone.setIgnoreCameraPosition(ignoreCameraPosition());
        clone.setRotation(rotation());
        clone.setScaleX(scaleX());
        clone.setScaleY(scaleY());
        clone.setFontRef(fontRef());
        clone.setImageRef(imageRef());
        clone.setCenterOffsetX(getCenterOffsetX());
        clone.setCenterOffsetY(getCenterOffsetY());
        if (color() != null)
            clone.setColor(color().clone());
        clone.setStroke(stroke());
        clone.setRotateBeforeScale(rotateBeforeScale());
        childrenInstructions().forEach(di -> clone.childrenInstructions().add(di.clone()));
        return clone;
    }

    public double getCenterOffsetX() {
        return centerOffsetX;
    }

    public void setCenterOffsetX(double centerOffsetX) {
        this.centerOffsetX = centerOffsetX;
    }

    public double getCenterOffsetY() {
        return centerOffsetY;
    }

    public void setCenterOffsetY(double centerOffsetY) {
        this.centerOffsetY = centerOffsetY;
    }
}
