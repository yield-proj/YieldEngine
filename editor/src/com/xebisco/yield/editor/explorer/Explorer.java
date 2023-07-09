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

package com.xebisco.yield.editor.explorer;

import com.formdev.flatlaf.ui.FlatBorder;
import com.sun.source.tree.Tree;
import com.xebisco.yield.editor.Assets;
import com.xebisco.yield.editor.PropsWindow;
import com.xebisco.yield.editor.Utils;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;
import com.xebisco.yield.editor.prop.StringProp;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Explorer extends JPanel implements ActionListener {
    private JTextArea jta;
    private JTree tree;
    private JButton refresh;
    private JTable jtb;
    private JScrollPane jsp;

    File currDirectory = null;

    final String[] colHeads = {"File Name", "SIZE(in Bytes)", "Read Only", "Hidden"};
    String[][] data = {{"", "", "", "", ""}};

    private final File mainDir;

    public Explorer(File mainDir) {
        this.mainDir = mainDir;

        setLayout(new BorderLayout());

        jta = new JTextArea(5, 30);
        refresh = new JButton(Assets.images.get("reloadIcon.png"));
        JButton newFolder = new JButton(UIManager.getIcon("Tree.closedIcon"));

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Project Explorer", UIManager.getIcon("FileChooser.homeFolderIcon"), SwingConstants.CENTER));
        p.add(refresh);
        p.add(new JButton(new AbstractAction("", UIManager.getIcon("Tree.closedIcon")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currDirectory == null) {
                    JOptionPane.showMessageDialog(null, "This requires to select a directory");
                } else {
                    Map<String, Prop[]> props = new HashMap<>();
                    props.put("New Directory", new Prop[]{new StringProp("Name", "")});
                    new PropsWindow(props, () -> {
                        new File(currDirectory, (String) Objects.requireNonNull(Props.get(props.get("New Directory"), "Name")).getValue()).mkdir();
                        Explorer.this.actionPerformed(null);
                    }, null, "New Directory");
                }
            }
        }));
        add(p, BorderLayout.NORTH);

        actionPerformed(null);

        refresh.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev) {
        DefaultMutableTreeNode newtop = createTree(mainDir);
        if (newtop != null) {
            tree = new JTree(newtop);
            tree.setCellRenderer(new ExplorerCellRenderer(mainDir));
        }
        if (jsp != null)
            remove(jsp);
        jsp = new JScrollPane(tree);
        add(jsp, BorderLayout.CENTER);
        tree.addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu popupMenu = new JPopupMenu();

                            File f = fPath(tree.getSelectionModel().getLeadSelectionPath(), mainDir);

                            popupMenu.add(new JMenuItem(new AbstractAction("Open" + (f.isDirectory() ? " in Explorer" : "")) {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (JOptionPane.showConfirmDialog(null, "Open " + f.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                        try {
                                            Desktop.getDesktop().open(f);
                                        } catch (IOException ex) {
                                            Utils.error(null, ex);
                                        }
                                    }
                                }
                            }));

                            popupMenu.show(Explorer.this, me.getX() + 2, me.getY() + 42);
                        } else
                            doMouseClicked(me);
                    }
                });
    }

    DefaultMutableTreeNode createTree(File temp) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Project Root");
        if (!(temp.exists() && temp.isDirectory()))
            return top;

        fillTree(top, temp.getPath());

        return top;
    }

    void fillTree(DefaultMutableTreeNode root, String filename) {
        File temp = new File(filename);

        if (!(temp.exists() && temp.isDirectory()))
            return;
        File[] filelist = temp.listFiles();

        for (File file : filelist) {
            final DefaultMutableTreeNode tempDmtn = new DefaultMutableTreeNode(file.getName());

            root.add(tempDmtn);
            final String newfilename = new String(filename + "\\" + file.getName());
            fillTree(tempDmtn, newfilename);
        }
    }

    public static File fPath(TreePath tp, File mainDir) {
        if (tp == null)
            return null;
        String s = tp.toString().substring(1, tp.toString().length() - 1);
        StringBuilder m = new StringBuilder();

        String[] spl = s.split(", ");

        for (int i = 1; i < spl.length; i++) {
            if (i > 1)
                m.append("\\");
            m.append(spl[i]);
        }

        return new File(mainDir, m.toString());
    }

    void doMouseClicked(MouseEvent me) {
        TreePath tp = tree.getSelectionModel().getLeadSelectionPath();
        if (tp == null) return;

        if (tp.getParentPath() == null) {
            currDirectory = mainDir;
            return;
        }

        showFiles(fPath(tp, mainDir));
    }

    void showFiles(File file) {
        if (file.isDirectory()) {
            currDirectory = file;
        }
    }
////////////////////////////////  
///////////////////////////////  
}