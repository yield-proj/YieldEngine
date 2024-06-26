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

package com.xebisco.yieldengine.uiutils;

import javax.swing.*;
import java.awt.*;

public class PrettyListCellRenderer<T extends String> extends PrettyLabel implements ListCellRenderer<T> {

    @Override
    public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
        Color background, foreground;
        if(isSelected) {
            foreground = list.getSelectionForeground();
            background = list.getSelectionBackground();
        }
        else {
            foreground = list.getForeground();
            background = list.getBackground();
        }

        setText(value);
        setPreferredSize(new Dimension(100, 42));
        setBackground(background);
        setForeground(foreground);
        return this;
    }
}
