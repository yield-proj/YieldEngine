package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.lang.reflect.Array;

public class ArrayProp<T extends Prop> extends Prop {
    private final JTextField sizeField;

    private final Class<T> type;

    public ArrayProp(String name, T[] value) {
        super(name, value);
        //noinspection unchecked
        type = (Class<T>) value.getClass().arrayType();
        sizeField = new JTextField(String.valueOf(value.length));
        ((PlainDocument) sizeField.getDocument()).setDocumentFilter(new IntFilter());
        sizeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateArraySize(Integer.parseInt(sizeField.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateArraySize(Integer.parseInt(sizeField.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateArraySize(Integer.parseInt(sizeField.getText()));
            }
        });
    }

    private void updateArraySize(int nsize) {
        //noinspection unchecked
        T[] old = (T[]) value;
        //noinspection unchecked
        setValue((T[]) Array.newInstance(type, nsize));
        for(int i = 0; i < old.length; i++) {
            if(i >= ((Object[]) value).length) break;
            ((Object[]) value)[i] = old[i];
        }
        sizeField.updateUI();
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(top.getBackground().brighter());
        String text = Srd.LANG.getProperty(name());
        if(text == null) text = name();
        top.add(new JLabel(text), BorderLayout.WEST);
        top.add(sizeField);
        panel.add(top, BorderLayout.NORTH);
        //noinspection unchecked
        panel.add(new PropPanel((T[]) value));
        return panel;
    }
}
