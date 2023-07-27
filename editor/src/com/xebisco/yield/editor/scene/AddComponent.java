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

package com.xebisco.yield.editor.scene;

import com.xebisco.yield.editor.Editor;
import com.xebisco.yield.editor.EngineInstall;
import com.xebisco.yield.editor.Utils;
import com.xebisco.yield.editor.YieldInternalFrame;
import com.xebisco.yield.editor.prop.ComponentProp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class AddComponent extends JPanel implements MouseListener {
    private final List<ComponentProp> props;
    private Class<?> selectedClass;
    private File selectedFile;
    private final YieldInternalFrame frame;

    private final EngineInstall install;

    public AddComponent(File projectLocation, EngineInstall install, YieldInternalFrame frame, List<ComponentProp> props) {
        this.props = props;
        this.frame = frame;
        this.install = install;

        File core = new File(Utils.EDITOR_DIR.getPath() + "/installs/" + install.install(), "yield-core.jar");
        JarInputStream jarFile;
        try {
            jarFile = new JarInputStream(new BufferedInputStream(new FileInputStream(core)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JarEntry je;
        List<Class<?>> classes = new ArrayList<>();

        try (URLClassLoader cl = new URLClassLoader(new URL[]{core.toURI().toURL()})) {
            while (true) {
                je = jarFile.getNextJarEntry();
                if (je == null) {
                    break;
                }
                if (je.getName().endsWith(".class")) {
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class<?> c = cl.loadClass(className);
                    Class<?> comp = c;
                    while ((c = c.getSuperclass()) != null) {
                        if (c.getName().equals("com.xebisco.yield.ComponentBehavior")) {
                            classes.add(comp);
                            break;
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        setLayout(new BorderLayout());
        JList<Class<?>> list = new JList<>(classes.toArray(new Class<?>[0]));
        list.addMouseListener(this);
        list.setCellRenderer(new ComponentListRender());
        list.addListSelectionListener(e -> {
            selectedClass = list.getSelectedValue();
            selectedFile = null;
        });
        selectedClass = list.getModel().getElementAt(0);
        JScrollPane s = new JScrollPane(list);
        s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(s);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Custom "));
        panel.add(new JButton(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(AddComponent.this), Dialog.ModalityType.APPLICATION_MODAL);
                List<Class<?>> customClasses = new ArrayList<>();

                List<File> files = Editor.getFilesByExtension(projectLocation, "java");
                Map<Class<?>, File> fileMap = new HashMap<>();
                try (URLClassLoader cl = new URLClassLoader(new URL[]{ComponentProp.DEST.toURI().toURL(), core.toURI().toURL()})) {
                    for (File f1 : files) {
                        File f = new File(ComponentProp.DEST, f1.getName().replace(".java", ".class"));
                        if (f.getName().endsWith(".class")) {
                            String className = f.getName().substring(0, f.getName().length() - 6);
                            Class<?> c = cl.loadClass(className);
                            Class<?> comp = c;
                            while ((c = c.getSuperclass()) != null) {
                                if (c.getName().equals("com.xebisco.yield.ComponentBehavior")) {
                                    customClasses.add(comp);
                                    fileMap.put(comp, f1);
                                    break;
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                JList<Class<?>> list = new JList<>(customClasses.toArray(new Class<?>[0]));
                list.addMouseListener(AddComponent.this);
                list.addListSelectionListener(e1 -> {
                    selectedFile = fileMap.get(list.getSelectedValue());
                });
                selectedClass = list.getModel().getElementAt(0);
                list.setCellRenderer(new ComponentListRender());
                dialog.add(list);

                dialog.setSize(300, 300);
                dialog.setLocation(dialog.getParent().getLocation());

                dialog.setVisible(true);
            }
        }));
        panel.add(new JButton(new AbstractAction("Create") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }));
        add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            if (selectedFile != null)
                props.add(new ComponentProp(selectedFile, install, frame).set(null));
            else
                props.add(new ComponentProp(selectedClass, true).set(null));
            SwingUtilities.getWindowAncestor(this).dispose();
        }
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
