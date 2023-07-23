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
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class Workspace extends JPanel {
    private final JDesktopPane desktopPane;

    public Workspace() {

        setLayout(new BorderLayout());

        Image image = Assets.images.get("bkg.png").getImage();

        add(desktopPane = new JDesktopPane() {
            @Override
            public void paint(Graphics g) {
                int w = image.getWidth(null), h = image.getHeight(null);
                if(w < getWidth())
                    w = getWidth();
                if(h < getHeight())
                    h = getHeight();
                g.drawImage(image, 0, 0, w, h, this);
                super.paint(g);
            }
        }, BorderLayout.CENTER);

        desktopPane.setOpaque(false);

        JToolBar toolBar = new JToolBar("Workspace Tool Bar");
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        toolBar.setBorderPainted(false);
        toolBar.add(new JLabel("Workspace", Assets.images.get("workspaceIcon.png"), JLabel.LEFT));
        toolBar.add(Box.createHorizontalGlue());

        JButton run = new JButton();
        Color savedBkg = new Color(run.getBackground().getRGB());
        run.setAction(new AbstractAction("", Assets.images.get("runIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                run.setBackground(new Color(89, 157, 94));
                run.repaint();
            }
        });

        toolBar.add(new JLabel(" "));

        toolBar.add(run);
        toolBar.add(new JButton(new AbstractAction("", Assets.images.get("searchIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }));
        add(toolBar, BorderLayout.NORTH);
    }
}
