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

package com.xebisco.yield;

import com.xebisco.yield.editor.annotations.AffectsEditorEntitySize;
import com.xebisco.yield.editor.annotations.Visible;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Renderer;

@AffectsEditorEntitySize
public class RectangleMesh extends AbstractRenderable {
    @Visible
    private Color color = new Color(1, 1, 1, 1);
    @Visible
    private Vector2D size = new Vector2D(100, 100);

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

    public Color color() {
        return color;
    }

    public RectangleMesh setColor(Color color) {
        this.color = color;
        return this;
    }

    public Vector2D size() {
        return size;
    }

    public RectangleMesh setSize(Vector2D size) {
        this.size = size;
        return this;
    }
}
