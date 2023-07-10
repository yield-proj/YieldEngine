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
import com.xebisco.yield.editor.EngineInstall;
import com.xebisco.yield.editor.YieldBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class EngineInstallProp extends Prop {
    private Image image;

    private final JPanel imagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    };

    private final EngineInstall install;

    public EngineInstallProp(EngineInstall install) {
        super(install.getInstall(), null);
        this.install = install;
        imagePanel.setBorder(BorderFactory.createTitledBorder(new YieldBorder(), install.getInstall()));
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

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(imagePanel, BorderLayout.WEST);
        JPanel mainP = new JPanel();
        mainP.setLayout(new BorderLayout());
        JLabel name = new JLabel(install.getName());
        name.setFont(name.getFont().deriveFont(Font.BOLD).deriveFont(name.getFont().getSize2D() + 3));
        mainP.add(name, BorderLayout.NORTH);
        mainP.add(new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setVerticalAlignment(SwingConstants.TOP);
                StringBuilder t = new StringBuilder("<html>");
                int w = 0;
                for(char c : install.getDescription().toCharArray()) {
                    w += g.getFontMetrics().charWidth(c);
                    if(w >= getParent().getWidth()) {
                        w = 0;
                        t.append("<br>");
                    }
                    t.append(c);
                }

                t.append("</html>");

                setText(t.toString());
            }
        });
        panel.add(mainP, BorderLayout.CENTER);
        return panel;
    }
}
