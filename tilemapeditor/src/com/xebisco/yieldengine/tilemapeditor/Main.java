package com.xebisco.yieldengine.tilemapeditor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.xebisco.yieldengine.tilemapeditor.editor.TileSetEditor;
import com.xebisco.yieldengine.tilemapeditor.tile.ImageTile;
import com.xebisco.yieldengine.tilemapeditor.tile.Tile;
import com.xebisco.yieldengine.tilemapeditor.tile.TileSet;
import com.xebisco.yieldengine.uiutils.Lang;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //System.setProperty("sun.java2d.opengl", "True");
        Locale.setDefault(Locale.US);
        FlatDarkLaf.setup();
        Lang.LANG.load(Main.class.getResourceAsStream("/en.properties"));

        //UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());

        TileSet tileSet = new TileSet();
        Tile tile = new ImageTile(new File("yieldIcon.png"), "yieldFile", null);
        tile.load();
        tileSet.getTiles().add(tile);
        tile = new ImageTile(new File("yieldIcon.png"), "yieldFile", null);
        tile.load();
        tileSet.getTiles().add(tile);

        tileSetEditor(tileSet, null);
    }

    public static final String TITLE = "Yield TileSet Editor (DEV0) [@]";

    public static TileSetEditor tileSetEditor(TileSet tileSet, File file) {
        JFrame frame = new JFrame();
        if(file == null) {
            frame.setTitle(TITLE);
        } else {
            frame.setTitle(TITLE.replace("@", file.getName()));
        }

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        TileSetEditor editor = new TileSetEditor(tileSet);
        editor.setDefaultSaveFile(file);

        frame.add(editor);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        fileMenu.setMnemonic('F');

        JMenu newMenu = new JMenu("New");
        newMenu.setMnemonic('N');

        JMenuItem newTileSetItem = new JMenuItem(new AbstractAction("TileSet...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileSetEditor(new TileSet(), null);
            }
        });
        newMenu.add(newTileSetItem);

        fileMenu.add(newMenu);

        JMenu openMenu = new JMenu("Open");
        openMenu.setMnemonic('O');

        JMenuItem openTileSetItem = new JMenuItem(new AbstractAction("TileSet...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = TileSetEditor.getTetsFileChooser();
                if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try(ObjectInputStream oi = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
                        tileSetEditor((TileSet) oi.readObject(), file);
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        openMenu.add(openTileSetItem);

        fileMenu.add(openMenu);

        fileMenu.addSeparator();

        JMenuItem saveItem = new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editor.saveToDefault();
                    frame.setTitle(TITLE.replace("@", editor.getDefaultSaveFile().getName()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        fileMenu.add(saveItem);

        JMenuItem saveToItem = new JMenuItem(new AbstractAction("Save To...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editor.save(null);
                    frame.setTitle(TITLE.replace("@", editor.getDefaultSaveFile().getName()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        fileMenu.add(saveToItem);




        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        exitItem.setMnemonic('X');

        fileMenu.add(exitItem);

        menuBar.add(fileMenu);


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
        return editor;
    }
}