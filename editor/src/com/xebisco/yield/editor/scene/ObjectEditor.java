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

import com.xebisco.yield.editor.*;
import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.StringProp;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ObjectEditor extends JPanel {
    private final YieldInternalFrame frame;

    private final Workspace workspace;
    private final JScrollPane scrollPane = new JScrollPane();
    private final EntityPrefab prefab;
    private final Prop nameProp;

    public ObjectEditor(File file, EntityPrefab prefab, Workspace workspace, YieldInternalFrame frame) {

        this.frame = frame;
        this.prefab = prefab;
        this.workspace = workspace;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nameProp = new StringProp("object_name", prefab.name());
        add(nameProp.panel(), BorderLayout.NORTH);

        List<ComponentProp> props = new ArrayList<>(prefab.components());

        scrollPane.getVerticalScrollBar().setUnitIncrement(10);


        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        JPanel addPanel = getAllPanel();
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setOpaque(false);
        addButtonPanel.add(new JButton(new AbstractAction("", Assets.images.get("addIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(ObjectEditor.this), Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setTitle(Assets.language.getProperty("add_script"));
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setLocation(addButtonPanel.getLocationOnScreen());
                dialog.add(new AddComponent(workspace.project().getProjectLocation(), workspace.project().preferredInstall(), frame, props, workspace.recompile()));
                dialog.setSize(300, 300);
                dialog.setVisible(true);
                update(props);
                repaint();
            }
        }));
        addPanel.add(addButtonPanel, BorderLayout.EAST);
        JLabel label;
        addPanel.add(label = new JLabel("<html><strong>" + Assets.language.getProperty("add_script") + "</strong></html>", Assets.images.get("scriptIcon.png"), JLabel.LEFT), BorderLayout.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(addPanel);
        update(props);
        add(bottomPanel, BorderLayout.SOUTH);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> save(props, file)));

        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                save(props, file);
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                update(props);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                save(props, file);
                frame.dispose();
            }
        });
    }

    private JPanel getAllPanel() {
        JPanel addPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground().brighter());
                g.fillRoundRect(getInsets().left, getInsets().top, getWidth() - getInsets().right - getInsets().left, getHeight() - getInsets().bottom - getInsets().top, 10, 10);
            }
        };


        addPanel.setOpaque(false);
        addPanel.setLayout(new BorderLayout());
        addPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        return addPanel;
    }

    private void save(List<ComponentProp> props, File file) {
        prefab.setName((String) nameProp.getValue());
        prefab.components().clear();
        prefab.components().addAll(props);

        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file))) {
            oo.writeObject(prefab);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void update(List<ComponentProp> props) {
        int sc = scrollPane.getVerticalScrollBar().getValue();
        props.forEach(ComponentProp::init);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(PropsPanel.compPropsPanel(props, frame, workspace.recompile(), () -> update(props)), BorderLayout.NORTH);
        scrollPane.setViewportView(panel);
        scrollPane.getVerticalScrollBar().setValue(sc);
        scrollPane.repaint();
    }
}
