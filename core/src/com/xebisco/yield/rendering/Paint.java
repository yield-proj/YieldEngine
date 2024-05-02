/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yield.rendering;

import com.xebisco.yield.Color;
import com.xebisco.yield.Colors;
import com.xebisco.yield.Transform2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.font.Font;

/**
 * {@code Paint} class for rendering objects with specified attributes.
 */
public class Paint {
    private Object drawObj;
    private Vector2D rectSize;
    private Transform2D transformation;
    private String text;
    private Font font;
    private boolean hasImage;
    private Color color = new Color(Colors.WHITE);

    /**
     * Returns the object reference {@code like image references} to be drawn.
     *
     * @return the object reference to be drawn
     */
    public Object drawObj() {
        return drawObj;
    }

    /**
     * Sets the object reference to be drawn.
     *
     * @param drawObj the object reference to be drawn
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setDrawObj(Object drawObj) {
        this.drawObj = drawObj;
        return this;
    }

    /**
     * Returns the transformation to be applied to the object before drawing.
     *
     * @return the transformation to be applied
     */
    public Transform2D transformation() {
        return transformation;
    }

    /**
     * Sets the transformation to be applied to the object before drawing.
     *
     * @param transformation the transformation to be applied
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setTransformation(Transform2D transformation) {
        this.transformation = transformation;
        return this;
    }

    /**
     * Returns true whether an image should be included in the object to be drawn.
     *
     * @return true if an image is included, false otherwise
     */
    public boolean hasImage() {
        return hasImage;
    }

    /**
     * Sets whether an image should be included in the object to be drawn.
     *
     * @param hasImage true if an image should be included, false otherwise
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
        return this;
    }

    /**
     * Returns the color of the object to be drawn.
     *
     * @return the color of the object to be drawn
     */
    public Color color() {
        return color;
    }

    /**
     * Sets the color of the object to be drawn.
     *
     * @param color the color of the object to be drawn
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Returns the text to be displayed.
     *
     * @return the text to be displayed
     */
    public String text() {
        return text;
    }

    /**
     * Sets the text to be displayed.
     *
     * @param text the text to be displayed
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Returns the font to be used for the text.
     *
     * @return the font to be used for the text
     */
    public Font font() {
        return font;
    }

    /**
     * Sets the font to be used for the text.
     *
     * @param font the font to be used for the text
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setFont(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Returns the size of the rectangle to be drawn.
     *
     * @return the size of the rectangle
     */
    public Vector2D rectSize() {
        return rectSize;
    }

    /**
     * Sets the size of the rectangle in which the object will be drawn.
     *
     * @param rectSize the size of the rectangle
     * @return this instance of the {@link Paint} class for method chaining
     */
    public Paint setRectSize(Vector2D rectSize) {
        this.rectSize = rectSize;
        return this;
    }
}
