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

public class TextureRectangle extends Rectangle {

    private Texture texture = Global.getDefaultTexture();

    @Override
    public void onStart() {
        super.onStart();
        getDrawInstruction().setType(DrawInstruction.Type.IMAGE);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        getDrawInstruction().setRenderRef(texture.getImageRef());
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
