package com.xebisco.yieldengine.tilemapeditor.editor;

import com.xebisco.yieldengine.tilemapeditor.map.TileMap;
import com.xebisco.yieldengine.tilemapeditor.tools.SettingsPanel;
import com.xebisco.yieldengine.tilemapeditor.tools.ToolsPanel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class TileMapEditor extends JPanel {
    private final TileMap tileMap;
    private File defaultSaveFile;

    private final ToolsPanel toolsPanel = new ToolsPanel();
    private final SettingsPanel settingsPanel = new SettingsPanel();
    private final MapEditorPanel mapEditorPanel;

    public TileMapEditor(TileMap tileMap) {
        super(new BorderLayout());
        this.tileMap = tileMap;

        JSplitPane toolsMapSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, toolsPanel, mapEditorPanel = new MapEditorPanel(tileMap));

        JSplitPane viewSettingsSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, toolsMapSplit, settingsPanel);
        viewSettingsSplit.setResizeWeight(1);

        add(viewSettingsSplit, BorderLayout.CENTER);
    }

    public JMenu getViewMenu() {
        JMenu menu = new JMenu("View");

        JCheckBox showGrid = new JCheckBox("Show Grid");
        showGrid.setSelected(true);
        showGrid.addActionListener(e -> mapEditorPanel.setShowGrid(showGrid.isSelected()));

        menu.add(showGrid);

        return menu;
    }

    public JMenu getTileMapMenu() {
        JMenu menu = new JMenu("TileMap");

        return menu;
    }

    public void saveToDefault() throws IOException {
        if (defaultSaveFile != null) {
            save(defaultSaveFile);
        } else {
            save(null);
        }
    }

    public static JFileChooser getTetmFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".tetm") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Tile Editor TileMap (.tetm)";
            }
        });
        return chooser;
    }

    public void save(File to) throws IOException {
        if (to == null) {
            JFileChooser chooser = getTetmFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                defaultSaveFile = chooser.getSelectedFile();
                to = defaultSaveFile;
            } else {
                JOptionPane.showMessageDialog(this, "Could not save.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try (ObjectOutputStream oo = new ObjectOutputStream(Files.newOutputStream(to.toPath()))) {
            oo.writeObject(tileMap);
        }
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public File getDefaultSaveFile() {
        return defaultSaveFile;
    }

    public void setDefaultSaveFile(File defaultSaveFile) {
        this.defaultSaveFile = defaultSaveFile;
    }
}
