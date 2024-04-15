package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class PositionProp extends Prop {

    private final JTextField widthField, heightField;

    public PositionProp(String name, Point2D.Float value) {
        super(name, value);
        widthField = new JTextField(String.valueOf(value.x));
        ((PlainDocument) widthField.getDocument()).setDocumentFilter(new FloatFilter());
        widthField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                value.x = Float.parseFloat(widthField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (widthField.getText().isEmpty()) value.x = 0;
                else value.x = Float.parseFloat(widthField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                value.x = Float.parseFloat(widthField.getText());
            }
        });
        heightField = new JTextField(String.valueOf(value.y));
        ((PlainDocument) heightField.getDocument()).setDocumentFilter(new FloatFilter());
        heightField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                value.y = Float.parseFloat(heightField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (heightField.getText().isEmpty()) value.y = 0;
                else value.y = Float.parseFloat(heightField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (heightField.getText().isEmpty()) value.y = 0;
                else value.y = Float.parseFloat(heightField.getText());
            }
        });
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        String text = Srd.LANG.getProperty(name());
        if (text == null) text = name();
        JLabel west = new JLabel(text + "    ");
        west.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        panel.add(west, BorderLayout.WEST);

        JPanel xW = new JPanel();
        xW.setLayout(new BorderLayout());
        xW.add(new JLabel(Srd.LANG.getProperty("misc_x") + " "), BorderLayout.WEST);
        JPanel wP = new JPanel();
        wP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        wP.add(widthField, gbc);
        xW.add(wP);

        JPanel yH = new JPanel();
        yH.setLayout(new BorderLayout());
        yH.add(new JLabel(" " + Srd.LANG.getProperty("misc_y") + " "), BorderLayout.WEST);
        yH.add(heightField);
        JPanel hP = new JPanel();
        hP.setLayout(new GridBagLayout());
        hP.add(heightField, gbc);
        yH.add(hP);

        JPanel valuesPanel = new JPanel();
        valuesPanel.setLayout(new GridLayout());
        valuesPanel.add(xW);
        valuesPanel.add(yH);
        panel.add(valuesPanel);

        return panel;
    }

    @Override
    public Prop setValue(Serializable value) {
        widthField.setText(String.valueOf(((Point2D.Float) value).x));
        heightField.setText(String.valueOf(((Point2D.Float) value).y));
        return super.setValue(value);
    }

    public Prop setValue(float x, float y) {
        widthField.setText(String.valueOf(x));
        heightField.setText(String.valueOf(y));
        ((Point2D.Float) value).setLocation(x, y);
        return this;
    }
}
