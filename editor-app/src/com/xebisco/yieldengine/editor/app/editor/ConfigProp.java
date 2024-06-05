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

package com.xebisco.yieldengine.editor.app.editor;

import com.xebisco.yieldengine.editor.app.Global;
import com.xebisco.yieldengine.uiutils.Srd;
import com.xebisco.yieldengine.uiutils.props.*;
import com.xebisco.yieldengine.utils.Loading;
import com.xebisco.yieldengine.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigProp extends Prop {
    List<Prop> props;
    private final transient Editor editor;
    private final Class<?> configClass;
    public final transient Object configInstance;

    public ConfigProp(Object configInstance, Editor editor) {
        super(configInstance.getClass().getSimpleName(), new ArrayList<Pair<Pair<String, String>, String[]>>());
        this.configClass = configInstance.getClass();
        this.configInstance = configInstance;
        this.editor = editor;
        addFields(configClass, configInstance);
    }

    public ConfigProp(Class<?> configClass, Editor editor) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this(configClass.getConstructor().newInstance(), editor);
    }

    private final JPanel componentPanel = new JPanel(new BorderLayout());

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        JToolBar header = new JToolBar();
        header.setBackground(header.getBackground().brighter().brighter());
        header.setRollover(true);
        header.setFloatable(false);
        JLabel title = new JLabel(Srd.prettyString(configClass.getSimpleName()));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setForeground(title.getForeground().brighter());
        header.add(Box.createHorizontalGlue());
        header.add(title);
        header.add(Box.createHorizontalGlue());

        componentPanel.setOpaque(false);
        addComp();
        panel.add(componentPanel);

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private void addFields(Class<?> clazz, Object o) {
        //noinspection unchecked
        List<Pair<Pair<String, String>, String[]>> fields = (List<Pair<Pair<String, String>, String[]>>) value;
        Global.extractFields(clazz, o, editor, fields, false);
    }

    public void addComp() {
        componentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //noinspection unchecked
        props = ComponentProp.getProps(((List<Pair<Pair<String, String>, String[]>>) value), editor);
        if (props.isEmpty()) props.add(new StringProp("No visible fields"));
        JPanel propPanel = new PropPanel(props.toArray(new Prop[0]));
        propPanel.setOpaque(false);
        componentPanel.removeAll();
        componentPanel.add(propPanel);
        saveValues();
    }

    public void addComp(HashMap<String, Serializable> update) {
        componentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //noinspection unchecked
        props = ComponentProp.getProps(((List<Pair<Pair<String, String>, String[]>>) value), editor);
        PropPanel.insert(props.toArray(new Prop[0]), update);
        if (props.isEmpty()) props.add(new StringProp("No visible fields"));
        JPanel propPanel = new PropPanel(props.toArray(new Prop[0]));
        propPanel.setOpaque(false);
        componentPanel.removeAll();
        componentPanel.add(propPanel);
        saveValues();
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
            } else if (prop instanceof DoubleTextFieldProp || prop instanceof FloatTextFieldProp || prop instanceof IntTextFieldProp || prop instanceof LongTextFieldProp) {
                v = new String[]{((String) prop.value()).isEmpty() ? "0" : ((String) prop.value())};
            } else {
                v = new String[]{String.valueOf(prop.value())};
            }
            for(Pair<Pair<String, String>, String[]> field : fields) {
                if (field.first().first().equals(prop.name())) {
                    System.arraycopy(v, 0, field.second(), 0, v.length);
                    break;
                }
            }
        }
        try {
            Loading.applyPropsToObject(fields, configInstance);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Prop> props() {
        return props;
    }

    public ConfigProp setProps(List<Prop> props) {
        this.props = props;
        return this;
    }

    public Editor editor() {
        return editor;
    }

    public Class<?> configClass() {
        return configClass;
    }

    public Object configInstance() {
        return configInstance;
    }

    public JPanel componentPanel() {
        return componentPanel;
    }
}