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
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class YieldEditor extends JFrame {
    public static final File DATA_DIR;

    static {
        String home = System.getProperty("os.home");
        if (System.getProperty("os.name").contains("Windows")) {
            home = System.getenv("APPDATA");
        }
        DATA_DIR = new File(home, "YieldEditorData");
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdir();
            try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(new File(DATA_DIR, "projectDirs.ser")))) {
                objectOutput.writeObject(new ArrayList<File>());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            File enginesDir = new File(DATA_DIR, "engines");
            enginesDir.mkdir();
            //TODO
            try {
                new File(enginesDir, "yield@noneV.jar").createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void menuBar() {
        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new JMenuItem(new AbstractAction("Open...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().equals("project.ser");
                    }

                    @Override
                    public String getDescription() {
                        return "Serialized Project (project.ser)";
                    }
                });
                if(fileChooser.showOpenDialog(YieldEditor.this) == JFileChooser.APPROVE_OPTION) {
                    Project project;
                    try(ObjectInput objectInput = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                        project = (Project) objectInput.readObject();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(YieldEditor.this, "Incompatible Project", "", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                    OpenProjectDialog openProjectDialog = new OpenProjectDialog(YieldEditor.this);
                    openProjectDialog.setVisible(true);
                    switch (openProjectDialog.getResponse()) {
                        case NEW -> new YieldEditor(project).setVisible(true);
                        case THIS -> {
                            dispose();
                            new YieldEditor(project).setVisible(true);
                        }
                    }
                }
            }
        }));
        fileMenu.add(new JMenuItem(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ProjectManager().setVisible(true);
            }
        }));

        menuBar.add(fileMenu);


        setJMenuBar(menuBar);
    }

    public YieldEditor(Project project) {
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(Icons.YIELD_ICON.getImage());
        setTitle(project.getProjectName() + " - Yield Editor");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        menuBar();
    }
}
