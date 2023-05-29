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

public class NewProjectPanel extends JPanel {
    private JTextField nameTextField;
    private JTextField versionTextField;

    public NewProjectPanel(Dialog dialog) {
        setLayout(new BorderLayout());

        /*JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 0, 10);

        JLabel nameLabel = new JLabel("Name:");
        nameTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameTextField, gbc);

        JLabel versionLabel = new JLabel("Version:");
        versionTextField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(versionLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(versionTextField, gbc);

        add(panel, BorderLayout.NORTH);*/
    }
}
