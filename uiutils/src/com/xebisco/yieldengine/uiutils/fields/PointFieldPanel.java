package com.xebisco.yieldengine.uiutils.fields;

import com.xebisco.yieldengine.uiutils.NumberField;
import com.xebisco.yieldengine.uiutils.Point;

import javax.swing.*;
import java.awt.*;

import static com.xebisco.yieldengine.uiutils.Lang.getString;

public class PointFieldPanel<T extends Number> extends FieldPanel<com.xebisco.yieldengine.uiutils.Point<T>> {

    private final NumberField<T> xNumberField, yNumberField;

    public PointFieldPanel(String name, Class<T> numberClass, com.xebisco.yieldengine.uiutils.Point<T> value, boolean allowsNegatives, boolean isSize, boolean editable) {
        super(name, editable);

        setLayout(new BorderLayout());
        add(new JLabel(getString(name) + ": "), BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        //X
        xNumberField = new NumberField<>(numberClass, allowsNegatives);
        xNumberField.setValue(value.getX());
        JPanel x = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.WEST;
        x.add(new JLabel(getString(isSize ? "width" : "x") + ": "), gbc);
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        x.add(xNumberField, gbc);
        xNumberField.setEditable(editable);

        center.add(x);

        //Y
        yNumberField = new NumberField<>(numberClass, allowsNegatives);
        yNumberField.setValue(value.getY());
        JPanel y = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.WEST;
        y.add(new JLabel(" " + getString(isSize ? "height" : "y") + ": "), gbc);
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        y.add(yNumberField, gbc);
        yNumberField.setEditable(editable);

        center.add(y);

        add(center);
    }

    @Override
    public com.xebisco.yieldengine.uiutils.Point<T> getValue() {
        return new Point<>(xNumberField.getValue(), yNumberField.getValue());
    }

    @Override
    public void setValue(com.xebisco.yieldengine.uiutils.Point<T> value) {
        xNumberField.setValue(value.getX());
        yNumberField.setValue(value.getY());
    }
}
