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

import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;
import com.xebisco.yield.ini.Ini;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Projects extends JPanel {

    public static JFrame projectsFrame;

    public Projects(JFrame frame) {
        projectsFrame = frame;
        setLayout(new BorderLayout());

        JPanel projectsAndTitlePanel = new JPanel();
        projectsAndTitlePanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Projects");
        title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(40f));
        title.setBorder(BorderFactory.createLineBorder(title.getBackground(), 20));
        projectsAndTitlePanel.add(title, BorderLayout.NORTH);
        JScrollPane projectListScrollPanel = new JScrollPane(new ProjectListPanel());
        projectListScrollPanel.setBorder(null);
        projectsAndTitlePanel.add(projectListScrollPanel, BorderLayout.CENTER);
        JPanel projectsControl = new JPanel();
        projectsControl.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton button = new JButton(new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newProjectFrame(frame);
            }
        });
        projectsControl.add(button);
        frame.getRootPane().setDefaultButton(button);

        button = new JButton(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Load Project");
                fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
                fileChooser.setFileFilter(new FileNameExtensionFilter("Yield Editor Project", "yep"));
                if (fileChooser.showDialog(frame, "Load") == JFileChooser.APPROVE_OPTION) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                        Assets.projects.add((Project) ois.readObject());
                        Projects.this.repaint();
                    } catch (IOException | ClassNotFoundException ex) {
                        Utils.error(frame, ex);
                        Utils.error(frame, new InvalidProjectException("Project might be invalid"));
                    }
                }
            }
        });
        projectsControl.add(button);

        projectsAndTitlePanel.add(projectsControl, BorderLayout.SOUTH);

        add(projectsAndTitlePanel, BorderLayout.CENTER);


        JPanel logoAndOptions = new JPanel();
        logoAndOptions.setLayout(new BorderLayout());
        JLabel logo = new JLabel(Assets.images.get("editorLogoSmall.png"));
        logo.setMaximumSize(new Dimension(100, 100));
        logo.setOpaque(true);
        logoAndOptions.add(logo, BorderLayout.NORTH);
        add(logoAndOptions, BorderLayout.WEST);

        JList<JButton> options = new JList<>(new JButton[]{
                new JButton(new AbstractAction("Projects") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                }),
                new JButton(new AbstractAction("Options") {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                })
        });
        logo.setBackground(options.getBackground());
        options.setCellRenderer(new ButtonListCellRenderer<>());
        options.addListSelectionListener(new ButtonSelectionListener(options));
        options.setSelectedIndex(0);
        options.getSelectedValue().getAction().actionPerformed(null);
        logoAndOptions.add(options, BorderLayout.CENTER);


    }

    public static void saveProjects() {
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(Utils.defaultDirectory() + "/.yield_editor/projects.ser"))) {
            oo.writeObject(Assets.projects);
        } catch (IOException ex) {
            Utils.error(null, ex);
        }
    }

    public static void newProjectFrame(Frame owner) {
        Map<String, Prop[]> sections = new HashMap<>();
        sections.put("New Project", Props.newProject());
        new PropsWindow(sections, () -> {
            Project project = new Project(
                    (String) Objects.requireNonNull(Props.get(sections.get("New Project"), "Project Name")).getValue(),
                    new File((String) Objects.requireNonNull(Props.get(sections.get("New Project"), "Project Location")).getValue(), (String) Objects.requireNonNull(Props.get(sections.get("New Project"), "Project Name")).getValue()));
            if (project.getName().equals("")) {
                Utils.error(null, new IllegalStateException("Project requires a name."));
                newProjectFrame(owner);
            } else
            if (!project.getProjectLocation().getParentFile().exists()) {
                Utils.error(null, new IllegalStateException("Path is not valid."));
                newProjectFrame(owner);
            } else if (!Assets.projects.contains(project)) {
                Assets.projects.add(project);
                project.getProjectLocation().mkdir();
                Image image = Assets.images.get("yieldIcon.png").getImage();
                if(Objects.requireNonNull(Props.get(sections.get("New Project"), "Project Icon")).getValue() != null) {
                    try {
                        image = ImageIO.read(new File((String) Objects.requireNonNull(Props.get(sections.get("New Project"), "Project Icon")).getValue()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                BufferedImage i = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g = i.getGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                try {
                    ImageIO.write(i, "PNG", new File(project.getProjectLocation(), "icon.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                File projectFile = new File(project.getProjectLocation(), "project.yep");
                try {
                    projectFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try(ObjectOutputStream oo = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(projectFile)))) {
                    oo.writeObject(project);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                owner.repaint();
            }
            else Utils.error(null, new IllegalStateException("Project already exists on the projects list"));
        }, owner);
    }
}
