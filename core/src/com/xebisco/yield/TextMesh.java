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

import com.xebisco.yield.editor.annotations.Visible;
import com.xebisco.yield.font.Font;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Renderer;

/**
 * {@code TextMesh} class extends {@link AbstractRenderable} and represents a text mesh object.
 * It is used to render text with specified contents, color, and font.
 */
public class TextMesh extends AbstractRenderable {

    @Visible
    private String contents = "Sample Text";
    @Visible
    private Color color = new Color(1, 1, 1, 1);
    @Visible
    private Font font;

    /**
     * Constructs a new TextMesh.
     */
    public TextMesh() {
        super(Form.TEXT);
    }

    @Override
    public void render(Renderer renderer) {
        paint().setColor(color);
        paint().setText(contents);
        paint().setFont(font);
        super.render(renderer);
    }

    @Override
    public void onStart() {
        if(font == null)
            font = application().defaultFont();
    }

    /**
     * Returns the text contents of the {@link TextMesh}.
     *
     * @return The text contents.
     */
    public String contents() {
        return contents;
    }

    /**
     * Sets the text contents of the {@link TextMesh}.
     *
     * @param contents The new text contents to be set.
     * @return The {@link TextMesh} object for method chaining.
     */
    public TextMesh setContents(String contents) {
        this.contents = contents;
        return this;
    }
    /**
     * Returns the color of the {@link TextMesh}.
     *
     * @return The color of the text.
     */
    public Color color() {
        return color;
    }

    /**
     * Sets the color of the {@link TextMesh}.
     *
     * @param color The new color to be set.
     * @return The {@link TextMesh} object for method chaining.
     */
    public TextMesh setColor(Color color) {
        this.color = color;
        return this;
    }
}
