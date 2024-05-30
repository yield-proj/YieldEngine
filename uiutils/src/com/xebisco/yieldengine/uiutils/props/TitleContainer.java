package com.xebisco.yieldengine.uiutils.props;

import javax.swing.*;

public class TitleContainer extends PContainer {

    private final String title;

    public TitleContainer(String title) {
        super(50);
        this.title = title;
    }

    @Override
    public JPanel render() {
        JPanel panel = super.render();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }
}
