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

import java.io.InputStream;

public class Texture extends FileInput {

    private Object imageRef;
    private final ImmutableSize2D size;

    public Texture(String relativePath, TextureLoader textureLoader) {
        super(relativePath);
        imageRef = textureLoader.loadTexture(this);
        size = new ImmutableSize2D(textureLoader.getImageWidth(this), textureLoader.getImageHeight(this));
    }

    public Texture(InputStream inputStream, TextureLoader textureLoader) {
        super(inputStream);
        imageRef = textureLoader.loadTexture(this);
        size = new ImmutableSize2D(textureLoader.getImageWidth(this), textureLoader.getImageHeight(this));
    }

    public Object getImageRef() {
        return imageRef;
    }

    public void setImageRef(Object imageRef) {
        this.imageRef = imageRef;
    }

    public ImmutableSize2D getSize() {
        return size;
    }
}
