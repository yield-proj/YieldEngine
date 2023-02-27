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

public class Rectangle extends ComponentBehavior {

    private final DrawInstruction drawInstruction = new DrawInstruction();
    private Color color = Colors.LIGHT_BLUE.brighter(), borderColor = Colors.BLACK;
    private double borderThickness, rotation;
    private boolean filled = true;

    @Override
    public void onStart() {
        drawInstruction.setType(DrawInstruction.Type.RECTANGLE);
    }

    @Override
    public void onUpdate() {
        drawInstruction.setRotation(rotation);
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

    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
