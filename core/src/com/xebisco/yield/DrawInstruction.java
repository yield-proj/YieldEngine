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

/**
 * It's a data structure that holds all the information needed to draw a rectangle, oval, text, line, or image
 */
public class DrawInstruction {
    public enum Type {
        RECTANGLE, OVAL, TEXT, SIMPLE_LINE, IMAGE
    }
    private Type type;
    private Point2D position;
    private Size2D size;
    private Object renderRef;
    private Font font;
    private boolean filled;
    private Color innerColor, borderColor;
    private double borderThickness, rotation;
    /**
     * Returns the type of this instruction.
     *
     * @return The type of the variable.
     */
    public Type getType() {
        return type;
    }

    /**
     * This function sets the type of the instruction to the type passed in as a parameter.
     *
     * @param type The type of the parameter.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns the position of the instruction.
     *
     * @return The position of the draw instruction.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * This function sets the position of the instruction to the given position.
     *
     * @param position The position of the instruction.
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Returns the size of the instruction.
     *
     * @return The size of the draw instruction.
     */
    public Size2D getSize() {
        return size;
    }

    /**
     * This function sets the size of the instruction.
     *
     * @param size The size of the draw instruction.
     */
    public void setSize(Size2D size) {
        this.size = size;
    }

    /**
     * Returns the renderRef object.
     *
     * @return The renderRef object.
     */
    public Object getRenderRef() {
        return renderRef;
    }

    /**
     * This function sets the renderRef variable to the value of the renderRef parameter.
     *
     * @param renderRef This is the reference to the object that will be rendered.
     */
    public void setRenderRef(Object renderRef) {
        this.renderRef = renderRef;
    }

    /**
     * This function returns the value of the filled variable.
     *
     * @return The boolean value of the filled variable.
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * This function sets the value of the filled variable to the value of the filled parameter.
     *
     * @param filled a boolean value that determines whether the shape of this instruction will be filled or not.
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    /**
     * This function returns the innerColor variable.
     *
     * @return The innerColor variable is being returned.
     */
    public Color getInnerColor() {
        return innerColor;
    }

    /**
     * This function sets the innerColor variable to the value of the innerColor parameter.
     *
     * @param innerColor The color of the inner circle.
     */
    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

    /**
     * This function returns the border color of the object.
     *
     * @return The borderColor variable.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * This function sets the border color of the object to the color passed in as a parameter.
     *
     * @param borderColor The color of the border.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * This function returns the thickness of the border
     *
     * @return The border thickness of the shape.
     */
    public double getBorderThickness() {
        return borderThickness;
    }

    /**
     * This function sets the thickness of the border of the shape.
     *
     * @param borderThickness The thickness of the border.
     */
    public void setBorderThickness(double borderThickness) {
        this.borderThickness = borderThickness;
    }

    /**
     * This function returns the rotation of the instruction.
     *
     * @return The rotation of the instruction.
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * This function sets the rotation of the instruction to the value of the parameter.
     *
     * @param rotation The rotation of the instruction in degrees.
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * This function returns the font of the instruction.
     *
     * @return The font of the instruction.
     */
    public Font getFont() {
        return font;
    }

    /**
     * This function sets the font of the instruction to the value of the parameter.
     *
     * @param font The font of the instruction.
     */
    public void setFont(Font font) {
        this.font = font;
    }
}
