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

package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.Assets;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;

public class PathProp extends Prop {
    private final int fileSelectionMode;

    public PathProp(String name, Serializable value, int fileSelectionMode) {
        super(name, value);
        this.fileSelectionMode = fileSelectionMode;
    }

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(getName() + "   "), BorderLayout.WEST);
        panel.add(panel2(), BorderLayout.CENTER);
        return panel;
    }

    protected JPanel panel2() {
        JPanel panel2 = new JPanel();
        JTextField field = new JTextField((String) getValue());
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setValue(field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setValue(field.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        panel2.setLayout(new BorderLayout());
        panel2.add(field, BorderLayout.CENTER);
        panel2.add(new JButton(new AbstractAction("", Assets.images.get("uploadIcon16.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(fileSelectionMode);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    field.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        }), BorderLayout.EAST);
        return panel2;
    }
}
