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

package com.xebisco.yield.editor;

import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropsWindow extends JDialog {
    public PropsWindow(Map<String, Prop[]> s, Runnable apply, Frame owner, String title) {
        super(owner);
        Runnable a = () -> {
            apply.run();
            dispose();
        };
        PropsPanel propsPanel = new PropsPanel(s, a);
        setModal(true);
        setContentPane(propsPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 400);
        setMaximumSize(new Dimension(1000, 400));
        setMinimumSize(new Dimension(500, 100));
        setLocationRelativeTo(owner);

        setTitle(Assets.language.getProperty(title));

        setVisible(true);
    }
}
