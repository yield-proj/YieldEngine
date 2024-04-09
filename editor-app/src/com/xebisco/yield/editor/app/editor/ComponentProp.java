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

import com.xebisco.yield.editor.app.Global;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentProp extends Prop {
    private boolean hidden;
    public final static Pattern COMPONENT_NAME_PATTERN = Pattern.compile("([^.]+)$");
    private final Runnable moveUp, moveDown, remove, reload;
    List<Prop> props;

    public ComponentProp(EditorComponent value, Runnable moveUp, Runnable moveDown, Runnable remove, Runnable reload) {
        super(value.className(), value);
        this.moveUp = moveUp;
        this.moveDown = moveDown;
        this.remove = remove;
        this.reload = reload;
    }

    @Override
    public JComponent render() {
        EditorComponent component = (EditorComponent) value();
        JPanel panel = new JPanel(new BorderLayout());
        JToolBar header = new JToolBar();
        header.setBackground(header.getBackground().brighter());
        header.setRollover(true);
        header.setFloatable(false);
        Matcher matcher = COMPONENT_NAME_PATTERN.matcher(component.className());
        matcher.find();
        JLabel title = new JLabel(matcher.group(1));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setForeground(title.getForeground().brighter());
        try {
            title.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(ComponentProp.class.getResource("/logo/codeIcon.png"))).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        header.add(title);
        header.add(Box.createHorizontalGlue());
        JButton hideButton = new JButton();

        JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BorderLayout());
        componentPanel.setOpaque(false);
        addComp(componentPanel);
        panel.add(componentPanel);


        hideButton.setAction(new AbstractAction(Srd.LANG.getProperty("misc_hide")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                hidden = !hidden;
                if (hidden) {
                    hideButton.setText(Srd.LANG.getProperty("misc_show"));
                    panel.remove(componentPanel);
                } else {
                    hideButton.setText(Srd.LANG.getProperty("misc_hide"));
                    panel.add(componentPanel);
                }

                panel.revalidate();
                panel.updateUI();
            }
        });
        header.add(hideButton);

        if (!component.className().equals("com.xebisco.yield.Transform2D")) {

            JButton optionsButton = new JButton();
            optionsButton.setAction(new AbstractAction("+") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.add(new AbstractAction("Move Up") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            moveUp.run();
                            reload.run();
                        }
                    });
                    popupMenu.add(new AbstractAction("Move Down") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            moveDown.run();
                            reload.run();
                        }
                    });
                    popupMenu.add(new AbstractAction("Remove") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            remove.run();
                            reload.run();
                        }
                    });
                    popupMenu.show(optionsButton, 0, 0);
                }
            });
            header.add(optionsButton);
        }

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private void addComp(JPanel componentPanel) {
        componentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        props = getProps(((EditorComponent) value).fields());
        if (props.isEmpty()) props.add(new StringProp("No visible fields"));
        JPanel propPanel = new PropPanel(props.toArray(new Prop[0]));
        propPanel.setOpaque(false);
        componentPanel.add(propPanel);
    }


    private static List<Prop> getProps(List<Pair<Pair<String, String>, String[]>> compValues) {
        List<Prop> props = new ArrayList<>();

        for (Pair<Pair<String, String>, String[]> compValue : compValues) {
            Class<?> c;
            try {
                c = Class.forName(compValue.first().second());
            } catch (ClassNotFoundException e) {
                try {
                    c = Global.yieldEngineClassLoader.loadClass(compValue.first().second());
                } catch (ClassNotFoundException ignore) {
                    switch (compValue.first().second()) {
                        case "int" -> c = int.class;
                        case "long" -> c = long.class;
                        case "float" -> c = float.class;
                        case "double" -> c = double.class;
                        default -> throw new RuntimeException(e);
                    }
                }
            }
            EditableValuesType type = EditableValuesType.getType(c);
            if (type == null) {
                if (c.isEnum()) {
                    try {
                        props.add(new ComboProp(compValue.first().first(), (Serializable) c.getMethod("valueOf", String.class).invoke(null, compValue.second()[0]), (Serializable[]) c.getEnumConstants()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                continue;
            }
            switch (type) {
                case STRING -> props.add(new TextFieldProp(compValue.first().first(), compValue.second()[0]));
                case INT ->
                        props.add(new IntTextFieldProp(compValue.first().first(), Integer.parseInt(compValue.second()[0])));
                case LONG ->
                        props.add(new LongTextFieldProp(compValue.first().first(), Long.parseLong(compValue.second()[0])));
                case FLOAT ->
                        props.add(new FloatTextFieldProp(compValue.first().first(), Float.parseFloat(compValue.second()[0])));
                case DOUBLE ->
                        props.add(new DoubleTextFieldProp(compValue.first().first(), Double.parseDouble(compValue.second()[0])));
                case POSITION -> {
                    props.add(new PositionProp(compValue.first().first(), new Point2D.Float(Float.parseFloat(compValue.second()[0]), Float.parseFloat(compValue.second()[1]))));
                }
                case COLOR -> {
                    props.add(new ColorProp(compValue.first().first(), new Color((float) Double.parseDouble(compValue.second()[0]), (float) Double.parseDouble(compValue.second()[1]), (float) Double.parseDouble(compValue.second()[2]), (float) Double.parseDouble(compValue.second()[3]))));
                }
                case FILE -> {
                    FileFilter filter;
                    if(compValue.second().length > 1)
                        filter = new FileFilter() {
                            @Override
                            public boolean accept(File f) {
                                for(String a : compValue.second()[1].split(";")) {
                                    if(f.getName().endsWith(a)) return true;
                                }
                                return false;
                            }

                            @Override
                            public String getDescription() {
                                return "Custom";
                            }
                        };
                    else filter = new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return true;
                        }

                        @Override
                        public String getDescription() {
                            return "All Files";
                        }
                    };
                    props.add(new PathProp(compValue.first().first(), compValue.second()[0], filter));
                }
                case TEXTURE -> props.add(new ImageFileProp(compValue.first().first(), compValue.second()[0]));
                case ARRAY -> {
                    //props.add(new ArrayProp<>(compValue.first().first(), getProps(compValue.arrayValues()).toArray(new Prop[0])));nnot invoke "java.awt.Color.getRed()" because "prop.value" is null
                }
            }
        }
        return props;
    }

    public void saveValues() {
        EditorComponent component = (EditorComponent) value;
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
            component.fields().forEach(p -> {
                if (p.first().first().equals(prop.name())) {
                    System.arraycopy(v, 0, p.second(), 0, v.length);
                }
            });
        }
    }


}