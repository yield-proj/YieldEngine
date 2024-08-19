package com.xebisco.yieldengine.uiutils;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberField extends JFormattedTextField {
    public NumberField(Class<? extends Number> numberClass, boolean allowNegatives) {
        super(getNumberFormatter(numberClass, allowNegatives));
        setText("0");

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals("0")) {
                    setText("");
                } else {
                    int caret = getCaretPosition();
                    SwingUtilities.invokeLater(() -> {
                        setCaretPosition(caret);
                    });
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("0");
                }
            }
        });
    }

    private static NumberFormatter getNumberFormatter(Class<? extends Number> numberClass, boolean allowNegatives) {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance()) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if(text.isEmpty()) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        formatter.setValueClass(numberClass);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        if (!allowNegatives) {
            formatter.setMinimum(0);
        }

        return formatter;
    }
}
