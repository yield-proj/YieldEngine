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

public class ProjectListPanel extends JPanel implements MouseListener {

    private final List<ProjectPos> projectsPos = new ArrayList<>();
    private Project selectedProject;

    public ProjectListPanel() {
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color brighterBkg = getBackground().brighter();
        int h = 80, s = 10;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (Assets.projects.size() == 0) {
            g.setColor(getForeground());
            String noProject = "No projects loaded.";
            int sw = g.getFontMetrics().stringWidth(noProject);
            g.drawString(noProject, getWidth() / 2 - sw / 2, g.getFont().getSize());
        } else {
            projectsPos.clear();
            for (int i = 0; i < Assets.projects.size(); i++) {
                Project project = Assets.projects.get(i);
                if (project == selectedProject) {
                    g.setColor(UIManager.getColor("List.selectionBackground"));
                } else
                    g.setColor(brighterBkg);
                int y = i * h + (i * s), w = getWidth() - s * 2;
                projectsPos.add(new ProjectPos(project, getLocationOnScreen().y + y));
                g.fillRoundRect(s, y, w, h, 10, 10);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        int w = getWidth();

        selectedProject = null;

        for (ProjectPos pp : projectsPos) {
            if (mousePosition.y >= pp.getPosition() && mousePosition.y <= pp.getPosition() + 80) {
                selectedProject = pp.getProject();
                break;
            }
        }
        repaint();
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
