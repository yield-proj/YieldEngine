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

import com.xebisco.yield.render.RenderMaster;
import com.xebisco.yield.Vector2;

public class UXFontMetrics {
    private UXFont font;
    private RenderMaster renderMaster;
    public Vector2 getStringBounds(String string) {
        return new Vector2(renderMaster.getStringWidth(string, font.getGenName()), renderMaster.getStringHeight(string, font.getGenName()));
    }

    public int stringWidth(String string) {
        return (int) getStringBounds(string).x;
    }

    public int getHeight() {
        return (int) font.getSize();
    }

    public void setFont(UXFont font) {
        this.font = font;
    }

    public UXFont getFont() {
        return font;
    }

    public RenderMaster getRenderMaster() {
        return renderMaster;
    }

    public void setRenderMaster(RenderMaster renderMaster) {
        this.renderMaster = renderMaster;
    }
}
