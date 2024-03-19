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

package com.xebisco.yield.tileeditor.app.tool;

import com.xebisco.yield.tileeditor.app.MapEditor;
import com.xebisco.yield.tileeditor.app.Tool;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PaintTool implements Tool {
    @Override
    public void press(Point mouse, MapEditor mapEditor) {
        mapEditor.project().map().map()[mouse.x][mouse.y] = 1;
    }

    @Override
    public void process(Point mouse, MapEditor mapEditor) {

    }

    @Override
    public ImageIcon icon() {
        return new ImageIcon(Objects.requireNonNull(PaintTool.class.getResource("/pencil.png")));
    }

    @Override
    public String showName() {
        return "Paint Tool";
    }
}
