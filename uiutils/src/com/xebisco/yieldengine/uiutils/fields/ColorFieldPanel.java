package com.xebisco.yieldengine.uiutils.fields;

import com.xebisco.yieldengine.uiutils.Out;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static com.xebisco.yieldengine.uiutils.Lang.getString;

public class ColorFieldPanel extends FieldPanel<Color> {

    private Color value;

    private final JButton colorButton;

    public ColorFieldPanel(String name, Color value, boolean editable) {
        super(name, editable);
        setLayout(new BorderLayout());

        colorButton = new JButton("Color");

        setValue(value);

        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(ColorFieldPanel.this, "Choose a Color", getValue());

            if(newColor != null) {
                setValue(newColor);
            }
        });

        /*colorButton.setBorderPainted(false);
        colorButton.setContentAreaFilled(false);*/

        add(new JLabel(getString(name) + ": "), BorderLayout.WEST);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(colorButton, gbc);

        p.add(Box.createHorizontalGlue());

        add(p);
    }

    @Override
    public Color getValue() {
        return value;
    }

    @Override
    public void setValue(Color value) {
        this.value = value;
        colorButton.setBackground(value);
    }
}
