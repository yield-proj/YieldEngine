package com.xebisco.yieldengine.uiutils.fields;

import com.xebisco.yieldengine.uiutils.Lang;
import com.xebisco.yieldengine.uiutils.Out;
import com.xebisco.yieldengine.uiutils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

import static com.xebisco.yieldengine.uiutils.Lang.getString;

public class FilePanel extends FieldPanel<File> {
    private File value;
    private final JButton fileNameButton;

    public FilePanel(String name, File value, FileExtensions fileExtensions, boolean editable) {
        super(name, editable);
        this.value = value;

        fileNameButton = new JButton();

        fileNameButton.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    desktop.open(FilePanel.this.value);
                } catch (Exception ex) {
                    Out.error(ex.getMessage());
                }
            }
        });

        fileNameButton.setBorderPainted(false);
        fileNameButton.setContentAreaFilled(false);
        fileNameButton.setMinimumSize(new Dimension(0, 0));

        setValue(value);

        setLayout(new BorderLayout());
        add(new JLabel(getString(name) + ": "), BorderLayout.WEST);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));

        p.add(fileNameButton);
        p.add(Box.createHorizontalGlue());
        JButton editFileButton = new JButton(Lang.getString("_edit"));
        editFileButton.setEnabled(editable);
        editFileButton.addActionListener(e -> {
            File f = Utils.getFile(getRootPane(), FilePanel.this.value, fileExtensions);
            if (f != null) {
                setValue(f);
            }
            FilePanel.this.repaint();
        });
        p.add(editFileButton);

        add(p);
    }

    @Override
    public File getValue() {
        return value;
    }

    @Override
    public void setValue(File value) {
        fileNameButton.setText(value.getAbsolutePath());
        this.value = value;
    }
}
