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

package com.xebisco.yieldengine.editor.app;

import com.xebisco.yieldengine.editor.app.editor.ActionsHandler;
import com.xebisco.yieldengine.editor.runtime.pack.EditorComponent;
import com.xebisco.yieldengine.editor.runtime.pack.EditorEntity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.*;

public class FileBrowser extends JPanel {

    private final DefaultMutableTreeNode root;

    private final DefaultTreeModel treeModel;

    private final JTree tree;
    private final FileFilter fileFilter;
    private final ActionsHandler actionsHandler;

    public interface OpenFileAction {
        void open(File file);
    }

    public FileBrowser(FileFilter fileFilter, File fileRoot, OpenFileAction openFileAction, ActionsHandler actionsHandler) {
        super(new BorderLayout());
        this.actionsHandler = actionsHandler;
        this.fileFilter = fileFilter;
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setBorder(null);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel s = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (((FileNode) ((DefaultMutableTreeNode) value).getUserObject()).file.isFile()) {
                    s.setIcon(UIManager.getIcon("FileView.fileIcon"));
                } else {
                    s.setIcon(UIManager.getIcon("FileView.directoryIcon"));
                }
                return s;
            }
        });
        tree.setShowsRootHandles(true);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    File[] files = es();
                    for (File file : files)
                        if (!file.isDirectory())
                            openFileAction.open(file);
                } else {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        File[] files = es();
                        JPopupMenu popupMenu = new JPopupMenu();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < files.length; i++) {
                            sb.append(files[i].getName());
                            if (i < files.length - 1) sb.append(",");
                        }
                        popupMenu.add(new JLabel(sb.toString()));
                        popupMenu.addSeparator();
                        //popupMenu.add(addMenu(entities[0].children(), entities[0], null));
                        popupMenu.add(new AbstractAction("Open") {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (File file : files)
                                    openFileAction.open(file);
                            }
                        });
                    /*popupMenu.add(new AbstractAction("Duplicate") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(() -> {
                                for (EditorEntity en : entities) {
                                    EditorEntity n = Global.clearComponents(new EditorEntity(), editor).setEntityName(en.entityName() + " (Clone)").setParent(en.parent()).setPrefabFile(en.prefabFile());
                                    n.components().clear();
                                    for (EditorComponent c : en.components())
                                        n.components().add(Global.sameAs(c, editor));

                                    List<EditorEntity> entities1;
                                    if (en.parent() == null) entities1 = scene.entities();
                                    else entities1 = en.parent().children();
                                    scenePanelAH.push(new ActionsHandler.ActionHA("Duplicate Entity(" + en.entityName() + ")", () -> {
                                        entities1.add(n);
                                        entitiesTree.tree.update();
                                    }, () -> {
                                        entities1.remove(n);
                                        entitiesTree.tree.update();
                                    }));
                                }
                            });
                        }
                    });*/
                        popupMenu.add(new AbstractAction("Delete") {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (File file : files)
                                    Global.deleteDir(file);
                                update();
                            }
                        });
                        popupMenu.show(tree, tree.getMousePosition().x, tree.getMousePosition().y);
                    }
                }
            }
        });
        add(tree);
        update();
    }

    private File[] es() throws ClassCastException {
        if (tree.getSelectionPaths() == null || tree.getSelectionPaths().length == 0) return new File[0];
        File[] files = new File[tree.getSelectionPaths().length];
        for (int i = 0; i < files.length; i++) {
            files[i] = ((FileNode) ((DefaultMutableTreeNode) tree.getSelectionPaths()[i].getLastPathComponent()).getUserObject()).file;
        }
        return files;
    }

    /*public static void main(String[] args) {
        FlatDarkLaf.setup();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new FileBrowser(pathname -> true, new File(".")));
        frame.pack();
        frame.setVisible(true);
    }*/

    public void update() {
        root.removeAllChildren();
        CreateChildNodes ccn = new CreateChildNodes(((FileNode) root.getUserObject()).file, root);
        ccn.run();
        tree.updateUI();
        tree.expandRow(0);
    }

    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        public CreateChildNodes(File fileRoot, DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }

        private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
            ArrayList<File> files = new ArrayList<>(List.of(Objects.requireNonNull(fileRoot.listFiles())));
            files.sort(Comparator.comparing(file -> file.getName().toUpperCase()));
            files.sort(Comparator.comparing(File::isFile));

            for (File file : files) {
                if (!fileFilter.accept(file)) continue;
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
                node.add(childNode);
                if (file.isDirectory()) {
                    createChildren(file, childNode);
                }
            }
        }

    }

    public static class FileNode {

        private final File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.isEmpty()) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }

}