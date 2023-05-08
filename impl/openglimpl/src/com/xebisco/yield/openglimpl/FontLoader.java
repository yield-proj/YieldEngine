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

package com.xebisco.yield.openglimpl;

import com.xebisco.yield.Font;
import org.lwjgl.stb.STBEasyFont.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.stb.STBTruetype.*;

public class FontLoader implements com.xebisco.yield.FontLoader {
    @Override
    public Object loadFont(Font font) {
        return font;
    }

    @Override
    public void unloadFont(Font font) {

    }

    @Override
    public double getStringWidth(String text, Object fontRef) {
        int width = 0;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pCodePoint       = stack.mallocInt(1);
            IntBuffer pAdvancedWidth   = stack.mallocInt(1);
            IntBuffer pLeftSideBearing = stack.mallocInt(1);

            int i = 0;
            while (i < text.length()) {
                i += com.xebisco.yield.openglimpl.Font.getCP(text, text.length(), i, pCodePoint);
                int cp = pCodePoint.get(0);

                stbtt_GetCodepointHMetrics(((com.xebisco.yield.openglimpl.Font) fontRef).getInfo(), cp, pAdvancedWidth, pLeftSideBearing);
                width += pAdvancedWidth.get(0);
            }
        }

        return width * stbtt_ScaleForPixelHeight(((com.xebisco.yield.openglimpl.Font) fontRef).getInfo(), (float) ((com.xebisco.yield.openglimpl.Font) fontRef).getSize());
    }

    @Override
    public double getStringHeight(String text, Object fontRef) {
        return 0;
    }
}
