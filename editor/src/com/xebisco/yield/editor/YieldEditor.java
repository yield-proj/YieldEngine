package com.xebisco.yield.editor;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class YieldEditor {

    private static File project;
    private final JFrame frame = new JFrame();
    private static boolean ASSETS_LOADED;

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        File project;
        if (args.length != 1) {
            if (args.length == 0) project = null;
            else {
                JOptionPane.showMessageDialog(null, "Wrong argument number", "Launch Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Wrong argument number");
            }
        } else {

            project = new File(args[0]);

            if (!project.exists()) {
                JOptionPane.showMessageDialog(null, "Project does not exist", "Launch Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Project does not exist");
            }

            if (project.isDirectory())
                project = new File(project, "project.ser");
            if (!project.exists()) {
                JOptionPane.showMessageDialog(null, "Is not a project", "Launch Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Is not a project");
            }
        }

        new YieldEditor(project);
    }

    public static void loadAssets() {
        JFrame splash = new JFrame();
    }

    public YieldEditor(File projectFile) {
        if(!ASSETS_LOADED) {
            loadAssets();
            ASSETS_LOADED = true;
        }
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Yield Editor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
