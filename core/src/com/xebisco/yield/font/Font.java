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

package com.xebisco.yield.font;

import com.xebisco.yield.FileInput;
import com.xebisco.yield.manager.FontManager;

import java.util.HashMap;
import java.util.Map;

/**
 * The Font class represents a font loaded from a file or input stream with a specified size and font loader.
 */
public class Font extends FileInput {

    private final Map<Character, FontCharacter> characterMap = new HashMap<>();
    private final double size;

    public Font(String path, double size, FontManager fontManager) {
        super(path);
        this.size = size;
        fontManager.loadFont(this);
    }

    public Map<Character, FontCharacter> characterMap() {
        return characterMap;
    }

    public double size() {
        return size;
    }
}
