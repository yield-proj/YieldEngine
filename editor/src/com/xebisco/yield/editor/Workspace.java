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
import java.awt.event.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Workspace extends JPanel {
    private final JDesktopPane desktopPane;
    private EngineInstall install;

    public Workspace(PreferredInstall preferredInstall) {

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

        desktopPane.setFocusable(true);
        desktopPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for(JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                    if(internalFrame.getX() + internalFrame.getWidth() / 2 > desktopPane.getWidth())
                        internalFrame.setLocation(desktopPane.getWidth() - internalFrame.getWidth() / 2, internalFrame.getY());
                    if(internalFrame.getY()  + internalFrame.getHeight() / 2> desktopPane.getHeight())
                        internalFrame.setLocation(internalFrame.getX(), desktopPane.getHeight() - internalFrame.getHeight() / 2);
                }
            }
        });
        desktopPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });

        desktopPane.setOpaque(false);

        JToolBar toolBar = new JToolBar("Workspace Tool Bar");
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        toolBar.setBorderPainted(false);
        toolBar.add(new JLabel("Workspace", Assets.images.get("workspaceIcon.png"), JLabel.LEFT));
        toolBar.add(Box.createHorizontalGlue());

        toolBar.add(new JLabel(" "));

        JComboBox<EngineInstall> installs = new JComboBox<>(Assets.engineInstalls.toArray(new EngineInstall[0]));
        installs.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInstall((EngineInstall) installs.getSelectedItem());
            }
        });
        installs.setMaximumSize(new Dimension(200, 100));

        if(preferredInstall.preferredInstall() != null) {
            if(Assets.engineInstalls.contains(preferredInstall.preferredInstall()))
                installs.setSelectedItem(preferredInstall.preferredInstall());
        } else {
            setInstall(Assets.engineInstalls.get(0));
        }

        toolBar.add(installs);

        JButton run = new JButton();
        Color savedBkg = new Color(run.getBackground().getRGB());
        run.setAction(new AbstractAction("", Assets.images.get("runIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                run.setBackground(new Color(89, 157, 94));
                run.repaint();
            }
        });

        toolBar.add(run);
        toolBar.add(new JButton(new AbstractAction("", Assets.images.get("searchIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }));
        add(toolBar, BorderLayout.NORTH);
    }

    public JDesktopPane desktopPane() {
        return desktopPane;
    }

    public EngineInstall install() {
        return install;
    }

    public Workspace setInstall(EngineInstall install) {
        this.install = install;
        return this;
    }
}
