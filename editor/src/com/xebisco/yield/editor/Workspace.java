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
import java.awt.geom.Path2D;

public class Workspace extends JPanel {
    private final JDesktopPane desktopPane;
    private final Project project;

    private final IRecompile recompile;

    public Workspace(Project project, IRecompile recompile) {
        this.project = project;
        this.recompile = recompile;

        setLayout(new BorderLayout());

        Image image = Assets.images.get("bkg.png").getImage();

        add(desktopPane = new JDesktopPane() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = image.getWidth(null), h = image.getHeight(null);
                if (w < getWidth())
                    w = getWidth();
                if (h < getHeight())
                    h = getHeight();
                g.drawImage(image, 0, 0, w, h, this);
                g.setColor(Color.YELLOW);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (JInternalFrame internalFrame : getAllFrames()) {
                    if (internalFrame.isVisible() && !internalFrame.isIcon() && internalFrame instanceof YieldInternalFrame i) {
                        if (i.parent() != null && i.parent().isVisible() && !i.parent().isIcon()) {
                            int x1 = i.getX(), x3 = i.parent().getX(), y1 = i.getY() + i.getHeight() / 2, y3 = i.parent().getY() + i.parent().getHeight() / 2;
                            if (x3 > x1) x1 += i.getWidth();
                            else x3 += i.parent().getWidth();
                            Path2D p = new Path2D.Float();
                            p.moveTo(x1, y1);
                            p.curveTo(x3, y1, x1, y3, x3, y3);
                            ((Graphics2D) g).draw(p);
                        }
                    }
                }
            }
        }, BorderLayout.CENTER);

        desktopPane.setFocusable(true);
        desktopPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                    if (internalFrame.getX() + internalFrame.getWidth() / 2 > desktopPane.getWidth())
                        internalFrame.setLocation(desktopPane.getWidth() - internalFrame.getWidth() / 2, internalFrame.getY());
                    if (internalFrame.getY() + internalFrame.getHeight() / 2 > desktopPane.getHeight())
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

    public Project project() {
        return project;
    }

    public IRecompile recompile() {
        return recompile;
    }
}
