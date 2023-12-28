package com.xebisco.yield.editor.app.props;

import com.xebisco.yield.editor.app.Srd;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class PathProp extends TextFieldProp {
    private final FileFilter filter;

    public PathProp(String name, String value, FileFilter filter) {
        super(name, value);
        this.filter = filter;
    }

    @Override
    public JComponent render() {
        JComponent o = super.render();
        JPanel b = new JPanel();
        b.add(new JButton(new AbstractAction(Srd.LANG.getProperty("misc_load")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                File start = new File((String) value);
                JFileChooser fileChooser;
                if (start.exists() && start.isDirectory()) fileChooser = new JFileChooser(start);
                else fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(o) == JFileChooser.APPROVE_OPTION) {
                    setValue(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        }));
        o.add(b, BorderLayout.EAST);
        return o;
    }
}
