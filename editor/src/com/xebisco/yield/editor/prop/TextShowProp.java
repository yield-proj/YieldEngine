package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.Assets;
import com.xebisco.yield.editor.TextShowPanel;

import javax.swing.*;
import java.awt.*;

public class TextShowProp extends Prop {

    private final String text;
    private final boolean lang;

    public TextShowProp(String text, boolean lang) {
        super(null, null);
        this.text = text;
        this.lang = lang;
    }

    @Override
    public JPanel panel() {
        return new TextShowPanel(text, lang).panel();
    }
}
