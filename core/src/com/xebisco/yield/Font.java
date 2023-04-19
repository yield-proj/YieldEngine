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

/**
 * The Font class represents a font loaded from a file or input stream with a specified size and font loader.
 */
public class Font extends FileInput {

    private Object fontRef;
    private final double size;

    public Font(String relativePath, double size, FontLoader fontLoader) {
        super(relativePath);
        this.size = size;
        fontRef = fontLoader.loadFont(this);
    }

    public Font(InputStream inputStream, double size, FontLoader fontLoader) {
        super(inputStream);
        this.size = size;
        fontRef = fontLoader.loadFont(this);
    }

    /**
     * The function returns a reference to a font object.
     *
     * @return The fontRef object.
     */
    public Object getFontRef() {
        return fontRef;
    }

    /**
     * This function sets the font reference object for a Java object.
     *
     * @param fontRef The parameter `fontRef` is an object that represents a reference to a font.
     */
    public void setFontRef(Object fontRef) {
        this.fontRef = fontRef;
    }

    /**
     * The function returns the size as a double.
     *
     * @return The method is returning a double value which is the size.
     */
    public double getSize() {
        return size;
    }
}
