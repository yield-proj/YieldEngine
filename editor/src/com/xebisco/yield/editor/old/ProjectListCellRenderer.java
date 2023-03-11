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

package com.xebisco.yield.editor.old;

import javax.swing.*;
import java.awt.*;

public class ProjectListCellRenderer extends JPanel implements ListCellRenderer<Project> {

    public ProjectListCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<? extends Project> list, Project value, int index, boolean isSelected, boolean cellHasFocus) {
        Color background;
        if (isSelected) {
            background = UIManager.getColor("Tree.selectionBackground");
        } else {
            background = UIManager.getColor("Tree.background");
        }

        removeAll();

        JPanel main = new JPanel(new FlowLayout(FlowLayout.LEFT));
        main.setOpaque(false);
        JLabel label = new JLabel(value.getProjectName());
        label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(20f));
        main.add(label);
        label = new JLabel(value.getDirectory().getPath());
        main.add(label);
        JPanel star = new JPanel(new BorderLayout());
        star.setOpaque(false);
        star.setPreferredSize(new Dimension(32, 32));
        if (value.isStarred())
            star.add(new JLabel(Icons.STARRED_ICON_16x16), BorderLayout.CENTER);
        else star.add(new JLabel(Icons.NOT_STARRED_ICON_16x16), BorderLayout.CENTER);

        add(star, BorderLayout.EAST);

        add(main, BorderLayout.CENTER);


        setBackground(background);

        return this;
    }
}