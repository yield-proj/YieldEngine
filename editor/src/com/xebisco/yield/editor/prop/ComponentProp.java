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

package com.xebisco.yield.editor.prop;

import com.xebisco.yield.ComponentIcon;
import com.xebisco.yield.ComponentIconType;
import com.xebisco.yield.VisibleOnEditor;
import com.xebisco.yield.editor.*;
import com.xebisco.yield.editor.code.CodePanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentProp extends Prop {
    public static final File DEST = new File(Utils.EDITOR_DIR + "/out/" + Entry.RUN);

    private Class<?> componentClass;
    private final List<Pair<String, Class<?>>> fields = new ArrayList<>();

    private final File comp;

    private final EngineInstall install;
    private final boolean showAddButton;
    private boolean addComp = true;

    public ComponentProp(File comp, EngineInstall install) {
        super(comp.getName().replace(".java", ""), null);
        this.showAddButton = true;
        this.comp = comp;
        this.install = install;
    }

    private void updateComponentClass() {
        File core = new File(Utils.EDITOR_DIR.getPath() + "/installs/" + install.install() + "/yield-core.jar");

        try (URLClassLoader loader = new URLClassLoader(new URL[]{DEST.toURI().toURL(), core.toURI().toURL()})) {
            componentClass = loader.loadClass(comp.getName().replace(".java", ""));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ComponentProp(Class<?> componentClass, boolean showAddButton) {
        super(componentClass.getSimpleName(), null);
        this.showAddButton = showAddButton;
        install = null;
        comp = null;
        this.componentClass = componentClass;
    }

    public ComponentProp init() {
        if (comp != null) updateComponentClass();
        Map<String, Serializable> values = new HashMap<>();
        List<Pair<String, Class<?>>> fields1 = new ArrayList<>();
        Object o;
        try {
            o = componentClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Field field : componentClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(VisibleOnEditor.class)) {
                boolean c = false;
                for (Pair<String, Class<?>> p : fields) {
                    if (p.first().equals(field.getName()) && p.second().equals(field.getType())) {
                        c = true;
                        break;
                    }
                }
                fields1.add(new Pair<>(field.getName(), field.getType()));
                if (!c) {
                    try {
                        Object v = field.get(o);
                        if (v instanceof Serializable vs) {
                            values.put(field.getName(), vs);
                        } else if (v == null) {
                            values.put(field.getName(), null);
                        } else {
                            JOptionPane.showMessageDialog(null, componentClass.getName() + " " + field.getName() + " need to be serializable.");
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    //noinspection unchecked
                    values.put(field.getName(), ((Map<String, Serializable>) getValue()).get(field.getName()));
                }
            }
        }
        fields.clear();
        fields.addAll(fields1);
        setValue((Serializable) values);
        return this;
    }

    public ComponentProp set(Map<String, Serializable> value) {
        if (value != null) {
            //noinspection unchecked
            ((Map<String, Serializable>) getValue()).putAll(value);
        }
        return this;
    }

    private void codeFrame(YieldInternalFrame frame, IRecompile recompile) {
        CodePanel.newCodeFrame(frame.getDesktopPane(), install, comp, frame, recompile).setLocation(frame.getX() + frame.getWidth() + 100, frame.getY() + 100);
    }

    public JPanel panel(YieldInternalFrame frame, IRecompile recompile, Runnable up, Runnable down) {
        //noinspection unchecked
        set((Map<String, Serializable>) getValue());
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground().brighter());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        panel.setLayout(new GridBagLayout());
        JLabel label = new JLabel(getName() + (comp == null ? "" : " (Custom)"));
        if (componentClass.isAnnotationPresent(ComponentIcon.class)) {
            ComponentIconType icon = componentClass.getAnnotation(ComponentIcon.class).iconType();
            label.setIcon(switch (icon) {
                case TRANSFORM -> Assets.images.get("transformIcon.png");
                case PHYSICS -> Assets.images.get("physicsIcon.png");
                case GRAPHICAL -> Assets.images.get("graphicalIcon.png");
                case ANIMATION -> Assets.images.get("animationIcon.png");
                case AUDIO -> Assets.images.get("audioIcon.png");
            });
        } else {
            label.setIcon(Assets.images.get("scriptIcon.png"));
        }
        label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(label.getFont().getSize2D() + 2));
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BorderLayout());
        namePanel.add(label);
        if (showAddButton) {
            JCheckBox checkBox;
            namePanel.add(checkBox = new JCheckBox(), BorderLayout.WEST);
            checkBox.setSelected(true);
            checkBox.addItemListener(e -> addComp = checkBox.isSelected());
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton editButton;
        buttonPanel.add(editButton = new JButton(new AbstractAction("", Assets.images.get("editIcon.png")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeFrame(frame, recompile);
            }
        }));
        editButton.setToolTipText(Assets.language.getProperty("edit_script"));

        if (showAddButton) {
            buttonPanel.add(new JButton(new AbstractAction("", Assets.images.get("optionsIcon.png")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem item = new JMenuItem(new AbstractAction(Assets.language.getProperty("edit_script")) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            codeFrame(frame, recompile);
                        }
                    });
                    if (comp != null)
                        popupMenu.add(item);

                    item = new JMenuItem(new AbstractAction(Assets.language.getProperty("move_component_up")) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            up.run();
                        }
                    });
                    popupMenu.add(item);

                    item = new JMenuItem(new AbstractAction(Assets.language.getProperty("move_component_down")) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            down.run();
                        }
                    });
                    popupMenu.add(item);

                    popupMenu.show(buttonPanel, buttonPanel.getMousePosition().x, buttonPanel.getMousePosition().y);
                }
            }));
        }
        if (comp == null) editButton.setEnabled(false);

        namePanel.add(buttonPanel, BorderLayout.EAST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        panel.add(namePanel, gbc);
        gbc.gridy++;
        namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.setOpaque(false);
        namePanel.add(label = new JLabel("Class "), BorderLayout.WEST);
        label.setEnabled(false);
        JTextField tf = new JTextField(componentClass.getName());
        tf.setEnabled(false);
        namePanel.add(tf);
        panel.add(namePanel, gbc);
        gbc.gridy++;
        for (Pair<String, Class<?>> field : fields) {
            panel.add(fieldPanel(field), gbc);
            gbc.gridy++;
        }
        label = new JLabel();
        label.setFont(label.getFont().deriveFont(5f));
        panel.add(label, gbc);
        return panel;
    }

    @Override
    public JPanel panel() {
        return null;
    }

    private JPanel fieldPanel(Pair<String, Class<?>> field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        JLabel n = new JLabel("<html>" + toName(field.first()) + ": <em>" + field.second().getSimpleName() + "  </em></html>");
        panel.add(n, BorderLayout.WEST);
        if (field.second().equals(String.class) ||
                field.second().equals(Integer.class) ||
                field.second().equals(int.class) ||
                field.second().equals(Float.class) ||
                field.second().equals(float.class) ||
                field.second().equals(Double.class) ||
                field.second().equals(double.class) ||
                field.second().equals(Long.class) ||
                field.second().equals(long.class) ||
                field.second().equals(Short.class) ||
                field.second().equals(short.class) ||
                field.second().equals(Byte.class) ||
                field.second().equals(byte.class)
        ) {
            YieldTextField textField = new YieldTextField();
            //noinspection unchecked
            Serializable value = ((Map<String, Serializable>) getValue()).get(field.first());
            textField.setText(String.valueOf(value == null ? "" : value));
            if (value == null) textField.setNull(true);
            Border border = textField.getBorder();
            textField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        //noinspection unchecked
                        ((Map<String, Serializable>) getValue()).put(field.first(), castP(textField.getText(), field.second()));
                        textField.setBackground(UIManager.getColor("TextField.background"));
                        textField.setNull(false);
                    } catch (NumberFormatException ex) {
                        textField.setBackground(Color.RED);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        //noinspection unchecked
                        ((Map<String, Serializable>) getValue()).put(field.first(), castP(textField.getText(), field.second()));
                        textField.setBorder(border);
                        textField.setBackground(UIManager.getColor("TextField.background"));
                        textField.setNull(false);
                    } catch (NumberFormatException ex) {
                        textField.setBackground(Color.RED);
                    }
                }
            });
            panel.add(textField);
            if (!field.second().isPrimitive()) {
                JPanel toNullPanel = new JPanel();
                toNullPanel.setOpaque(false);
                toNullPanel.add(new JButton(new AbstractAction("", Assets.images.get("closeIcon.png")) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textField.setText(null);
                        //noinspection unchecked
                        ((Map<String, Serializable>) ComponentProp.this.getValue()).put(field.first(), null);
                        textField.setNull(true);
                        textField.setBorder(border);
                        textField.repaint();
                    }
                }));
                panel.add(toNullPanel, BorderLayout.EAST);
            }
        } else if (field.second().getName().equals("com.xebisco.yield.Vector2D")) {
            //noinspection unchecked
            Serializable value = ((Map<String, Serializable>) getValue()).get(field.first());
            double xValue, yValue;
            try {
                xValue = (double) value.getClass().getMethod("x").invoke(value);
                yValue = (double) value.getClass().getMethod("y").invoke(value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            JPanel values = new JPanel();
            values.setOpaque(false);
            values.setLayout(new GridLayout(1, 2));
            JPanel xPanel = new JPanel();
            xPanel.setOpaque(false);
            xPanel.setLayout(new BorderLayout());
            JLabel label = new JLabel();
            label.setText("<html><strong>X</strong></html>");
            label.setForeground(Color.RED);
            xPanel.add(label, BorderLayout.WEST);
            values.add(xPanel);

            JTextField textField = new JTextField();
            xPanel.add(textField);
            textField.setText(String.valueOf(xValue));
            JTextField finalTextField1 = textField;
            textField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        value.getClass().getMethod("setX", double.class).invoke(value, Double.parseDouble(finalTextField1.getText()));
                        finalTextField1.setBackground(UIManager.getColor("TextField.background"));
                    } catch (NumberFormatException ex) {
                        finalTextField1.setBackground(Color.RED);
                    } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        value.getClass().getMethod("setX", double.class).invoke(value, Double.parseDouble(finalTextField1.getText()));
                        finalTextField1.setBackground(UIManager.getColor("TextField.background"));
                    } catch (NumberFormatException ex) {
                        finalTextField1.setBackground(Color.RED);
                    } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            JPanel yPanel = new JPanel();
            yPanel.setOpaque(false);
            yPanel.setLayout(new BorderLayout());
            label = new JLabel();
            label.setText("<html><strong>Y</strong></html>");
            label.setForeground(Color.GREEN);
            yPanel.add(label, BorderLayout.WEST);
            values.add(yPanel);

            textField = new JTextField();
            yPanel.add(textField);
            textField.setText(String.valueOf(yValue));
            JTextField finalTextField = textField;
            textField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        value.getClass().getMethod("setY", double.class).invoke(value, Double.parseDouble(finalTextField.getText()));
                        finalTextField.setBorder(null);
                    } catch (NumberFormatException ex) {
                        finalTextField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        value.getClass().getMethod("setY", double.class).invoke(value, Double.parseDouble(finalTextField.getText()));
                        finalTextField.setBorder(null);
                    } catch (NumberFormatException ex) {
                        finalTextField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            panel.add(values);
        }
        return panel;
    }

    private static String toName(String n) {
        StringBuilder name = new StringBuilder();
        boolean lastIs_ = false;
        for (char c : n.toCharArray()) {
            if (c == '_') {
                if (!lastIs_) c = ' ';
                else if (name.charAt(name.length() - 1) == ' ') name.setLength(name.length() - 1);
                lastIs_ = true;
            }
            if (name.isEmpty()) {
                c = Character.toUpperCase(c);
            } else if (Character.isUpperCase(c)) {
                name.append(' ');
            }
            name.append(c);
        }
        return name.toString();
    }

    private Serializable castP(String s, Class<?> c) throws NumberFormatException {
        if (c.equals(Integer.class) || c.equals(int.class)) {
            return Integer.parseInt(s);
        } else if (c.equals(Float.class) || c.equals(float.class)) {
            return Float.parseFloat(s);
        } else if (c.equals(Double.class) || c.equals(double.class)) {
            return Double.parseDouble(s);
        } else if (c.equals(Short.class) || c.equals(short.class)) {
            return Short.parseShort(s);
        } else if (c.equals(Byte.class) || c.equals(byte.class)) {
            return Byte.parseByte(s);
        } else return s;
    }

    public Class<?> componentClass() {
        return componentClass;
    }

    public List<Pair<String, Class<?>>> fields() {
        return fields;
    }

    public File comp() {
        return comp;
    }

    public EngineInstall install() {
        return install;
    }

    public boolean showAddButton() {
        return showAddButton;
    }

    public boolean addComp() {
        return addComp;
    }

    public ComponentProp setAddComp(boolean addComp) {
        this.addComp = addComp;
        return this;
    }
}
