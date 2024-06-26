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

package com.xebisco.yield.tileeditor.app;

import javax.swing.*;
import java.awt.*;

public class TileListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setPreferredSize(new Dimension(100, 100));
        setSize(new Dimension(100, 100));
        setToolTipText(value.toString());
        setText(((Tile) value).name());
        if (((Tile) value).image() != null)
            setIcon(new ImageIcon(((Tile) value).image().getScaledInstance(100, -1, Image.SCALE_FAST)));
        return this;
    }
}
