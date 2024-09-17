package com.xebisco.yieldengine.tilemapeditor;

import com.xebisco.yieldengine.tilemapeditor.editor.TileMapEditor;
import com.xebisco.yieldengine.tilemapeditor.map.TileMap;
import com.xebisco.yieldengine.tileseteditor.editor.TileSetEditor;
import com.xebisco.yieldengine.uiutils.Point;
import com.xebisco.yieldengine.uiutils.Utils;
import com.xebisco.yieldengine.uiutils.fields.PointFieldPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TileMap tileMap = new TileMap(new Point<>(100, 100), new Point<>(16, 16));

        tileMapEditor(tileMap, null);
    }

    public static final String TITLE = "Yield TileMap Editor (DEV0) [@]";

    public static TileMapEditor tileMapEditor(TileMap tileMap,  File file) {
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

        TileMapEditor editor = new TileMapEditor(tileMap);
        editor.setDefaultSaveFile(file);

        frame.add(editor);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        fileMenu.setMnemonic('F');

        JMenu newMenu = new JMenu("New");
        newMenu.setMnemonic('N');

        JMenuItem newTileMapItem = new JMenuItem(new AbstractAction("TileMap...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, Serializable> values = Utils.showOptions("New TileMap", frame, false,
                        new PointFieldPanel<>("map_size", Integer.class, new Point<>(100, 100), false, true, true),
                        new PointFieldPanel<>("map_grid_size", Integer.class, new Point<>(16, 16), false, true, true)
                );
                //noinspection unchecked
                tileMapEditor(new TileMap((Point<Integer>) values.get("map_size"), (Point<Integer>) values.get("map_grid_size")), null);
            }
        });
        newMenu.add(newTileMapItem);

        fileMenu.add(newMenu);

        JMenu openMenu = new JMenu("Open");
        openMenu.setMnemonic('O');

        JMenuItem openTileMapItem = new JMenuItem(new AbstractAction("TileMap...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = TileSetEditor.getTetsFileChooser();
                if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try(ObjectInputStream oi = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
                        tileMapEditor((TileMap) oi.readObject(), file);
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        openMenu.add(openTileMapItem);

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

        menuBar.add(editor.getViewMenu());

        menuBar.add(editor.getTileMapMenu());




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
