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
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class FileCellRenderer implements ListCellRenderer<File> {
    @Override
    public Component getListCellRendererComponent(JList<? extends File> list, File value, int index, boolean isSelected, boolean cellHasFocus) {
        Image fileIcon;

        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(value);

        if (icon instanceof ImageIcon) {
            fileIcon = ((ImageIcon) icon).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } else {
            fileIcon = YieldEditor.WHAT_ICON;
        }

        JPanel panel = new JPanel() /*{
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isSelected)
                    g.setColor(list.getSelectionBackground());
                else g.setColor(list.getForeground());
                g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 20, 20);
                g.drawImage(fileIcon, getX() + 25, getY() + 15, getWidth() - 50, getHeight() - 50, this);
                if (isSelected)
                    g.setColor(list.getForeground());
                else g.setColor(list.getSelectionBackground());
                g.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD));
                g.drawString(value.getName(), getX() + getWidth() / 2 - g.getFontMetrics().stringWidth(value.getName()) / 2, getY() + getHeight() - 20);
            }
        }*/;

        panel.setLayout(new BorderLayout());

        panel.add(new JLabel(new ImageIcon(fileIcon)), BorderLayout.CENTER);
        JLabel name = new JLabel(value.getName(), JLabel.CENTER);
        name.setFont(name.getFont().deriveFont(Font.BOLD));
        panel.add(name, BorderLayout.SOUTH);

        if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
            panel.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        else {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
        }
        //panel.setOpaque(false);

        panel.setPreferredSize(new Dimension(100, 100));

        return panel;
    }
}
