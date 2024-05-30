package com.xebisco.yieldengine.uiutils.props;

import com.xebisco.yieldengine.uiutils.Srd;

import javax.swing.*;
import java.awt.*;

public class SlideProp extends Prop {

    private final int min, max;

    public SlideProp(String name, int value, int min, int max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        String text = Srd.LANG.getProperty(name());
        if (text == null) text = name();
        panel.add(new JLabel(text + " "), BorderLayout.WEST);
        JPanel sPanel = new JPanel(new BorderLayout());
        JSlider slider = new JSlider(min, max, (int) value);
        slider.addChangeListener(e -> setValue(slider.getValue()));
        sPanel.add(new JLabel(String.valueOf(min)), BorderLayout.WEST);
        sPanel.add(slider);
        sPanel.add(new JLabel(String.valueOf(max)), BorderLayout.EAST);
        panel.add(sPanel);
        return panel;
    }
}
