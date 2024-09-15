package com.xebisco.yieldengine.tilemapeditor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.xebisco.yieldengine.tilemapeditor.editor.TileSetEditor;
import com.xebisco.yieldengine.tilemapeditor.tile.TileSet;
import com.xebisco.yieldengine.uiutils.Lang;
import com.xebisco.yieldengine.uiutils.fields.FieldsPanel;

import javax.swing.*;
import java.io.IOException;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //System.setProperty("sun.java2d.opengl", "True");
        Locale.setDefault(Locale.US);
        FlatDarkLaf.setup();
        Lang.LANG.load(Main.class.getResourceAsStream("/en.properties"));

        //UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());

        testTileSetEditor();
    }
    static FieldsPanel fieldsPanel;

    public static void testTileSetEditor() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tile Set Test");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        TileSetEditor editor = new TileSetEditor(new TileSet());

        frame.add(editor);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editor.getTileSetMenu());
        frame.setJMenuBar(menuBar);

        /*JPanel fieldsPanelPanel = new JPanel(new BorderLayout());
        frame.add(fieldsPanelPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        bottom.add(new JButton(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldsPanel.saveToObject(imageTile);
            }
        }));

        bottom.add(new JButton(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldsPanelPanel.removeAll();
                fieldsPanel = FieldsPanel.fromClass(ImageTile.class, imageTile);
                fieldsPanelPanel.add(fieldsPanel);
                fieldsPanelPanel.revalidate();
            }
        }));

        frame.add(bottom, BorderLayout.SOUTH);*/


        frame.setVisible(true);
    }
}