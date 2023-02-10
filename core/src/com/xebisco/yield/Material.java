/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

/**
 *  This is the main class that will set how a Entity looks, by setting its color or Texture.
 * @author Xebisco
 */
public class Material {

    private Color color = Colors.RED;
    private Texture texture;
    private boolean lined;
    @Deprecated
    private Shape shape = Shape.RECTANGLE;

    @Deprecated
    public enum Shape {
        RECTANGLE, OVAL
    }

    /**
     * Creates an empty Material instance.
     */
    public Material() {
    }

    /**
     * Creates a Material instance based on the passed Texture instance.
     * @param texture The texture instance to be added to this Material.
     */
    public Material(Texture texture) {
        this.texture = texture;
    }

    @Deprecated
    public Material(Shape shape) {
        this.shape = shape;
    }

    @Deprecated
    public Material(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    /**
     * Creates a Material instance based on the passed Color instance.
     * @param color The color instance to be added to this Material.
     */
    public Material(Color color) {
        this.color = color;
    }

    /**
     * Getter of the color variable.
     * @return The color variable.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter of the color variable.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter of the lined variable.
     * @return The lines variable.
     */
    public boolean isLined() {
        return lined;
    }

    /**
     * Sets if the connected graphical object will be lined.
     * @param lined If the graphical object will be lined.
     */
    public void setLined(boolean lined) {
        this.lined = lined;
    }

    /**
     * Getter of the texture variable.
     * @return The texture variable.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Setter of the texture variable.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Deprecated
    public Shape getShape() {
        return shape;
    }

    @Deprecated
    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
