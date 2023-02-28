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
 * It's a behavior that draws a rectangle
 */
public class Rectangle extends ComponentBehavior {

    private final DrawInstruction drawInstruction = new DrawInstruction();
    private Color color = Colors.LIGHT_BLUE.brighter(), borderColor = Colors.BLACK;
    private double borderThickness;
    private boolean filled = true;

    @Override
    public void onStart() {
        drawInstruction.setType(DrawInstruction.Type.RECTANGLE);
    }

    @Override
    public void onUpdate() {
        drawInstruction.setRotation(getEntity().getTransform().getzRotation());
        drawInstruction.setFilled(filled);
        if(borderThickness != 0)
            drawInstruction.setBorderColor(borderColor);
        else drawInstruction.setBorderColor(null);
        drawInstruction.setInnerColor(color);
        drawInstruction.setPosition(getEntity().getTransform().getPosition());
        drawInstruction.setSize(new Size2D(
                getEntity().getTransform().getSize().getWidth() * getEntity().getTransform().getScale().getX(),
                getEntity().getTransform().getSize().getHeight() * getEntity().getTransform().getScale().getY()
        ));
        drawInstruction.setBorderThickness(borderThickness);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {
        graphics.draw(drawInstruction);
    }

    /**
     * This function returns the draw instruction for this object.
     *
     * @return The drawInstruction variable is being returned.
     */
    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    /**
     * This function returns the color of the object.
     *
     * @return The color of the component.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This function sets the color of the object to the color passed in as a parameter.
     *
     * @param color The color of the component.
     */
    public void setColor(Color color) {
        this.color = color;
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
     * @return The border thickness of the component.
     */
    public double getBorderThickness() {
        return borderThickness;
    }

    /**
     * This function sets the thickness of the border of the component.
     *
     * @param borderThickness The thickness of the border.
     */
    public void setBorderThickness(double borderThickness) {
        this.borderThickness = borderThickness;
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
     * @param filled a boolean value that determines whether the component is filled or not.
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
