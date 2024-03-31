package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

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
        JPanel b = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        b.add(new JButton(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                File start;
                if (value == null) start = null;
                else start = new File((String) value);
                JFileChooser fileChooser;
                if (start != null && start.exists() && start.isDirectory()) fileChooser = new JFileChooser(start);
                else fileChooser = new JFileChooser(new File(""));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(o) == JFileChooser.APPROVE_OPTION) {
                    setValue(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        }), gbc);
        b.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        o.add(b, BorderLayout.EAST);
        return o;
    }
}
