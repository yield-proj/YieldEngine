package com.xebisco.yieldengine.tilemapeditor.editor;

import com.xebisco.yieldengine.tilemapeditor.Dialogs;
import com.xebisco.yieldengine.tilemapeditor.imagecutter.ImageCutterPanel;
import com.xebisco.yieldengine.tilemapeditor.tile.*;
import com.xebisco.yieldengine.uiutils.NumberField;
import com.xebisco.yieldengine.uiutils.Point;
import com.xebisco.yieldengine.uiutils.Utils;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;

public class TileSetEditor extends JPanel {

    private final TileSet tileSet;
    private final JPanel tileSetPanel = new JPanel(new BorderLayout());
    private final ImageCutterPanel imageCutterPanel;
    private File defaultSaveFile;

    static class TileSetEditorListCellRenderer extends JLabel implements ListCellRenderer<Tile> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Tile> list, Tile value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof FillerTile) {
                setVerticalTextPosition(JLabel.CENTER);
                setText("|FILLER|");
                setIcon(null);
                setEnabled(false);
            } else {
                setVerticalTextPosition(JLabel.BOTTOM);
                setEnabled(true);
                setIcon(new ImageIcon(value.get100pImage()));
                setText(value.getName() + " (" + value.getWidth() + "x" + value.getHeight() + ")");
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setOpaque(true);

            setHorizontalAlignment(JLabel.CENTER);
            setHorizontalTextPosition(JLabel.CENTER);

            setPreferredSize(new Dimension(120, 120));

            return this;
        }
    }

    static class TileJList extends JList<Tile> {
        public TileJList(Tile[] listData) {
            super(listData);
        }
    }

    class TileListPopupMouseListener extends MouseAdapter {
        private final JList<Tile> list;

        public TileListPopupMouseListener(JList<Tile> list) {
            this.list = list;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            popup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            popup(e);
        }

        private void popup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                list.setSelectedIndex(list.locationToIndex(e.getPoint()));

                JPopupMenu menu = new JPopupMenu(list.getSelectedValue().getName());

                JMenuItem nameItem = new JMenuItem(list.getSelectedValue().getName() + " (" + list.getSelectedValue().getWidth() + "x" + list.getSelectedValue().getHeight() + ")");
                nameItem.setEnabled(false);
                menu.add(nameItem);

                menu.addSeparator();

                menu.add(new AbstractAction("Name: " + list.getSelectedValue().getName()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField field = new JTextField(list.getSelectedValue().getName());
                        if (JOptionPane.showConfirmDialog(TileSetEditor.this, field, "Change Name", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            list.getSelectedValue().setName(field.getText());
                            reload();
                        }
                    }
                });

                menu.add(new AbstractAction("Index: " + list.getSelectedIndex()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        NumberField<Integer> field = new NumberField<>(Integer.class, false);
                        field.setValue(list.getSelectedIndex());
                        if (JOptionPane.showConfirmDialog(TileSetEditor.this, field, "Change Index", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            if (field.getValue() > list.getModel().getSize() - 1) {
                                field.setValue(list.getModel().getSize() - 1);
                            }
                            tileSet.getTiles().remove(list.getSelectedValue());
                            tileSet.getTiles().add(field.getValue(), list.getSelectedValue());
                            reload();
                        }
                    }
                });

                menu.add(new AbstractAction("Class: " + list.getSelectedValue().getEntityCreatorClassName()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField field = new JTextField(list.getSelectedValue().getEntityCreatorClassName());
                        if (JOptionPane.showConfirmDialog(TileSetEditor.this, field, "Change EntityCreatorClassName", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            list.getSelectedValue().setEntityCreatorClassName(field.getText());
                            reload();
                        }
                    }
                });

                menu.addSeparator();

                menu.add(new AbstractAction("New Tile Before") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newTile(list.getSelectedIndex());
                    }
                });

                menu.add(new AbstractAction("New Tile After") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newTile(list.getSelectedIndex() + 1);
                    }
                });

                menu.add(new AbstractAction("Remove") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tileSet.getTiles().remove(list.getSelectedIndex());
                        reload();
                    }
                });
                menu.add(new AbstractAction("Remove And Add Filler") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tileSet.getTiles().remove(list.getSelectedIndex());
                        tileSet.getTiles().add(list.getSelectedIndex(), new FillerTile());
                        reload();
                    }
                });
                menu.show(e.getComponent(), e.getX(), e.getY());
            }

        }

    }

    public TileSetEditor(TileSet tileSet) {
        super(new BorderLayout());
        this.tileSet = tileSet;

        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);

        tabbedPane.addTab("Tile Set", tileSetPanel);

        imageCutterPanel = new ImageCutterPanel(tileSet);
        tabbedPane.addTab("Image Sheet", imageCutterPanel);
        tabbedPane.addChangeListener(e -> {
            reload();
        });

        add(tabbedPane, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.add(new AbstractAction("Reload") {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
                imageCutterPanel.repaint();
            }
        });

        add(toolBar, BorderLayout.NORTH);

        reload();
    }

    public void saveToDefault() throws IOException {
        if (defaultSaveFile != null) {
            save(defaultSaveFile);
        } else {
            save(null);
        }
    }

    public void save(File to) throws IOException {
        if (to == null) {
            JFileChooser chooser = getTetsFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                defaultSaveFile = chooser.getSelectedFile();
                to = defaultSaveFile;
            } else {
                JOptionPane.showMessageDialog(this, "Could not save.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try (ObjectOutputStream oo = new ObjectOutputStream(Files.newOutputStream(to.toPath()))) {
            oo.writeObject(tileSet);
        }
    }

    public static JFileChooser getTetsFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".tets") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Tile Editor TileSet (.tets)";
            }
        });
        return chooser;
    }

    public void newTile(int index) {
        if (index < 0) index = tileSet.getTiles().size();

        //int tileType = JOptionPane.showOptionDialog(this, "Choose the Tile Type", null, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Image", "SubImage"}, "Image");

        //if(tileType == 0) {
        HashMap<String, Serializable> values = Utils.showOptions("New Image Tile", SwingUtilities.getWindowAncestor(this), false, Dialogs.newImageTileFields());

        if (values != null) {
            try {
                String name = (String) values.get(Dialogs.NAME);
                //if (name.isEmpty()) throw new IllegalArgumentException("Name is empty");
                Tile tile = new ImageTile((File) values.get(Dialogs.IMAGE_FILE), name, (String) values.get(Dialogs.ENTITY_CREATOR_CLASS_NAME));
                tile.load();
                tileSet.getTiles().add(index, tile);
                reload();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                newTile(index);
            }
        }
        /*} else if(tileType == 1) {
            ImageCutterPanel imageCutterPanel = new ImageCutterPanel(tileSet.getImageSheet(), tileSet.getTiles());
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
            dialog.setSize(500, 500);
            dialog.add(imageCutterPanel);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);*/
            /*FieldsPanel fieldsPanel = new FieldsPanel(Dialogs.newSubImageTileFields());

            boolean resp = FieldsDialog.create((Frame) SwingUtilities.getWindowAncestor(this), "New SubImage Tile", fieldsPanel);

            if (resp) {
                try {
                    HashMap<String, Serializable> values = fieldsPanel.getMap();
                    String name = (String) values.get(Dialogs.NAME);
                    if (name.isEmpty()) throw new IllegalArgumentException("Name is empty");
                    Tile tile = new ImageTile((File) values.get(Dialogs.IMAGE_FILE), name, (String) values.get(Dialogs.ENTITY_CREATOR_CLASS_NAME));
                    tile.load();
                    tileSet.getTiles().add(index, tile);
                    reload();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    newTile(index);
                }
            }
        }*/
    }

    public JMenu getTileSetMenu() {
        JMenu menu = new JMenu("TileSet");
        menu.setMnemonic('T');

        JMenuItem newTile = new JMenuItem(new AbstractAction("New Image Tile...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newTile(-1);
            }
        });

        newTile.setMnemonic('N');
        newTile.setAccelerator(KeyStroke.getKeyStroke("control N"));

        menu.add(newTile);

        menu.add(new JMenuItem(new AbstractAction("Delete All") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(TileSetEditor.this, "Delete all tiles?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    tileSet.getTiles().clear();
                    reload();
                }
            }
        }));

        menu.addSeparator();

        JMenuItem showImageSheet = new JMenuItem(new AbstractAction("Show Image Sheet...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(tileSet.getImageSheetFile());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        menu.add(showImageSheet);

        JMenuItem setImageSheetItem = new JMenuItem(new AbstractAction("Set Image Sheet...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = Utils.getFile(getRootPane(), tileSet.getImageSheetFile(), Utils.IMAGE_FILE_EXTENSIONS);
                if (f != null)
                    tileSet.setImageSheetFile(f);
                reload();
            }
        });
        setImageSheetItem.setAccelerator(KeyStroke.getKeyStroke("control shift I"));

        menu.add(setImageSheetItem);

        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showImageSheet.setEnabled(tileSet.getImageSheetFile() != null);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        return menu;
    }

    public void reload() {
        tileSetPanel.removeAll();

        try {
            tileSet.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JList<Tile> tileList = new TileJList(tileSet.getTiles().toArray(new Tile[0]));
        tileList.addMouseListener(new TileListPopupMouseListener(tileList));
        tileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tileList.setCellRenderer(new TileSetEditorListCellRenderer());
        tileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tileList.setVisibleRowCount(0);

        JScrollPane scrollPane = new JScrollPane(tileList);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        tileSetPanel.add(scrollPane, BorderLayout.CENTER);

        tileSetPanel.updateUI();
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public JPanel getTileSetPanel() {
        return tileSetPanel;
    }

    public ImageCutterPanel getImageCutterPanel() {
        return imageCutterPanel;
    }

    public File getDefaultSaveFile() {
        return defaultSaveFile;
    }

    public void setDefaultSaveFile(File defaultSaveFile) {
        this.defaultSaveFile = defaultSaveFile;
    }
}
