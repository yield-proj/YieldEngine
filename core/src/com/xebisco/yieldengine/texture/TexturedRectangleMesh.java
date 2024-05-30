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

package com.xebisco.yieldengine.texture;

import com.xebisco.yieldengine.AbstractTexture;
import com.xebisco.yieldengine.RectangleMesh;
import com.xebisco.yieldengine.editor.annotations.Visible;
import com.xebisco.yieldengine.rendering.Renderer;

/**
 * A subclass of {@link RectangleMesh} that adds the ability to apply a texture to the mesh.
 */
public class TexturedRectangleMesh extends RectangleMesh {

    @Visible
    private AbstractTexture texture;

    /**
     * Constructs a new {@link TexturedRectangleMesh}.
     */
    public TexturedRectangleMesh() {
        paint().setHasImage(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(texture == null) texture = application().defaultTexture();
    }

    @Override
    public void render(Renderer renderer) {
        paint().setDrawObj(texture.imageRef());
        super.render(renderer);
    }

    /**
     * Returns the {@link AbstractTexture} object applied to the mesh.
     *
     * @return The texture applied to the mesh.
     */
    public AbstractTexture texture() {
        return texture;
    }

    /**
     * Sets the texture applied to the mesh.
     *
     * @param texture The texture to apply to the mesh.
     * @return This instance of {@link TexturedRectangleMesh} for method chaining.
     */
    public TexturedRectangleMesh setTexture(AbstractTexture texture) {
        this.texture = texture;
        return this;
    }
}
