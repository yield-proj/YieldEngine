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

import com.xebisco.yield.*;

public class UXMain extends Component {
    private final UXPanel mainPanel;
    private UXPalette palette;
    private Vector2 mouse, rightClickPanelPosition;
    private boolean justPressedRightMouse, justPressedLeftMouse, justPressedMiddleMouse, pressingLeftMouse;
    private float rightClickPanelAlpha;

    public UXMain(Vector2 size, UXPalette palette) {
        this.palette = palette;
        mainPanel = new UXPanel(new Vector2(), size, this);
        mainPanel.setArcWidth(0);
        mainPanel.setArcHeight(0);
    }

    @Override
    public void start() {
        game.loadFont("roboto", 30f, 0, new RelativeFile("/com/xebisco/yield/assets/Roboto-Regular.ttf"));
        game.loadFont("roboto-title", 130f, 0, new RelativeFile("/com/xebisco/yield/assets/Roboto-Medium.ttf"));
    }

    @Override
    public void render(SampleGraphics graphics) {
        mainPanel.setPosition(transform.position);
        mouse = input.getMouse();
        if (mouse.x >= transform.position.x && mouse.x <= transform.position.x + mainPanel.getSize().x && mouse.y >= transform.position.y && mouse.y <= transform.position.y + mainPanel.getSize().y) {
            if (input.isPressed(Key.MOUSE_1)) pressingLeftMouse = true;
            if (input.isJustPressed(Key.MOUSE_1)) justPressedLeftMouse = true;
            if (input.isJustPressed(Key.MOUSE_2)) justPressedMiddleMouse = true;
            if (input.isJustPressed(Key.MOUSE_3)) justPressedRightMouse = true;
        }
        mainPanel.render(graphics, time.getDelta());
        if (justPressedRightMouse) {
            rightClickPanelPosition = mouse.get();
            rightClickPanelAlpha = 0;
        }
        if (rightClickPanelPosition != null) {
            rightClickPanelAlpha += (1f - rightClickPanelAlpha) / (time.getTargetFPS() / 15);
            if (rightClickPanelAlpha > .99f)
                rightClickPanelAlpha = 1;
            Color f1 = getPalette().foreground2.get().brighter(), b1 = getPalette().foreground2.get(), t1 = getPalette().text1.get();
            f1.setA(rightClickPanelAlpha);
            b1.setA(.9f);
            t1.setA(rightClickPanelAlpha);
            String[] names = {"Test1", "Test2"};
            Vector2 size = new Vector2(200, 200);
            if (rightClickPanelPosition.y + size.y > transform.position.y + mainPanel.getSize().y) {
                rightClickPanelPosition.y -= size.y;
            }
            if (rightClickPanelPosition.x + size.x > transform.position.x + mainPanel.getSize().x) {
                rightClickPanelPosition.x -= size.x;
            }
            Vector2 p = rightClickPanelPosition.sum(size.div(2f)).subt(new Vector2(0, (1 - rightClickPanelAlpha) * size.y / 2f)), s = size.mul(new Vector2(1, rightClickPanelAlpha));
            graphics.drawRoundRect(p, s, b1, true, 60, 60);
            graphics.drawRoundRect(p, s, f1, false, 60, 60);
            boolean panelSelected = mouse.x > rightClickPanelPosition.x && mouse.x < rightClickPanelPosition.x + size.x && mouse.y > rightClickPanelPosition.y && mouse.y < rightClickPanelPosition.y + size.y;
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                Color tc = t1;
                float x = rightClickPanelPosition.x + 20 + graphics.getStringWidth(name) / 2f, y = rightClickPanelPosition.y + 40 + (graphics.getStringHeight(name) + 15) * i, w = graphics.getStringWidth(name, getPalette().font1), h = graphics.getStringHeight(name, getPalette().font1);
                if (panelSelected) {
                    if (mouse.y > y - h / 2f && mouse.y < y + h / 2f) {
                        tc = getPalette().text2;
                        graphics.drawRoundRect(new Vector2(p.x, y), new Vector2(s.x, h * 1.5f), getPalette().foreground1, true, 20, 20);
                    }
                }
                graphics.drawString(name, tc, new Vector2(x, y), new Vector2(1, 1), getPalette().font1);
            }
            if (justPressedLeftMouse && !panelSelected) {
                rightClickPanelPosition = null;
            }
        }
        justPressedRightMouse = false;
        justPressedMiddleMouse = false;
        justPressedLeftMouse = false;
        pressingLeftMouse = false;
    }

    public UXPanel getMainPanel() {
        return mainPanel;
    }

    public UXPalette getPalette() {
        return palette;
    }

    public void setPalette(UXPalette palette) {
        this.palette = palette;
    }

    public Vector2 getMouse() {
        return mouse;
    }

    public boolean isJustPressedRightMouse() {
        return justPressedRightMouse;
    }

    public boolean isJustPressedLeftMouse() {
        return justPressedLeftMouse;
    }

    public boolean isJustPressedMiddleMouse() {
        return justPressedMiddleMouse;
    }

    public boolean isPressingLeftMouse() {
        return pressingLeftMouse;
    }
}
