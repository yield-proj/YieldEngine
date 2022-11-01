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

import java.awt.*;

public class Button implements Renderable, Logical {
    private Dimension size = new Dimension(200, 60);
    private Point location;
    private String contents = "Button";

    private Color color1 = DefColors.foreground, color2 = DefColors.selected;

    public Button(Point location, String contents) {
        this.location = location;
        this.contents = contents;
    }

    int x, y;

    @Override
    public void update() {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        mouse = new Point(mouse.x - EditorMain.getFrame().getX() + 22 - 8, mouse.y - EditorMain.getFrame().getY() - 8);
        x = location.x;
        y = location.y;
        if (location.x < 0)
            x += Renderer.getPanelWidth().getValue();
        if (location.y < 0)
            y += Renderer.getPanelHeight().getValue();
        if (mouse.x > x && mouse.x < x + size.width && mouse.y > y && mouse.y < y + size.height) {
            color1 = DefColors.selected;
            color2 = DefColors.foreground;
            Renderer.getInstance().repaint();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color1);
        g.fillRoundRect(x, y, size.width, size.height, 40, 40);
    }
}
