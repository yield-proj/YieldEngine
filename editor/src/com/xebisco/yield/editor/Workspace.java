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
import java.awt.geom.AffineTransform;
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
            public void paint(Graphics g) {
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
                            if (x1 < x3 + i.parent().getWidth() && x1 + i.getWidth() > x3) {
                                if (i.getY() + i.getHeight() > i.parent().getY() && i.getY() < i.parent().getY() + i.parent().getHeight())
                                    continue;
                                x1 += i.getWidth() / 2;
                                x3 += i.parent().getWidth() / 2;
                                y1 -= i.getHeight() / 2;
                                y3 -= i.parent().getHeight() / 2;

                                if (y3 > y1) y1 += i.getHeight();
                                else y3 += i.parent().getHeight();
                                Path2D p = new Path2D.Float();
                                p.moveTo(x1, y1);
                                p.curveTo(x1, y3, x3, y1, x3, y3);
                                ((Graphics2D) g).draw(p);
                            } else {
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
                super.paint(g);
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
