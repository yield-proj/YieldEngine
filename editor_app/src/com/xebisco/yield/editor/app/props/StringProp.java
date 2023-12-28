package com.xebisco.yield.editor.app.props;

import javax.swing.*;

public class StringProp extends Prop {
    private final String string;

    public StringProp(String string) {
        super(null, null);
        this.string = string;
    }

    @Override
    public JComponent render() {
        return new JLabel(string, null, SwingConstants.CENTER);
    }
}
