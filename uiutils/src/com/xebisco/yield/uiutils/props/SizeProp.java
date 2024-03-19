package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.io.Serializable;

public class SizeProp extends Prop {

    private final JTextField widthField, heightField;

    public SizeProp(String name, Dimension value) {
        super(name, value);
        widthField = new JTextField(String.valueOf(value.width));
        ((PlainDocument) widthField.getDocument()).setDocumentFilter(new IntFilter());
        widthField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                value.width = Integer.parseInt(widthField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (widthField.getText().isEmpty()) value.width = 0;
                else value.width = Integer.parseInt(widthField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (widthField.getText().isEmpty()) value.width = 0;
                else value.width = Integer.parseInt(widthField.getText());
            }
        });
        heightField = new JTextField(String.valueOf(value.height));
        ((PlainDocument) heightField.getDocument()).setDocumentFilter(new IntFilter());
        heightField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                value.height = Integer.parseInt(heightField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (heightField.getText().isEmpty()) value.height = 0;
                else value.height = Integer.parseInt(heightField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (heightField.getText().isEmpty()) value.height = 0;
                else value.height = Integer.parseInt(heightField.getText());
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
        xW.add(new JLabel(Srd.LANG.getProperty("misc_width") + " "), BorderLayout.WEST);
        JPanel wP = new JPanel();
        wP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        wP.add(widthField, gbc);
        xW.add(wP);

        JPanel yH = new JPanel();
        yH.setLayout(new BorderLayout());
        yH.add(new JLabel(" " + Srd.LANG.getProperty("misc_height") + " "), BorderLayout.WEST);
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
        widthField.setText(String.valueOf(((Dimension) value).width));
        heightField.setText(String.valueOf(((Dimension) value).height));
        return super.setValue(value);
    }
}
