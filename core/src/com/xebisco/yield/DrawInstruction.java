/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

public class DrawInstruction {
    public enum Type {
        RECTANGLE, OVAL, TEXT, SIMPLE_LINE, IMAGE
    }
    private Type type;
    private Point2D position;
    private Size2D size;
    private Object renderRef;
    private boolean filled;
    private Color innerColor, borderColor;
    private double borderThickness, rotation;
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Size2D getSize() {
        return size;
    }

    public void setSize(Size2D size) {
        this.size = size;
    }

    public Object getRenderRef() {
        return renderRef;
    }

    public void setRenderRef(Object renderRef) {
        this.renderRef = renderRef;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public Color getInnerColor() {
        return innerColor;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public double getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(double borderThickness) {
        this.borderThickness = borderThickness;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
