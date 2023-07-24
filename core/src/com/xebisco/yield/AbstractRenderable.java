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
import java.util.stream.IntStream;

public abstract class AbstractRenderable extends ComponentBehavior {
    private final List<VertexShader> vertexShaders = new ArrayList<>();
    public abstract int verticesCount();

    public abstract void setup(Vector2D[] vertices);

    private final DrawInstruction drawInstruction = new DrawInstruction(new int[verticesCount()], new int[verticesCount()]);

    private final Vector2D[] vertices = new Vector2D[verticesCount()];

    public AbstractRenderable() {
        for(int i = 0; i < vertices.length; i++)
            vertices[i] = new Vector2D();
    }

    @VisibleOnEditor
    private Size2D size = new Size2D(100, 100);
    @VisibleOnEditor
    private Color color = new Color(Colors.WHITE);
    @VisibleOnEditor
    private double borderThickness;
    @VisibleOnEditor
    private boolean filled = true, absoluteScaled, ignoreOffsetScaling;

    @VisibleOnEditor
    private Vector2D offset = new Vector2D();

    @VisibleOnEditor
    private RectangleAnchor anchor = RectangleAnchor.CENTER;

    private final Vector2D anchorSum = new Vector2D();

    @Override
    public DrawInstruction render() {
        if (filled)
            drawInstruction.setStroke(0);
        else drawInstruction.setStroke(borderThickness);
        drawInstruction.setColor(color);

        anchorSum.reset();
        switch (anchor) {
            case UP:
                anchorSum.setY(-getSize().getHeight() / 2.);
                break;
            case DOWN:
                anchorSum.setY(getSize().getHeight() / 2.);
                break;
            case RIGHT:
                anchorSum.setX(-getSize().getWidth() / 2.);
                break;
            case LEFT:
                anchorSum.setX(getSize().getWidth() / 2.);
                break;
        }
        setup(vertices);
        IntStream.range(0, vertices.length).forEach(i -> {
            Vector2D v = vertices[i];
            double x = v.getX(), y = v.getY();
            for(VertexShader shader : vertexShaders) {
                shader.run(x, y, i);
                x = shader.getPosition().getX();
                y = shader.getPosition().getY();
            }
            drawInstruction.getVerticesX()[i] = (int) x;
            drawInstruction.getVerticesY()[i] = (int) y;
        });
        for (int i = 0; i < drawInstruction.getVerticesX().length; i++) {
            drawInstruction.getVerticesX()[i] += anchorSum.getX();
            drawInstruction.getVerticesY()[i] += anchorSum.getY();
        }
        double ox = offset.getX(), oy = offset.getY();
        if(!ignoreOffsetScaling) {
            ox *= getTransform().getScale().getX();
            oy += getTransform().getScale().getY();
        }
        drawInstruction.setX(ox);
        drawInstruction.setY(oy);
        return drawInstruction;
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

    /**
     * Returns the size of the object.
     *
     * @return The size of the object.
     */
    public Size2D getSize() {
        return size;
    }

    /**
     * This function sets the size of an object using a Size2D parameter.
     *
     * @param size The size value to set.
     */
    public void setSize(Size2D size) {
        this.size = size;
    }

    /**
     * This function returns the start drawing anchor of a rectangle.
     * If its ´DOWN´, The rectangle will have its center on the top point.
     * If its ´UP´, The rectangle will have its center on the bottom point.
     * If its ´CENTER´, The rectangle will have its center on the center point.
     * If its ´LEFT´, The rectangle will have its center on the right point.
     * If its ´DOWN´, The rectangle will have its center on the left point.
     *
     * @return The method is returning an object of type `RectangleAnchor`.
     */
    public RectangleAnchor getAnchor() {
        return anchor;
    }

    /**
     * This function sets the anchor of a rectangle.
     *
     * @param anchor The parameter "anchor" is of type RectangleAnchor, which is an enumerated type that represents the
     *               position of an anchor point relative to a rectangle.
     *               If its ´DOWN´, The rectangle will have its center on the top point.
     *               If its ´UP´, The rectangle will have its center on the bottom point.
     *               If its ´CENTER´, The rectangle will have its center on the center point.
     *               If its ´LEFT´, The rectangle will have its center on the right point.
     *               If its ´DOWN´, The rectangle will have its center on the left point.
     */
    public void setAnchor(RectangleAnchor anchor) {
        this.anchor = anchor;
    }

    /**
     * The function returns a Vector2D object representing an offset.
     *
     * @return A Vector2D object named "offset" is being returned.
     */
    public Vector2D getOffset() {
        return offset;
    }

    /**
     * This function sets the offset of a Vector2D object.
     *
     * @param offset The "offset" parameter is a Vector2D object that represents the amount of displacement or shift in
     *               position from a reference point. This method sets the value of the "offset" instance variable to the value of the
     *               "offset" parameter passed to it.
     */
    public void setOffset(Vector2D offset) {
        this.offset = offset;
    }

    /**
     * This function returns a boolean value indicating whether the scaling is absolute or not.
     *
     * @return The method `isAbsoluteScaled()` is returning a boolean value, which indicates whether the scaling used is
     * absolute or not. The value returned is stored in the variable `absoluteScaled`.
     */
    public boolean isAbsoluteScaled() {
        return absoluteScaled;
    }

    /**
     * This function sets the value of a boolean variable called "absoluteScaled".
     *
     * @param absoluteScaled absoluteScaled is a boolean parameter that is used to set whether the scaling of an object is
     *                       absolute.
     */
    public void setAbsoluteScaled(boolean absoluteScaled) {
        this.absoluteScaled = absoluteScaled;
    }

    public List<VertexShader> getVertexShaders() {
        return vertexShaders;
    }

    public Vector2D[] getVertices() {
        return vertices;
    }

    public Vector2D getAnchorSum() {
        return anchorSum;
    }

    public boolean isIgnoreOffsetScaling() {
        return ignoreOffsetScaling;
    }

    public void setIgnoreOffsetScaling(boolean ignoreOffsetScaling) {
        this.ignoreOffsetScaling = ignoreOffsetScaling;
    }
}
