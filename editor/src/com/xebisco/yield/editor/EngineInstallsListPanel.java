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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineInstallsListPanel extends JPanel {

    public EngineInstallsListPanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color brighterBkg = getBackground().brighter();
        int h = 80, s = 10;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (Assets.engineInstalls.size() == 0) {
            g.setColor(getForeground());
            String noProject = "No engine installed.";
            int sw = g.getFontMetrics().stringWidth(noProject);
            g.drawString(noProject, getWidth() / 2 - sw / 2, g.getFont().getSize());
        } else {
            Font font = g.getFont();
            Font bigFont = font.deriveFont(font.getSize() + 10f).deriveFont(Font.BOLD);
            for (int i = 0; i < Assets.engineInstalls.size(); i++) {
                EngineInstall install = Assets.engineInstalls.get(i);
                g.setColor(brighterBkg);
                int y = i * h + (i * s), w = getWidth() - s;
                g.fillRoundRect(s, y, w, h, 20, 20);
                g.fillRect(w, y, s, h);
                g.drawImage(Assets.images.get("yieldSmallIcon.png").getImage(), s + 10, y + h / 2 - 40, null);
                g.setColor(getForeground());
                g.setFont(bigFont);
                g.drawString(install.name(), s + 20 + h, y + bigFont.getSize() + 10);
                g.setFont(font);
                g.drawString(install.install(), s + 20 + h, y + h - 10);
            }
            int y = Assets.projects.size() * h + (Assets.projects.size() * s);
            setPreferredSize(new Dimension(getPreferredSize().width, y));
        }
    }
}
