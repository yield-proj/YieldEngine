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

package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {

    private InEditorScene scene;

    private int middleX, middleY;

    public InEditorScene getScene() {
        return scene;
    }

    public void setScene(InEditorScene scene) {
        this.scene = scene;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        middleX = getHeight() / 2;
        middleY = getHeight() / 2;
        g.setColor(scene.getBackGroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
