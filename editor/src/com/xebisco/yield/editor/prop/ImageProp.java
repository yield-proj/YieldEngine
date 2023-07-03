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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class ImageProp extends PathProp {
    private Image image;
    private JPanel imagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    };

    public ImageProp(String name) {
        super(name, null, JFileChooser.FILES_ONLY);
        loadNull();
    }

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(super.panel(), BorderLayout.SOUTH);
        panel.add(p1, BorderLayout.CENTER);
        panel.add(imagePanel, BorderLayout.EAST);
        return panel;
    }

    @Override
    public void setValue(Serializable value) {
        super.setValue(value);
        try {
            image = ImageIO.read(new FileInputStream((String) value));
        } catch (IOException e) {
            loadNull();
            return;
        }
        imagePanel.setPreferredSize(new Dimension(100, (int) (100 * ((double) image.getHeight(null) / image.getWidth(null)))));
        imagePanel.setSize(imagePanel.getPreferredSize());
        Component c = imagePanel;
        while (c != null && !(c instanceof Window))
            c = c.getParent();
        if (c != null)
            c.revalidate();
        System.out.println(c);
    }

    public void loadNull() {
        super.setValue(null);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Preview:"));
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageProp.class.getResourceAsStream("/yieldIcon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagePanel.setPreferredSize(new Dimension(100, (int) (100 * ((double) image.getHeight(null) / image.getWidth(null)))));
        imagePanel.setSize(imagePanel.getPreferredSize());
        Component c = imagePanel;
        while (c != null && !(c instanceof Window))
            c = c.getParent();
        if (c != null)
            c.revalidate();
    }
}
