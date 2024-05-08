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

package com.xebisco.yield.editor.app.editor;

import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.props.*;
import com.xebisco.yield.utils.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConfigProp extends Prop {
    List<Prop> props;
    private final transient Editor editor;
    private final Class<?> configClass;

    public ConfigProp(Class<?> configClass, Editor editor) {
        super(configClass.getSimpleName(), new ArrayList<Pair<Pair<String, String>, String[]>>());
        this.configClass = configClass;
        this.editor = editor;
        try {
            addFields(configClass, configClass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        JToolBar header = new JToolBar();
        header.setBackground(header.getBackground().brighter());
        header.setRollover(true);
        header.setFloatable(false);
        JLabel title = new JLabel(Srd.prettyString(configClass.getSimpleName()));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setForeground(title.getForeground().brighter());
        header.add(Box.createHorizontalGlue());
        header.add(title);
        header.add(Box.createHorizontalGlue());

        JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BorderLayout());
        componentPanel.setOpaque(false);
        addComp(componentPanel);
        panel.add(componentPanel);

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private void addFields(Class<?> clazz, Object o) {
        //noinspection unchecked
        List<Pair<Pair<String, String>, String[]>> fields = (List<Pair<Pair<String, String>, String[]>>) value;
        EditorComponent.extractFields(clazz, o, editor, fields, false);
    }

    private void addComp(JPanel componentPanel) {
        componentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //noinspection unchecked
        props = ComponentProp.getProps(((List<Pair<Pair<String, String>, String[]>>) value), editor);
        if (props.isEmpty()) props.add(new StringProp("No visible fields"));
        JPanel propPanel = new PropPanel(props.toArray(new Prop[0]));
        propPanel.setOpaque(false);
        componentPanel.add(propPanel);
    }

    public void saveValues() {
        //noinspection unchecked
        ArrayList<Pair<Pair<String, String>, String[]>> fields = (ArrayList<Pair<Pair<String, String>, String[]>>) value;
        for (Prop prop : props) {
            String[] v;
            if (prop.getClass().equals(PositionProp.class)) {
                v = new String[]{String.valueOf(((Point2D.Float) prop.value()).x), String.valueOf(((Point2D.Float) prop.value()).y).toUpperCase()};
            } else if (prop.getClass().equals(ColorProp.class)) {
                v = new String[]{String.valueOf(((Color) prop.value()).getRed() / 255.), String.valueOf(((Color) prop.value()).getGreen() / 255.), String.valueOf(((Color) prop.value()).getBlue() / 255.), String.valueOf(((Color) prop.value()).getAlpha() / 255.)};
            } else if(prop instanceof DoubleTextFieldProp || prop instanceof FloatTextFieldProp || prop instanceof IntTextFieldProp || prop instanceof LongTextFieldProp) {
                v = new String[]{((String) prop.value()).isEmpty() ? "0" : ((String) prop.value())};
            } else {
                v = new String[]{String.valueOf(prop.value())};
            }
            fields.forEach(p -> {
                if (p.first().first().equals(prop.name())) {
                    System.arraycopy(v, 0, p.second(), 0, v.length);
                }
            });
        }
    }


}