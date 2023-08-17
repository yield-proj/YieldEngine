package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.Assets;
import com.xebisco.yield.editor.TextShowPanel;

import javax.swing.*;
import java.awt.*;

public class TextShowProp extends Prop {

    private final String text;

    public TextShowProp(String text) {
        super(null, null);
        this.text = text;
    }

    @Override
    public JPanel panel() {
        return new TextShowPanel(text).panel();
    }
}
