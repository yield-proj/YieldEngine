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

public class Material {

    private Color color = Colors.RED;
    private Texture texture;
    private boolean lined;
    private Shape shape = Shape.RECTANGLE;

    public enum Shape {
        RECTANGLE, OVAL
    }

    public Material() {
    }

    public Material(Texture texture) {
        this.texture = texture;
    }

    public Material(Shape shape) {
        this.shape = shape;
    }

    public Material(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Material(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isLined() {
        return lined;
    }

    public void setLined(boolean lined) {
        this.lined = lined;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
