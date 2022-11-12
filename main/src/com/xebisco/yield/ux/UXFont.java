/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.ux;

import com.xebisco.yield.RelativeFile;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.render.RenderMaster;

public class UXFont {
    public static final int PLAIN = 0, BOLD = 1, ITALIC = 2, LOADED = 3;
    private final String name;
    private final String genName = String.valueOf(Yld.RAND.nextLong());
    private final int style;
    private final float size;
    private final RenderMaster renderMaster;
    private final RelativeFile relativeFile;

    public UXFont(String name, float size, int style, RenderMaster renderMaster, RelativeFile relativeFile) {
        this.name = name;
        this.style = style;
        this.size = size;
        this.renderMaster = renderMaster;
        this.relativeFile = null;
        renderMaster.loadFont(genName, name, size, style);
    }

    public UXFont(String name, float size, int format, RelativeFile relativeFile, RenderMaster renderMaster) {
        this.name = name;
        this.style = UXFont.LOADED;
        this.size = size;
        this.renderMaster = renderMaster;
        this.relativeFile = relativeFile;
        renderMaster.loadFont(genName, size, size, format, relativeFile);
    }

    public String getName() {
        return name;
    }

    public int getStyle() {
        return style;
    }

    public float getSize() {
        return size;
    }

    public RenderMaster getRenderMaster() {
        return renderMaster;
    }

    public String getGenName() {
        return genName;
    }
}
