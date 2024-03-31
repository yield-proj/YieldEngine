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

package com.xebisco.yield.editor.app;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {
    public TitleLabel(String text, Icon icon) {
        super(text, icon, LEFT);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UIManager.getColor("List.background"));
        setOpaque(true);
        setFont(getFont().deriveFont(Font.BOLD).deriveFont(28f));
    }
}

