package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewProjectDialog extends JDialog {

    private Project newProject;
    private File newProjectDir;

    public NewProjectDialog(Frame owner) {
        super(owner, true);
        setSize(new Dimension(400, 210));
        setResizable(false);
        setLocationRelativeTo(owner);
        setTitle("New Project");
        JPanel main2 = new JPanel(new BorderLayout());

        JPanel fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(30, 10));
        main2.add(fillPanel, BorderLayout.NORTH);
        fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(30, 40));
        main2.add(fillPanel, BorderLayout.WEST);
        fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(30, 40));
        main2.add(fillPanel, BorderLayout.EAST);
        fillPanel = new JPanel();
        fillPanel.setPreferredSize(new Dimension(30, 10));
        main2.add(fillPanel, BorderLayout.SOUTH);

        main2.setOpaque(true);
        JPanel main = new JPanel(new GridLayout(4, 2, 0, 10));
        main2.setMaximumSize(new Dimension(400, 100));
        main.add(new JLabel("Project path:"));
        JPanel pathPanel = new JPanel(new BorderLayout());
        final JTextField path = new JTextField();
        path.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    //noinspection unchecked
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        System.out.println(file);
                        path.setText(file.getAbsolutePath());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        pathPanel.add(path, BorderLayout.CENTER);
        JButton pathButton = new JButton(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(fileChooser.showDialog(NewProjectDialog.this, null) == JFileChooser.APPROVE_OPTION) {
                    path.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        pathButton.setIcon(Icons.UPLOAD_ICON_16x16);
        pathPanel.add(pathButton, BorderLayout.EAST);
        main.add(pathPanel);
        main.add(new JLabel("Project name:"));
        JTextField projectNameTextField = new JTextField();
        main.add(projectNameTextField);
        main.add(new JLabel("Create standard folders:"));
        JCheckBox stdFoldersCheckBox = new JCheckBox();
        stdFoldersCheckBox.setSelected(true);
        main.add(stdFoldersCheckBox);
        main.add(new JLabel("Engine version:"));
        JComboBox<String> enginesComboBox = new JComboBox<>();
        for (File file : Objects.requireNonNull(new File(YieldEditor.DATA_DIR, "engines").listFiles())) {
            Matcher matcher = Pattern.compile("yield@([^}]+).jar").matcher(file.getName());
            matcher.matches();
            enginesComboBox.addItem(matcher.group(1));
        }
        main.add(enginesComboBox);
        main2.add(main, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));
        JButton createButton = new JButton(new AbstractAction("Create") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Project project = new Project();
                project.setProjectName(projectNameTextField.getText());
                project.setEngineVersion((String) enginesComboBox.getSelectedItem());
                project.setDirectory(new File(path.getText()));
                setNewProject(project);
                setNewProjectDir(project.getDirectory());
                File projectSerFile = new File(project.getDirectory(), "project.ser");
                try {
                    projectSerFile.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(projectSerFile))) {
                    objectOutput.writeObject(project);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
                if(stdFoldersCheckBox.isSelected()) {
                    new File(project.getDirectory(), "Scripts").mkdir();
                    new File(project.getDirectory(), "Textures").mkdir();
                    new File(project.getDirectory(), "Prefabs").mkdir();
                    new File(project.getDirectory(), "Materials").mkdir();
                }
            }
        });
        bottomPanel.add(createButton);
        getRootPane().setDefaultButton(createButton);

        add(bottomPanel, BorderLayout.SOUTH);

        add(main2, BorderLayout.CENTER);
    }

    public static JPanel panel(Component... components) {
        JPanel panel = new JPanel();
        for (Component component : components)
            panel.add(component);
        return panel;
    }

    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(Project newProject) {
        this.newProject = newProject;
    }

    public File getNewProjectDir() {
        return newProjectDir;
    }

    public void setNewProjectDir(File newProjectDir) {
        this.newProjectDir = newProjectDir;
    }
}
