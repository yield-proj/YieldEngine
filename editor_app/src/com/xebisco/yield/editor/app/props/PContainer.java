package com.xebisco.yield.editor.app.props;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PContainer extends ContainerProp {
    private final int paragraphLength;

    public PContainer(int paragraphLength) {
        this.paragraphLength = paragraphLength;
    }

    @Override
    public JPanel render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, paragraphLength, 0, 0));
        return panel;
    }
}
