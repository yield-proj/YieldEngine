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

public interface FontManager {
    /**
     * The function loads a font object in Java.
     *
     * @param font The "font" parameter in the "loadFont" method is the font that you want to load.
     */
    Object loadFont(Font font);

    /**
     * The function unloads a specified font.
     *
     * @param font The parameter "font" is a variable of type "Font" that represents a font loaded in memory. This function
     * "unloadFont" is used to release the memory allocated for the font and free up resources used by the font.
     */
    void unloadFont(Font font);

    /**
     * Calculates the text width based on the font reference gave.
     *
     * @param text    The text.
     * @param fontRef The font reference.
     * @return The text width.
     */
    double getStringWidth(String text, Object fontRef);

    /**
     * Calculates the text height based on the font reference gave.
     *
     * @param text    The text.
     * @param fontRef The font reference.
     * @return The text height.
     */
    double getStringHeight(String text, Object fontRef);
}
