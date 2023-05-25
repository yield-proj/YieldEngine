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
import java.awt.event.ActionEvent;

public class Projects extends JPanel {

    public Projects() {
        setLayout(new BorderLayout());

        JPanel projectsAndTitlePanel = new JPanel();
        projectsAndTitlePanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Projects");
        Color titleBkg = title.getBackground().brighter();
        title.setBackground(titleBkg);
        title.setOpaque(true);
        title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(40f));
        title.setBorder(BorderFactory.createLineBorder(titleBkg, 20, true));
        projectsAndTitlePanel.add(title, BorderLayout.NORTH);

        add(projectsAndTitlePanel, BorderLayout.CENTER);





        JPanel logoAndOptions = new JPanel();
        logoAndOptions.setLayout(new BorderLayout());
        JLabel logo = new JLabel(Assets.images.get("editorLogoSmall.png"));
        logo.setMaximumSize(new Dimension(100, 100));
        logoAndOptions.add(logo, BorderLayout.NORTH);
        add(logoAndOptions, BorderLayout.WEST);

        JList<JButton> options = new JList<>(new JButton[]{
                new JButton(new AbstractAction("Projects") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("aaaa");
                    }
                }),
                new JButton(new AbstractAction("Options") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("bbbb");
                    }
                })
        });
        options.setCellRenderer(new ButtonListCellRenderer<>());
        options.addListSelectionListener(new ButtonSelectionListener(options));
        options.setSelectedIndex(0);
        options.getSelectedValue().getAction().actionPerformed(null);
        logoAndOptions.add(options, BorderLayout.CENTER);




        JFrame projectsList = new JFrame();


    }
}
