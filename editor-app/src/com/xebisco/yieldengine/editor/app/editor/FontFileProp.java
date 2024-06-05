package com.xebisco.yieldengine.editor.app.editor;

import com.xebisco.yieldengine.uiutils.props.PathProp;
import com.xebisco.yieldengine.uiutils.props.Prop;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontFileProp extends PathProp {
    private final JLabel fontLabel = new JLabel();
    public final static Pattern SIZEP = Pattern.compile("^(.*)\\s*,\\s*([0-9]+)$");

    public FontFileProp(String name, String value, FileSystemView fileSystemView, boolean prettyString) {
        super(name, value, new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".ttf");
            }

            @Override
            public String getDescription() {
                return "TTF Files";
            }
        }, fileSystemView, prettyString);
        field().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFont(field().getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFont(field().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFont(field().getText());
            }
        });
        updateFont(value);
    }

    private void updateFont(String path) {
        try {
            Matcher m = SIZEP.matcher(path);
            if (!m.find()) throw new FontFormatException("wrong formatting");
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(m.group(1))).deriveFont(Float.parseFloat(m.group(2)));
            fontLabel.setPreferredSize(new Dimension(100, 60));
            fontLabel.setFont(font);
            fontLabel.setBorder(BorderFactory.createTitledBorder(new File(path).getName()));
            fontLabel.setText(font.getFontName());
        } catch (IOException | NullPointerException | FontFormatException e) {
            fontLabel.setFont(null);
            fontLabel.setBorder(BorderFactory.createTitledBorder("NONE"));
            fontLabel.setText("");
        }
        fontLabel.repaint();
        fontLabel.revalidate();
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        JComponent s = super.render();
        s.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.add(s);
        JPanel m = new JPanel(new BorderLayout());
        m.add(panel);
        m.add(fontLabel, BorderLayout.WEST);
        return m;
    }

    @Override
    public Prop setValue(Serializable value) {
        Matcher m = SIZEP.matcher((String) value);
        if (!m.find() && !((String) value).isEmpty() && !value.equals("null")) value += ", 32";
        Serializable finalValue = value;
        SwingUtilities.invokeLater(() -> {
            if (fontLabel != null)
                updateFont((String) finalValue);
        });
        return super.setValue(value);
    }
}
