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

import com.xebisco.yield.VisibleOnEditor;
import com.xebisco.yield.editor.DocumentAdapter;
import com.xebisco.yield.editor.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentProp extends Prop {
    public static final File DEST = new File(Utils.EDITOR_DIR + "/out/");
    public static final ClassLoader loader;


    static {
        if (!DEST.exists()) DEST.mkdir();
        try {
            loader = new URLClassLoader(new URL[]{new URL("file://" + DEST.getPath())});
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private final Class<?> componentClass;
    private final List<Field> fields = new ArrayList<>();

    public ComponentProp(File comp) {
        super(comp.getName().replace(".java", ""), null);
        try {
            Runtime.getRuntime().exec(new String[]{"javac", "-d", DEST.getPath(), comp.getPath()});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            componentClass = loader.loadClass(getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        set();
    }

    public ComponentProp(Class<?> componentClass) {
        super(componentClass.getSimpleName(), null);
        this.componentClass = componentClass;
        set();
    }

    private void set() {
        Object o;
        try {
            o = componentClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        setValue(new HashMap<String, Serializable>());
        for (Field field : componentClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(VisibleOnEditor.class)) {
                fields.add(field);
                try {
                    Object v = field.get(o);
                    if (v instanceof Serializable vs) {
                        //noinspection unchecked
                        ((Map<String, Serializable>) getValue()).put(field.getName(), vs);
                    } else {
                        JOptionPane.showMessageDialog(null, componentClass.getName() + " " + field.getName() + " need to be serializable.");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground().brighter());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.setLayout(new GridBagLayout());
        gbc.gridy = 0;
        JLabel label = new JLabel(getName());
        label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(label.getFont().getSize2D() + 2));
        panel.add(label, gbc);
        gbc.gridy++;
        for (Field field : fields) {
            panel.add(fieldPanel(field), gbc);
            gbc.gridy++;
        }
        return panel;
    }

    private JPanel fieldPanel(Field field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(field.getName() + ' '), BorderLayout.WEST);
        if (field.getType().equals(String.class) ||
                field.getType().equals(Integer.class) ||
                field.getType().equals(int.class) ||
                field.getType().equals(Float.class) ||
                field.getType().equals(float.class) ||
                field.getType().equals(Double.class) ||
                field.getType().equals(double.class) ||
                field.getType().equals(Long.class) ||
                field.getType().equals(long.class) ||
                field.getType().equals(Short.class) ||
                field.getType().equals(short.class) ||
                field.getType().equals(Byte.class) ||
                field.getType().equals(byte.class)
        ) {
            JTextField textField = new JTextField();
            textField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        //noinspection unchecked
                        ((Map<String, Serializable>) getValue()).put(field.getName(), castP(textField.getText(), field.getType()));
                        textField.setBorder(null);
                    } catch (NumberFormatException ex) {
                        textField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        //noinspection unchecked
                        ((Map<String, Serializable>) getValue()).put(field.getName(), castP(textField.getText(), field.getType()));
                        textField.setBorder(null);
                    } catch (NumberFormatException ex) {
                        textField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                }
            });
            panel.add(textField);
        }
        return panel;
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
}
