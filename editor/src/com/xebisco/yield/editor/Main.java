package com.xebisco.yield.editor;

import com.formdev.flatlaf.FlatDarkLaf;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        Splash splash;
        try {
            splash = new Splash();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        splash.setVisible(true);
        splash.dispose();
        new ProjectManager().setVisible(true);
    }
}
