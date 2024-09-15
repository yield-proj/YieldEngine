package com.xebisco.yieldengine.uiutils;

import com.xebisco.yieldengine.uiutils.fields.*;
import sun.reflect.annotation.AnnotationParser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final FileExtensions IMAGE_FILE_EXTENSIONS = getFileExtensionsInstance(new String[]{"PNG", "JPG", "JPEG", "BMP", "WBMP", "GIF"}, "Image Files");

    public static <T extends Annotation> T getAnnotationInstance(Class<T> annotationClass, Map<String, Object> values) {
        //noinspection unchecked
        return (T) AnnotationParser.annotationForMap(annotationClass, values);
    }

    public static FileExtensions getFileExtensionsInstance(String[] extensions, String description) {
        Map<String, Object> values = new HashMap<>();
        values.put("extensions", extensions);
        values.put("description", description);
        return getAnnotationInstance(FileExtensions.class, values);
    }

    public static HashMap<String, Serializable> showOptions(String title, Window owner, boolean showApplyButton, FieldPanel<?>... fieldPanels) {
        FieldsPanel fieldsPanel = new FieldsPanel(fieldPanels);

        FieldsDialog dialog = new FieldsDialog(owner, title, fieldsPanel);
        dialog.setTitle(title);
        dialog.setUndecorated(true);

        //noinspection unchecked
        final HashMap<String, Serializable>[] values = new HashMap[]{fieldsPanel.getMap()};

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                values[0] = fieldsPanel.getMap();
                dialog.dispose();
            }
        });

        buttonPanel.add(okButton);
        dialog.getRootPane().setDefaultButton(okButton);

        if(showApplyButton) {
            JButton applyButton = new JButton(new AbstractAction("Apply") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    values[0] = fieldsPanel.getMap();
                }
            });
        }

        JButton cancelButton = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!showApplyButton) values[0] = null;
                dialog.dispose();
            }
        });

        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(fieldsPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);

        return values[0];
    }

    public static HashMap<String, Serializable> showOptions(Window owner, FieldPanel<?>... fieldPanels) {
        return showOptions(null, owner, true, fieldPanels);
    }

    public static JButton menuItemButton(String name, Integer mnemonic, JComponent... menuItem) {
        JButton button = new JButton();
        if (mnemonic != null) button.setMnemonic(mnemonic);

        JPopupMenu popupMenu = new JPopupMenu();
        for (JComponent item : menuItem) {
            popupMenu.add(item);
        }

        button.setAction(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.show(button, button.getMousePosition().x, button.getMousePosition().y);
            }
        });

        return button;
    }

    public static File getFile(JRootPane rootPane, File currentDir, FileExtensions fileExtensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileExtensions != null) {
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    for (String ext : fileExtensions.extensions()) {
                        if (f.getName().toUpperCase().endsWith(ext.toUpperCase())) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return fileExtensions.description();
                }
            });
        }

        chooser.setCurrentDirectory(currentDir);

        if (chooser.showOpenDialog(rootPane) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        return null;
    }
}
