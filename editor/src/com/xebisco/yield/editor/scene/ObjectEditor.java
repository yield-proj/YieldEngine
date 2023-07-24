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
import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;
import com.xebisco.yield.editor.prop.StringProp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class ObjectEditor extends JPanel {
    public ObjectEditor(EntityPrefab prefab, Workspace workspace) {
        setLayout(new BorderLayout());
        Prop[] props;
        try {
            URLClassLoader core = new URLClassLoader(new URL[] {new File(Utils.EDITOR_DIR + "/installs/" + workspace.install().install(), "yield-core.jar").toURI().toURL()});
            props = new Prop[]{
                    new StringProp("Name", prefab.name()),
                    new ComponentProp(core.loadClass("com.xebisco.yield.Transform2D"))
            };
            core.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        add(PropsWindow.propsPanel(props), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(new JButton(new AbstractAction("Apply") {
            @Override
            public void actionPerformed(ActionEvent e) {
                prefab.setName((String) Objects.requireNonNull(Props.get(props, "Name")).getValue());

            }
        }));
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
