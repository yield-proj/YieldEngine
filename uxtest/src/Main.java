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

import com.xebisco.yield.*;
import com.xebisco.yield.ux.UXCanvas;
import com.xebisco.yield.ux.UXComponent;
import com.xebisco.yield.ux.UXGraphics;
import com.xebisco.yield.ux.UXPanel;

public class Main extends YldGame {

    @Override
    public void create() {
        UXCanvas canvas = new UXCanvas();
        instantiate(e -> {
            e.addComponent(canvas);
            e.addComponent(new YldScript() {
                @Override
                public void update(float delta) {
                    float x = input.getAxis("Horizontal");
                    if(x != 0) {
                        getComponent(UXCanvas.class).getPanels().forEach(p -> {
                            p.setWidth((int) (p.getWidth() + x));
                            p.repaint();
                        });
                    }
                }
            });
        });
        UXPanel panel = new UXPanel(getView().getWidth() - 200, getView().getHeight() - 200);
        canvas.add(panel);
        panel.add(new UXComponent() {
            @Override
            protected void paintComponent(UXGraphics g) {
                super.paintComponent(g);
                g.fillOval(1000, 1000, 100, 100);
                g.drawString("Hello, World!", 10, 10);
            }
        });
    }

    public static void main(String[] args) {
        launch(new Main());
    }
}