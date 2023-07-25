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

import com.xebisco.yield.editor.PropsWindow;
import com.xebisco.yield.editor.Utils;
import com.xebisco.yield.editor.Workspace;
import com.xebisco.yield.editor.YieldInternalFrame;
import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.StringProp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class ObjectEditor extends JPanel {
    public ObjectEditor(EntityPrefab prefab, Workspace workspace, YieldInternalFrame frame) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Prop nameProp = new StringProp("Name", prefab.name());
        add(nameProp.panel(), BorderLayout.NORTH);
        Prop[] props;
        try {
            URLClassLoader core = new URLClassLoader(new URL[] {new File(Utils.EDITOR_DIR + "/installs/" + workspace.install().install(), "yield-core.jar").toURI().toURL()});
            props = new Prop[]{
                    new ComponentProp(core.loadClass("com.xebisco.yield.Transform2D")),
                    new ComponentProp(new File("C:\\Users\\vto6j\\OneDrive\\Documentos\\Test\\Scripts\\HelloScript.java"), workspace.install(), frame)
            };
            core.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(PropsWindow.propsPanel(props), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(new JButton(new AbstractAction("Apply") {
            @Override
            public void actionPerformed(ActionEvent e) {
                prefab.setName((String) nameProp.getValue());

            }
        }));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void update() {

    }
}
