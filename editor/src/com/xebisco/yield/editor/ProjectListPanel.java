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

public class ProjectListPanel extends JPanel implements MouseListener {

    private final List<ProjectPos> projectsPos = new ArrayList<>();
    private Project selectedProject;

    public ProjectListPanel() {
        addMouseListener(this);
    }

    private final Map<Project, BufferedImage> imageMap = new HashMap<>();

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
            Font font = g.getFont();
            Font bigFont = font.deriveFont(font.getSize() + 10f).deriveFont(Font.BOLD);
            for (int i = 0; i < Assets.projects.size(); i++) {
                Project project = Assets.projects.get(i);
                if (project == selectedProject) {
                    g.setColor(UIManager.getColor("List.selectionBackground"));
                } else
                    g.setColor(brighterBkg);
                int y = i * h + (i * s), w = getWidth() - s;
                projectsPos.add(new ProjectPos(project, y));
                g.fillRoundRect(s, y, w, h, 20, 20);
                g.fillRect(w, y, s, h);
                BufferedImage image = imageMap.get(project);
                if (image == null) {
                    try {
                        Image img = ImageIO.read(new File(project.getProjectLocation(), "icon.png"));
                        img = img.getScaledInstance(h, (int) (h * ((double) img.getHeight(null) / img.getWidth(null))), Image.SCALE_SMOOTH);
                        image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                        Graphics g1 = image.getGraphics();
                        g1.drawImage(img, 0, 0, image.getWidth(), image.getHeight(), null);
                        g1.dispose();
                        imageMap.put(project, image);
                    } catch (IOException e) {
                        Assets.projects.remove(project);
                        break;
                    }
                }
                g.drawImage(image, s + 10, y + h / 2 - image.getHeight() / 2, null);
                g.setColor(getForeground());
                g.setFont(bigFont);
                g.drawString(project.getName(), s + 20 + h, y + bigFont.getSize() + 10);
                g.setFont(font);
                g.drawString(project.getProjectLocation().toString(), s + 20 + h, y + h - 10);
                g.setColor(getForeground().darker());
                g.fillOval(w - 10, y + h / 2 - 2, 4, 4);
                g.fillOval(w - 16, y + h / 2 - 2, 4, 4);
                g.fillOval(w - 22, y + h / 2 - 2, 4, 4);
            }
            int y = Assets.projects.size() * h + (Assets.projects.size() * s);
            setPreferredSize(new Dimension(getPreferredSize().width, y));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedProject = null;

        for (ProjectPos pp : projectsPos) {
            if (e.getY() >= pp.getPosition() && e.getY() <= pp.getPosition() + 80) {
                selectedProject = pp.getProject();
                break;
            }
        }

        if (selectedProject != null && e.getX() > getWidth() / 2) {
            JPopupMenu popupMenu = new JPopupMenu(selectedProject.getName());
            JMenuItem item = new JMenuItem(new AbstractAction("Open") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (Projects.checkInstalls()) {
                        Projects.projectsFrame.dispose();
                        new Editor(selectedProject);
                    } else {
                        JOptionPane.showMessageDialog(null, "There is no engine installed on this machine");
                        Projects.editInstalls();
                    }
                }
            });
            popupMenu.add(item);

            item = new JMenuItem(new AbstractAction("Remove") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Assets.projects.remove(selectedProject);
                    repaint();
                }
            });
            popupMenu.add(item);
            popupMenu.show(this, getWidth() - 80, e.getY());
            repaint();
        }

        repaint();
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
