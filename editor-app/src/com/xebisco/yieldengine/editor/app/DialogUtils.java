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

import com.xebisco.yieldengine.editor.app.editor.ComponentProp;
import com.xebisco.yieldengine.editor.app.editor.Editor;
import com.xebisco.yieldengine.editor.runtime.pack.EditorComponent;
import com.xebisco.yieldengine.uiutils.Srd;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.xebisco.yieldengine.editor.app.editor.ScenePanel.getComponentClasses;

public class DialogUtils {

    public static void error(Component component, Throwable e) {
        //noinspection CallToPrintStackTrace
        e.printStackTrace();
        JOptionPane.showMessageDialog(component, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }

    public static Class<?> addComponent(Editor editor) {
        AtomicReference<Class<?>> componentClass = new AtomicReference<>();
        JDialog addComponentDialog = new JDialog(editor, true);
        addComponentDialog.add(new TitleLabel(Srd.LANG.getProperty("ce_addComponent"), null), BorderLayout.NORTH);
        addComponentDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addComponentDialog.setSize(500, 350);
        addComponentDialog.setLocationRelativeTo(editor);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Components");
        TreeModel treeModel = new DefaultTreeModel(root);

        JTree tree = new JTree(treeModel);

        JList<Class<?>> components;

        try {
            Class<?>[] componentsClasses = getComponentClasses(editor.yieldEngineClassLoader, "com.xebisco.yieldengine", editor).toArray(new Class<?>[0]);
            List<Class<?>> componentsList = new ArrayList<>(Arrays.asList(componentsClasses));
            List<File> files = new ArrayList<>();
            for(File f : Global.listf(editor.project().buildDirectory())) {
                if(f.getName().endsWith(".class")) {
                    files.add(f);
                }
            }
            URLClassLoader classLoader = new URLClassLoader(new URL[] {editor.project().buildDirectory().toURI().toURL()});

            for(File f : files) {
                Class<?> compClass = classLoader.loadClass(f.getAbsolutePath().replace(editor.project().buildDirectory().getAbsolutePath(), "").replace(File.separator, ".").replace(".class", "").substring(1));
                //if(Global.containsAnnotationByName(compClass, ""))
                componentsList.add(compClass);
            }
            components = new JList<>(componentsList.toArray(new Class<?>[0]));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }



        components.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        components.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && components.getSelectedValue() != null) {
                    componentClass.set(components.getSelectedValue());
                    addComponentDialog.dispose();
                }
            }
        });

        components.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setText(((Class<?>) value).getSimpleName());
                l.setHorizontalAlignment(CENTER);
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                try {
                    l.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(ComponentProp.class.getResource("/logo/codeIcon.png"))).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return l;
            }
        });
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(null);
        tree.setBackground(editor.getBackground());
        addComponentDialog.add(scrollPane);
        addComponentDialog.setVisible(true);
        return componentClass.get();
    }
}
