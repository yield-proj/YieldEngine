package com.xebisco.yield.editor.app;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import java.io.IOException;
import java.util.Locale;

public class Entry {
    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        System.setProperty("sun.java2d.opengl", "True");
        Srd.LANG.load(Entry.class.getResourceAsStream("langs/en.properties"));

        FlatMacDarkLaf.setup();

        EditorWindow window = new EditorWindow();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}