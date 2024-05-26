package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.file.DirectoryRestrictedFileSystemView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import java.nio.file.FileSystem;

public class PathProp extends TextFieldProp {
    private final FileFilter filter;
    private FileSystemView fileSystemView;

    public PathProp(String name, String value, FileFilter filter, FileSystemView fileSystemView, boolean prettyString) {
        super(name, value, prettyString);
        if (value != null) {
            String p = value;
            if (p.startsWith(fileSystemView.getHomeDirectory().getAbsolutePath()))
                p = value.substring(fileSystemView.getHomeDirectory().getAbsolutePath().length() + 1);
            setValue(p);
        }
        this.filter = filter;
        this.fileSystemView = fileSystemView;
    }

    public PathProp(String name, String value, FileFilter filter, boolean prettyString) {
        this(name, value, filter, FileSystemView.getFileSystemView(), prettyString);
    }

    @Override
    public JComponent render() {
        JComponent o = super.render();
        JPanel b = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        b.add(new JButton(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(fileSystemView.getHomeDirectory(), fileSystemView);
                fileChooser.setMultiSelectionEnabled(false);
                for (FileFilter f : fileChooser.getChoosableFileFilters()) {
                    fileChooser.removeChoosableFileFilter(f);
                }
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return filter.accept(f) || f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return filter.getDescription();
                    }
                });
                if (fileChooser.showOpenDialog(o) == JFileChooser.APPROVE_OPTION) {
                    if (!fileChooser.getSelectedFile().exists() || !filter.accept(fileChooser.getSelectedFile())) {
                        JOptionPane.showMessageDialog(null, "File is not valid", "File Load Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String p = fileChooser.getSelectedFile().getAbsolutePath();
                        if (p.startsWith(fileSystemView.getHomeDirectory().getAbsolutePath()))
                            p = p.substring(fileSystemView.getHomeDirectory().getAbsolutePath().length() + 1);
                        setValue(p);
                    }
                }
            }
        }), gbc);
        b.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        o.add(b, BorderLayout.EAST);
        return o;
    }

    @Override
    public Serializable value() {
        if (value == null || ((String) value).startsWith("null,") || ((String) value).startsWith("null ,")) return null;
        return value;
    }

    public FileFilter filter() {
        return filter;
    }

    public FileSystemView fileSystemView() {
        return fileSystemView;
    }

    public PathProp setFileSystemView(FileSystemView fileSystemView) {
        this.fileSystemView = fileSystemView;
        return this;
    }
}
