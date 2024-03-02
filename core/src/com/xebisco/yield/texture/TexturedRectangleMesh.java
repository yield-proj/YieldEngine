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

package com.xebisco.yield.texture;

import com.xebisco.yield.AbstractTexture;
import com.xebisco.yield.RectangleMesh;
import com.xebisco.yield.rendering.Renderer;

public class TexturedRectangleMesh extends RectangleMesh {

    private AbstractTexture texture;

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

    public AbstractTexture texture() {
        return texture;
    }

    public TexturedRectangleMesh setTexture(AbstractTexture texture) {
        this.texture = texture;
        return this;
    }
}
