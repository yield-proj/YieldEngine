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

package com.xebisco.yieldengine;

import com.xebisco.yieldengine.editor.annotations.AffectsEditorEntitySize;
import com.xebisco.yieldengine.editor.annotations.Visible;
import com.xebisco.yieldengine.rendering.Form;
import com.xebisco.yieldengine.rendering.Renderer;

/**
 * {@code RectangleMesh} class extends {@link AbstractRenderable} and represents a rectangle mesh object.
 * It is used to render a rectangle with specified color and size.
 */
@AffectsEditorEntitySize
public class RectangleMesh extends AbstractRenderable {
    @Visible
    private Color color = new Color(1, 1, 1, 1);
    @Visible
    private Vector2D size = new Vector2D(100, 100);

    /**
     * Constructs a new RectangleMesh.
     */
    public RectangleMesh() {
        super(Form.SQUARE);
        paint().setHasImage(false);
    }

    @Override
    public void render(Renderer renderer) {
        paint().setColor(color);
        paint().setRectSize(size);
        super.render(renderer);
    }

    /**
     * Gets the color of the rectangle mesh.
     *
     * @return The color of the rectangle mesh.
     */
    public Color color() {
        return color;
    }

    /**
     * Sets the color of the rectangle mesh.
     *
     * @param color The new color for the rectangle mesh.
     * @return This {@link RectangleMesh} instance for method chaining.
     */
    public RectangleMesh setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Gets the size of the rectangle mesh.
     *
     * @return The size of the rectangle mesh.
     */
    public Vector2D size() {
        return size;
    }

    /**
     * Sets the size of the rectangle mesh.
     *
     * @param size The new size for the rectangle mesh.
     * @return This {@link RectangleMesh} instance for method chaining.
     */
    public RectangleMesh setSize(Vector2D size) {
        this.size = size;
        return this;
    }
}
