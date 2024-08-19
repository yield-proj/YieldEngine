package com.xebisco.yieldengine.uiutils.fields;

import com.xebisco.yieldengine.uiutils.NumberField;

import javax.swing.*;
import java.awt.*;

import static com.xebisco.yieldengine.uiutils.Lang.getString;

public class PointFieldPanel extends FieldPanel<Point> {

    private Point value;
    private final NumberField xNumberField, yNumberField;

    public PointFieldPanel(String name, Point value, boolean allowsNegatives, boolean isSize, boolean editable) {
        super(name, editable);

        this.value = value;
        setLayout(new BorderLayout());
        add(new JLabel(getString(name) + ": "), BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        //X
        xNumberField = new NumberField(Integer.class, allowsNegatives);
        xNumberField.setValue(value.x);
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
        yNumberField = new NumberField(Integer.class, allowsNegatives);
        yNumberField.setValue(value.y);
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
    public Point getValue() {
        return null;
    }

    @Override
    public void setValue(Point value) {

    }
}
