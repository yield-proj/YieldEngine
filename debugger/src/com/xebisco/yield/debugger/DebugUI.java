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

package com.xebisco.yield.debugger;

import com.xebisco.yield.*;

public class DebugUI extends ComponentBehavior {
    public static final Entity2DPrefab DEBUG_UI_PREFAB = new Entity2DPrefab(new ComponentCreation(DebugUI.class));

    private boolean canOpen, opened;

    private DrawInstruction inst = new DrawInstruction();

    @Override
    public void onStart() {
        inst.setFont(getApplication().getDefaultFont());
    }

    @Override
    public void onUpdate() {
        if (getApplication().isPressingKey(Input.Key.VK_CONTROL) && getApplication().isPressingKey(Input.Key.VK_SHIFT) && getApplication().isPressingKey(Input.Key.VK_Z)) {
            if (canOpen) {
                canOpen = false;
                opened = !opened;
            }
        } else {
            canOpen = true;
        }
    }

    @Override
    public void render(PlatformGraphics graphics) {
        if (opened) {
            inst.setType(DrawInstruction.Type.RECTANGLE);
            inst.setFilled(true);
            inst.setInnerColor(new Color(0, 0, 0, .3));
            inst.setBorderColor(null);
            inst.setBorderThickness(0);
            inst.setPosition(new Vector2D(0, 0));
            inst.setSize(getApplication().getPlatformInit().getViewportSize());
            graphics.draw(inst);


            inst.setFilled(true);
            inst.setInnerColor(Colors.BLACK);
            inst.setBorderColor(Colors.GRAY);
            inst.setBorderThickness(2);
            inst.setPosition(new Vector2D(0, 0));
            inst.setSize(new Size2D(200, inst.getFont().getSize() + 5));
            graphics.draw(inst);
        }
    }
}
