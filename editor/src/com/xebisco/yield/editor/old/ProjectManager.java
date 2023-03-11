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

package com.xebisco.yield.editor.old;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class ProjectManager extends JFrame {
    private final static List<File> projectDirs;
    private final Map<String, JPanel> panelMap = new HashMap<>();
    private JList<Project> projectsJList;
    private List<Project> projects = new ArrayList<>();
    private JScrollPane projectsScroll;
    private JScrollPane starredProjectsScroll;

    static {
        try {
            ObjectInput objectInput = new ObjectInputStream(new FileInputStream(new File(YieldEditor.DATA_DIR, "projectDirs.ser")));
            //noinspection unchecked
            projectDirs = (List<File>) objectInput.readObject();
        } catch (InvalidClassException e) {
            JOptionPane.showMessageDialog(null, "Incompatible Projects", "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose() {
        for (Project project : projects) {
            try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(new File(project.getDirectory(), "project.ser")))) {
                objectOutput.writeObject(project);
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }
        try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(new File(YieldEditor.DATA_DIR, "projectDirs.ser")))) {
            objectOutput.writeObject(projectDirs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.dispose();
    }

    public ProjectManager() {
        for (File projectDir : projectDirs) {
            try (ObjectInput objectInput = new ObjectInputStream(new FileInputStream(new File(projectDir, "project.ser")))) {
                projects.add((Project) objectInput.readObject());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(ProjectManager.this, "This directory does not contain a project", projectDir.getName(), JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e1) {
                JOptionPane.showMessageDialog(ProjectManager.this, "Incompatible Project", projectDir.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
        pack();
        setMinimumSize(new Dimension(400, 200));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Icons.YIELD_ICON.getImage());
        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().height / 1.5), Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        setLocationRelativeTo(null);
        if (projects.size() > 0)
            initUI();
        else
            initWelcomeUI();
    }

    public void reloadProjects() {
        projects.sort(Comparator.comparing(Project::getLastModifiedDate).reversed());
        System.out.println("PROJECTS: " + projects);
        projectsJList.setListData(projects.toArray(new Project[0]));
        projectsScroll.setViewportView(projectsJList);
        JList<Project> starredProjects = new JList<>(projects.stream().filter(Project::isStarred).toList().toArray(new Project[0]));
        starredProjects.setCellRenderer(projectsJList.getCellRenderer());
        starredProjects.addListSelectionListener(projectsJList.getListSelectionListeners()[0]);
        starredProjectsScroll.setViewportView(starredProjects);
        setTitle("Yield Project Manager");
    }

    private void initWelcomeUI() {
        setSize(400, 200);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel);
        JPanel welcomeMessagePanel = new JPanel();
        welcomeMessagePanel.add(new JLabel("Welcome to the"));
        welcomeMessagePanel.add(new JLabel(Icons.EDITOR_LOGO_12dv));
        panel.add(welcomeMessagePanel, BorderLayout.NORTH);
        JPanel startNowPanel = new JPanel();
        JButton startNowButton = new JButton(new AbstractAction("Start Now!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().height / 1.5), Toolkit.getDefaultToolkit().getScreenSize().height / 2);
                setLocationRelativeTo(null);
                setVisible(true);
                initUI();
            }
        });
        startNowPanel.add(startNowButton);
        getRootPane().setDefaultButton(startNowButton);
        panel.add(startNowPanel, BorderLayout.CENTER);
    }

    private void initUI() {
        JPanel projectsMainPanel = new JPanel(new MultiBorderLayout());
        projectsMainPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        JPanel projectsPanelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        projectsPanelButtons.setMinimumSize(new Dimension(100, 100));

        JButton newProjectButton = new JButton(new AbstractAction("New Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewProjectDialog newProjectDialog = new NewProjectDialog(ProjectManager.this);
                newProjectDialog.setVisible(true);
                if (newProjectDialog.getNewProject() != null) {
                    projects.add(newProjectDialog.getNewProject());
                    projectDirs.add(newProjectDialog.getNewProjectDir());
                }
            }
        });

        projectsPanelButtons.add(newProjectButton);

        JButton addProjectButton = new JButton(new AbstractAction("Add Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showDialog(ProjectManager.this, "Add Project") == JFileChooser.APPROVE_OPTION) {
                    Project project;
                    try (ObjectInput objectInput = new ObjectInputStream(new FileInputStream(new File(fileChooser.getSelectedFile(), "project.ser")))) {
                        project = (Project) objectInput.readObject();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(ProjectManager.this, "This directory does not contain a project", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (ClassNotFoundException e1) {
                        JOptionPane.showMessageDialog(ProjectManager.this, "Incompatible Project", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    projectDirs.add(fileChooser.getSelectedFile());
                    projects.add(project);
                    reloadProjects();
                }
            }
        });

        projectsPanelButtons.add(addProjectButton);

        JButton reloadProjectsButton = new JButton(new AbstractAction("Reload Projects") {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadProjects();
            }
        });

        projectsPanelButtons.add(reloadProjectsButton);

        projectsMainPanel.add(projectsPanelButtons, BorderLayout.NORTH);

        JLabel label = new JLabel(" ");
        label.setFont(label.getFont().deriveFont(8f));
        projectsMainPanel.add(label, BorderLayout.NORTH);

        label = new JLabel("Projects");
        label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(32f));
        projectsMainPanel.add(label, BorderLayout.NORTH);

        projectsScroll = new JScrollPane();
        starredProjectsScroll = new JScrollPane();
        JTabbedPane projectsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        projectsTabbedPane.addTab("All Projects", projectsScroll);
        projectsTabbedPane.addTab("Starred Projects", starredProjectsScroll);
        projectsJList = new JList<>();
        projectsJList.setCellRenderer(new ProjectListCellRenderer());
        ListSelectionListener listSelectionListener = e -> {
            //noinspection unchecked
            JList<Project> list = (JList<Project>) e.getSource();
                if (list.getSelectedValue() != null) {
                    JPopupMenu menu = new JPopupMenu();
                    menu.add(new JMenuItem(new AbstractAction(list.getSelectedValue().getProjectName()) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            reloadProjects();
                        }
                    }));
                    menu.addSeparator();
                    menu.add(new JMenuItem(new AbstractAction("Open this Project") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            list.getSelectedValue().setLastModifiedDate(new Date());
                            dispose();
                            new YieldEditor(list.getSelectedValue()).setVisible(true);
                        }
                    }));
                    String star = "Star";
                    if (list.getSelectedValue().isStarred()) star = "Un-star";
                    menu.add(new JMenuItem(new AbstractAction(star + " this Project") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            list.getSelectedValue().setStarred(!list.getSelectedValue().isStarred());
                            reloadProjects();
                        }
                    }));
                    menu.add(new JMenuItem(new AbstractAction("Remove from list") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            projectDirs.remove(list.getSelectedValue().getDirectory());
                            projects.remove(list.getSelectedValue());
                            reloadProjects();
                        }
                    }));
                    menu.addPopupMenuListener(new PopupMenuListener() {
                        @Override
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                        }

                        @Override
                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

                        }

                        @Override
                        public void popupMenuCanceled(PopupMenuEvent e) {
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    list.setSelectedValue(null, false);
                                    timer.cancel();
                                }
                            }, 50);
                        }
                    });
                    menu.show(ProjectManager.this, MouseInfo.getPointerInfo().getLocation().x - ProjectManager.this.getX(), MouseInfo.getPointerInfo().getLocation().y - ProjectManager.this.getY());
                }
        };
        projectsJList.addListSelectionListener(listSelectionListener);
        reloadProjects();
        projectsMainPanel.add(projectsTabbedPane, BorderLayout.CENTER);

        JPanel settingsMainPanel = new JPanel(new BorderLayout(20, 20));
        JPanel settingsPanelButtons = new JPanel();

        settingsPanelButtons.add(new JButton("settings"));

        settingsMainPanel.add(settingsPanelButtons, BorderLayout.NORTH);

        settingsMainPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.LINE_START);


        JPanel enginesMainPanel = new JPanel(new MultiBorderLayout());

        label = new JLabel("Installed Engines");
        label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(32f));
        enginesMainPanel.add(label, BorderLayout.NORTH);

        panelMap.put("Projects", projectsMainPanel);

        panelMap.put("Settings", settingsMainPanel);

        panelMap.put("Installed Engines", enginesMainPanel);

        JPanel main = new JPanel(new BorderLayout());
        setContentPane(main);

        JList<String> editorExtras = new JList<>(new String[]{"Projects", "Settings", "Installed Engines"});
        String[] last = new String[1];
        editorExtras.addListSelectionListener(e -> {
            if (last[0] != null)
                main.remove(panelMap.get(last[0]));
            main.add(panelMap.get(editorExtras.getSelectedValue()), BorderLayout.CENTER);
            last[0] = editorExtras.getSelectedValue();
            main.revalidate();
            main.repaint();
        });
        editorExtras.setFixedCellHeight(50);
        editorExtras.setCellRenderer(new BigListCellRenderer());
        JSplitPane editorLogoAndEditorExtras = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        editorLogoAndEditorExtras.setTopComponent(new JLabel(Icons.EDITOR_LOGO_12dv));
        editorLogoAndEditorExtras.setBottomComponent(editorExtras);
        editorExtras.setSelectedIndex(0);
        main.add(editorLogoAndEditorExtras, BorderLayout.WEST);
    }

    public Map<String, JPanel> getPanelMap() {
        return panelMap;
    }

    public static List<File> getProjectDirs() {
        return projectDirs;
    }

    public JList<Project> getProjectsJList() {
        return projectsJList;
    }

    public void setProjectsJList(JList<Project> projectsJList) {
        this.projectsJList = projectsJList;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public JScrollPane getProjectsScroll() {
        return projectsScroll;
    }

    public void setProjectsScroll(JScrollPane projectsScroll) {
        this.projectsScroll = projectsScroll;
    }
}
