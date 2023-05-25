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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileCellRenderer implements ListCellRenderer<File> {

    public static Map<File, Image> IMAGE_CACHE = new HashMap<>();

    @Override
    public Component getListCellRendererComponent(JList<? extends File> list, File value, int index, boolean isSelected, boolean cellHasFocus) {
        Image fileIcon;

        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(value);

        if (IMAGE_CACHE.containsKey(value)) {
            fileIcon = IMAGE_CACHE.get(value);
        } else {
            if (value.getName().endsWith(".png") || value.getName().endsWith(".jpeg") || value.getName().endsWith(".jpg")) {
                try {
                    fileIcon = ImageIO.read(value).getScaledInstance(50, 50, Image.SCALE_FAST);
                } catch (IOException e) {
                    fileIcon = YieldEditor.WHAT_ICON.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                }
            } else
                fileIcon = ((ImageIcon) icon).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            IMAGE_CACHE.put(value, fileIcon);
        }

        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.add(new JLabel(new ImageIcon(fileIcon)), BorderLayout.CENTER);
        JLabel name = new JLabel(value.getName(), JLabel.CENTER);
        name.setFont(name.getFont().deriveFont(Font.BOLD));
        panel.add(name, BorderLayout.SOUTH);

        if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
            panel.setBorder(BorderFactory.createRaisedBevelBorder());
        } else {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
        }
        //panel.setOpaque(false);

        panel.setPreferredSize(new Dimension(100, 100));

        return panel;
    }
}
