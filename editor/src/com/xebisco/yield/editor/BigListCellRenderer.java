/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public class BigListCellRenderer extends JLabel implements ListCellRenderer<String> {
  public BigListCellRenderer() {
    setOpaque(true);
  }

  public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
    setText(value);
    setHorizontalAlignment(CENTER);

    Color background;

    if (isSelected) {
      background = UIManager.getColor("Tree.selectionBackground");
    } else {
      background = UIManager.getColor("Tree.background");
    }

    setBackground(background);

    return this;
  }
}