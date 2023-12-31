package com.xebisco.yield.editor.app.props;

import com.xebisco.yield.editor.app.EditableValuesType;
import com.xebisco.yield.editor.app.EditorComponent;
import com.xebisco.yield.editor.app.Pair;
import com.xebisco.yield.editor.app.Srd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentProp extends Prop {
    private boolean hidden;
    private final static Pattern COMPONENT_NAME_PATTERN = Pattern.compile("([^.]+)$");

    public ComponentProp(EditorComponent value) {
        super(value.className(), value);
    }

    @Override
    public JComponent render() {
        EditorComponent component = (EditorComponent) value();
        JPanel panel = new JPanel(new BorderLayout());
        JToolBar header = new JToolBar();
        header.setRollover(true);
        header.setFloatable(false);
        Matcher matcher = COMPONENT_NAME_PATTERN.matcher(component.className());
        matcher.find();
        JLabel title = new JLabel(matcher.group(1));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setIcon(new ImageIcon(Objects.requireNonNull(ComponentProp.class.getResource("/com/xebisco/yield/editor/app/codeIcon.png"))));
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

        panel.add(header, BorderLayout.NORTH);
        return panel;
    }

    private void addComp(JPanel componentPanel) {
        componentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        List<Prop> props = getProps(((EditorComponent) value).fields());
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
                    c = Srd.yieldEngineClassLoader.loadClass(compValue.first().second());
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
                case ARRAY -> {
                    //props.add(new ArrayProp<>(compValue.first().first(), getProps(compValue.arrayValues()).toArray(new Prop[0])));
                }
            }
        }
        return props;
    }


}
