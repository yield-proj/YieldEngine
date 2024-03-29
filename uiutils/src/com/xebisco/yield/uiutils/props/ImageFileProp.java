package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFileProp extends PathProp {
    private final JLabel imageLabel = new JLabel();

    public ImageFileProp(String name, String value) {
        super(name, value, new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".png") || f.getName().endsWith(".jpg") || f.getName().endsWith(".jpeg");
            }

            @Override
            public String getDescription() {
                return "Image Files";
            }
        });
        field().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateImage(field().getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateImage(field().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateImage(field().getText());
            }
        });
        updateImage(value);
    }

    private void updateImage(String path) {
        try {
            imageLabel.setBorder(BorderFactory.createTitledBorder(new File(path).getName()));
            imageLabel.setIcon(new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(32, -1, Image.SCALE_SMOOTH)));
        } catch (IOException | NullPointerException e) {
            imageLabel.setBorder(BorderFactory.createTitledBorder("NONE"));
            BufferedImage i = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics g = i.createGraphics();
            UIManager.getIcon("OptionPane.errorIcon").paintIcon(imageLabel, g, 32, 32);
            g.dispose();
            imageLabel.setIcon(new ImageIcon(i));
        }
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(super.render(), BorderLayout.NORTH);
        JPanel m = new JPanel(new BorderLayout());
        m.add(panel);
        m.add(imageLabel, BorderLayout.WEST);
        return m;
    }
}
