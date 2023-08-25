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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class EngineInstallsListPanel extends JPanel implements MouseListener {

    private final List<ObjPos> pos = new ArrayList<>();

    public EngineInstallsListPanel() {
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color brighterBkg = UIManager.getColor("List.background");
        int h = 80, s = 10;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (Assets.engineInstalls.isEmpty()) {
            g.setColor(getForeground());
            String noProject = "No engine installed.";
            int sw = g.getFontMetrics().stringWidth(noProject);
            g.drawString(noProject, getWidth() / 2 - sw / 2, g.getFont().getSize());
        } else {
            Font font = g.getFont();
            Font bigFont = font.deriveFont(font.getSize() + 10f).deriveFont(Font.BOLD);
            pos.clear();
            for (int i = 0; i < Assets.engineInstalls.size(); i++) {
                EngineInstall install = Assets.engineInstalls.get(i);
                g.setColor(brighterBkg);
                int y = i * h + (i * s), w = getWidth() - s;
                pos.add(new ObjPos(install, y));
                g.fillRoundRect(0, y, w, h, 20, 20);
                g.fillRect(0, y, s, h);
                g.drawImage(Assets.images.get("yieldSmallIcon.png").getImage(), s + 10, y + h / 2 - 40, null);
                g.setColor(getForeground());
                g.setFont(bigFont);
                g.drawString(install.name(), s + 20 + h, y + bigFont.getSize() + 10);
                g.setFont(font);
                g.drawString(install.install(), s + 20 + h, y + h - 10);
                g.setColor(getForeground().darker());
                ((Graphics2D) g).setStroke(new BasicStroke(1));
                g.drawLine(getWidth() - 10 - 12, y + 40 - 15, getWidth() - 15 - 12, y + 40 - 20);
                g.drawLine(getWidth() - 15 - 12, y + 40 - 20, getWidth() - 20 - 12, y + 40 - 15);
                g.drawLine(getWidth() - 10 - 12, y + 40 + 15, getWidth() - 15 - 12, y + 40 + 20);
                g.drawLine(getWidth() - 15 - 12, y + 40 + 20, getWidth() - 20 - 12, y + 40 + 15);
            }
            int y = Assets.projects.size() * h + (Assets.projects.size() * s);
            setPreferredSize(new Dimension(getPreferredSize().width, y));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ObjPos selectedInstall = null;

        for (ObjPos pp : pos) {
            if (e.getY() >= pp.position() && e.getY() <= pp.position() + 80) {
                selectedInstall = pp;
                break;
            }
        }

        if (selectedInstall != null) {
            int index = Assets.engineInstalls.indexOf((EngineInstall) selectedInstall.obj());
            if (e.getY() > selectedInstall.position() + 40) {
                if (index < Assets.engineInstalls.size() - 1) {
                    Assets.engineInstalls.remove((EngineInstall) selectedInstall.obj());
                    Assets.engineInstalls.add(index + 1, (EngineInstall) selectedInstall.obj());
                }
            } else {
                if (index > 0) {
                    Assets.engineInstalls.remove((EngineInstall) selectedInstall.obj());
                    Assets.engineInstalls.add(index - 1, (EngineInstall) selectedInstall.obj());
                }
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
