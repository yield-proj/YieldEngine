/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yield.tileeditor.app;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.xebisco.yield.uiutils.PrettyListCellRenderer;
import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.props.Prop;
import com.xebisco.yield.uiutils.props.PropPanel;
import com.xebisco.yield.uiutils.props.SizeProp;
import com.xebisco.yield.uiutils.props.TextFieldProp;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProjectEditor extends JFrame {
    public static List<File> PROJECT_FILES;
    public static List<Project> PROJECT_OBJECTS = new ArrayList<>();
    private final File workspaceFile;

    private static void saveWorkspace(File workspaceFile, List<File> projects) {
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(workspaceFile))) {
            oo.writeObject(projects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createWorkspace(File workspaceDir) throws IOException {
        File workspaceFile = new File(workspaceDir, "workspace.ser");
        if (workspaceFile.exists()) {
            JOptionPane.showMessageDialog(null, "Workspace already exists");
            return;
        }
        workspaceFile.createNewFile();
        saveWorkspace(workspaceFile, new ArrayList<>());
    }

    public static void loadProjects() {
        PROJECT_OBJECTS.clear();
        for(File p : PROJECT_FILES) {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(p))) {
                //noinspection unchecked
                PROJECT_OBJECTS.add(((Project) oi.readObject()).setPath(p));
            } catch (Exception e) {
                PROJECT_FILES.remove(p);
                System.err.println(e.getMessage());
            }
        }
    }

    private void openProject(Project project) {
        dispose();
        new MapEditor(project);
    }

    public ProjectEditor(File workspaceFile) {
        this.workspaceFile = workspaceFile;
        if (!workspaceFile.exists()) {
            throw new IllegalStateException("NON EXISTENT WORKSPACE");
        } else {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(workspaceFile))) {
                //noinspection unchecked
                PROJECT_FILES = (List<File>) oi.readObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveWorkspace(workspaceFile, PROJECT_FILES);
        }));

        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(ProjectEditor.class.getResourceAsStream("/logo/logo.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setTitle(Srd.LANG.getProperty("tile_editor_projects"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500, 300));
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout()), leftPanel = new JPanel(new BorderLayout());

        loadProjects();

        ProjectsModel model = new ProjectsModel(PROJECT_OBJECTS);
        JTable table = new JTable(model);
        table.setDefaultRenderer(Date.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return super.getTableCellRendererComponent(table, DateFormat.getDateInstance(DateFormat.DEFAULT).format(value), isSelected, hasFocus, row, column);
            }
        });
        table.getColumn("Name").setPreferredWidth(200);
        table.setRowHeight(50);


        /*table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return new PrettyLabel().setText(String.valueOf(value));
            }
        });*/

        TableRowSorter<ProjectsModel> sorter = new TableRowSorter<>(model);
        sorter.setSortable(0, false);
        sorter.setSortable(1, true);
        sorter.setComparator(1, Comparator.naturalOrder());
        sorter.toggleSortOrder(1);
        sorter.toggleSortOrder(1);
        table.setRowSorter(sorter);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            boolean popup;

            @Override
            public void mousePressed(MouseEvent e) {
                popup = e.isPopupTrigger();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) popup = true;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int p = table.rowAtPoint(e.getPoint());
                if (p < 0) return;
                table.requestFocus();
                table.clearSelection();
                Project project = PROJECT_OBJECTS.get(p);
                if (popup) {
                    JPopupMenu popupMenu = new JPopupMenu(project.name());
                    popupMenu.add(new AbstractAction("Open") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            openProject(project);
                        }
                    });
                    popupMenu.add(new AbstractAction("Remove From List") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            PROJECT_FILES.remove(project.path());
                            loadProjects();
                            sorter.allRowsChanged();
                            table.updateUI();
                        }
                    });
                    popupMenu.show(table, e.getX(), e.getY());
                    table.addRowSelectionInterval(p, p);
                } else {
                    openProject(project);
                }
            }
        });

        JScrollPane mainScrollPane = new JScrollPane(table);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(4);
        mainScrollPane.setBorder(BorderFactory.createLineBorder(table.getGridColor()));

        mainPanel.add(mainScrollPane, BorderLayout.CENTER);

        mainPanel.add(topPanel(sorter, () -> {
            final boolean[] err = {false};
            do {
                JDialog newProjectDialog = new JDialog(ProjectEditor.this, true);
                newProjectDialog.setTitle("New Project");
                Prop[] props = new Prop[]{
                        new TextFieldProp("Project Name", "Sample Name"),
                        new SizeProp("Map Grid Size", new Dimension(100, 100)),
                        new TextFieldProp("Entity Creation Class", "EntityCreation"),
                };
                PropPanel newProjectProps = new PropPanel(props);
                newProjectProps.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
                newProjectDialog.add(newProjectProps);
                newProjectDialog.setMinimumSize(new Dimension(450, 300));
                newProjectDialog.setLocationRelativeTo(ProjectEditor.this);

                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                JButton finish = new JButton(new AbstractAction("Finish") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Serializable> values = PropPanel.values(props);
                        String s = (String) values.get("Project Name");
                        String ec = (String) values.get("Entity Creation Class");
                        Dimension d = (Dimension) values.get("Map Grid Size");
                        if (s.isEmpty()) {
                            err[0] = false;
                            JOptionPane.showMessageDialog(ProjectEditor.this, "Project Name must not be empty");
                            return;
                        }
                        AtomicBoolean alreadyExists = new AtomicBoolean(false);
                        PROJECT_OBJECTS.forEach(p -> {
                            if (p.name().equals(s)) {
                                alreadyExists.set(true);
                            }
                        });
                        if (alreadyExists.get()) {
                            err[0] = false;
                            JOptionPane.showMessageDialog(ProjectEditor.this, "A project with this name already exists");
                            return;
                        }
                        try {
                            File projectFile = new File(workspaceFile.getParentFile(), s);
                            PROJECT_FILES.add(projectFile);
                            PROJECT_OBJECTS.add(Project.createProject(s, d, ec, projectFile).setPath(projectFile));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        sorter.allRowsChanged();
                        table.updateUI();
                        newProjectDialog.dispose();
                    }
                });
                newProjectDialog.getRootPane().setDefaultButton(finish);
                bottom.add(finish);
                bottom.add(new JButton(new AbstractAction("Cancel") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        err[0] = false;
                        newProjectDialog.dispose();
                    }
                }));
                newProjectDialog.add(bottom, BorderLayout.SOUTH);

                newProjectDialog.setVisible(true);
            } while (err[0]);
        }, () -> {
            JFileChooser fileChooser = new JFileChooser(workspaceFile.getAbsolutePath());
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".ser");
                }

                @Override
                public String getDescription() {
                    return "Serialized Files";
                }
            });
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (fileChooser.showOpenDialog(ProjectEditor.this) == JFileChooser.APPROVE_OPTION) {
                try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                    Project p = ((Project) oi.readObject()).setLastModified(new Date());
                    if (PROJECT_OBJECTS.contains(p)) {
                        JOptionPane.showMessageDialog(fileChooser, "This project is already imported", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        PROJECT_FILES.add(fileChooser.getSelectedFile());
                        loadProjects();
                        sorter.allRowsChanged();
                        table.updateUI();
                    }
                } catch (IOException | ClassNotFoundException | ClassCastException e) {
                    JOptionPane.showMessageDialog(fileChooser, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                }

            }
        }, () -> {
            JDialog dialog = new JDialog(ProjectEditor.this, true);
            dialog.setTitle("Workspace");
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            dialog.setMinimumSize(new Dimension(250, 140));

            JLabel label = new JLabel(workspaceFile.getParentFile().getAbsolutePath());
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setIcon(FileSystemView.getFileSystemView().getSystemIcon(workspaceFile.getParentFile()));

            dialog.add(label);

            JPanel bottom = new JPanel();
            JButton changeButton;
            bottom.add(changeButton = new JButton(new AbstractAction("Change Workspace") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Entry.createWorkspace();
                        Dimension size = ProjectEditor.this.getSize();
                        ProjectEditor editor = new ProjectEditor(new File(G.appProps.getProperty("lastWorkspace"), "workspace.ser"));
                        editor.setSize(size);
                        editor.setLocationRelativeTo(ProjectEditor.this);
                        ProjectEditor.this.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }));
            dialog.add(bottom, BorderLayout.SOUTH);
            dialog.getRootPane().setDefaultButton(changeButton);

            dialog.pack();

            dialog.setLocationRelativeTo(ProjectEditor.this);
            dialog.setVisible(true);
        }), BorderLayout.NORTH);


        //LEFT


        JList<String> pages = new JList<>(new String[]{"Projects", "TileMaps"});
        pages.setCellRenderer(new PrettyListCellRenderer<>());

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, mainPanel);

        pages.addListSelectionListener(e -> {
            switch (pages.getSelectedValue()) {
                case "Projects" -> {
                    mainSplitPane.setRightComponent(mainPanel);
                    mainSplitPane.updateUI();
                }
                case "TileMaps" -> {
                    mainSplitPane.setRightComponent(new JPanel());
                    mainSplitPane.updateUI();
                }
            }
        });

        pages.setSelectedIndex(0);

        JLabel titleLabel = new JLabel("<html><h1>Yield Tile Editor</h1><p>" + G.VERSION + "</p></html>");

        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setBackground(pages.getBackground());

        titleLabel.setOpaque(true);

        leftPanel.add(titleLabel, BorderLayout.NORTH);

        leftPanel.add(pages);

        add(mainSplitPane);

        setVisible(true);
        mainSplitPane.setDividerLocation(.3);
    }

    private JPanel topPanel(TableRowSorter<?> sorter, Runnable newAction, Runnable importAction, Runnable workspaceAction) {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(100, 30));
        //searchField.setMaximumSize(new Dimension(100, 30));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)(" + searchField.getText().trim() + ")", 0));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)(" + searchField.getText().trim() + ")", 0));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)(" + searchField.getText().trim() + ")", 0));
            }
        });

        top.add(new JLabel("Search: "));
        top.add(searchField);
        top.add(Box.createHorizontalStrut(12));
        top.add(Box.createGlue());

        if (workspaceAction != null) {
            top.add(new JButton(new AbstractAction("Workspace") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    workspaceAction.run();
                }
            }));
            top.add(Box.createHorizontalStrut(6));
        }

        top.add(new JButton(new AbstractAction("Import") {
            @Override
            public void actionPerformed(ActionEvent e) {
                importAction.run();
            }
        }));

        top.add(Box.createHorizontalStrut(6));

        top.add(new JButton(new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newAction.run();
            }
        }));
        return top;
    }

    private static final class ProjectsModel extends AbstractTableModel {

        private static final String[] COLUMNS = {"Name", "Modified"};

        private final List<Project> contents;

        ProjectsModel(List<Project> contents) {
            this.contents = contents;
        }

        @Override
        public int getRowCount() {
            return contents.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> String.class;
                case 1 -> Date.class;
                default -> null;
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> contents.get(rowIndex).name();
                case 1 -> contents.get(rowIndex).lastModified();
                default -> null;
            };
        }
    }
}
