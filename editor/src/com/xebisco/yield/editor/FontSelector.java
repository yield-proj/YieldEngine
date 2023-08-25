package com.xebisco.yield.editor;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class FontSelector extends JDialog {
    private final JList<String> list = new JList<>();
    private final JTextField fontFamily = new JTextField();
    private int size = 20;
    private final JCheckBox bold = new JCheckBox("Bold"), italic = new JCheckBox("Italic");
    private Font font, actFont;

    public static Font open(Font selected, Component c) {
        FontSelector f = new FontSelector(selected);
        f.setLocationRelativeTo(c);
        f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        f.setVisible(true);
        return f.actFont;
    }

    private FontSelector(Font selected) {
        setModal(true);
        actFont = selected;
        if(selected != null) {
            fontFamily.setText(selected.getFamily());
            size = selected.getSize();
            bold.setSelected(selected.isBold());
            italic.setSelected(selected.isItalic());
        }
        bold.addChangeListener(e -> FontSelector.this.repaint());
        italic.addChangeListener(e -> FontSelector.this.repaint());
        setTitle("Font Selector");
        setIconImage(Assets.images.get("yieldIcon.png").getImage());
        setLayout(new BorderLayout());
        updateList("");
        list.addListSelectionListener(e -> {
            fontFamily.setText(list.getSelectedValue());
            FontSelector.this.repaint();
        });
        fontFamily.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateList(fontFamily.getText());
            }
        });
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add(fontFamily);
        add(top, BorderLayout.NORTH);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                setPreferredSize(new Dimension(100, size + 10));
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setColor(getForeground());
                g.setFont(font = new Font(fontFamily.getText(), (bold.isSelected() ? Font.BOLD : Font.PLAIN) | (italic.isSelected() ? Font.ITALIC : Font.PLAIN), size));
                g.drawString("Hello, World!", 0, getHeight() / 2 + size / 2);
            }
        };
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(panel);
        JButton button = new JButton(new AbstractAction("Apply") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actFont = font;
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);
        buttonPanel.add(new JButton(new AbstractAction("Select null") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actFont = null;
                dispose();
            }
        }));
        p1.add(buttonPanel, BorderLayout.EAST);
        getRootPane().setDefaultButton(button);
        add(p1, BorderLayout.SOUTH);
        JPanel optPanel = new JPanel();
        optPanel.add(bold);
        optPanel.add(italic);
        JSpinner spinner = intSpinner();
        spinner.setValue(size);
        spinner.addChangeListener(e -> {
            size = (int) spinner.getValue();
            FontSelector.this.repaint();
            FontSelector.this.validate();
        });
        optPanel.add(spinner);
        top.add(optPanel, BorderLayout.EAST);
        pack();
        setMinimumSize(new Dimension(400, 300));
    }

    private static JSpinner intSpinner() {
        List<Integer> sizes = new ArrayList<>();
        for(int i = 1; i <= 128; i++) sizes.add(i);
        return new JSpinner(new SpinnerListModel(sizes));
    }

    private void updateList(String search) {
        List<String> fonts = new ArrayList<>();
        for(String f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if(f.contains(search))
                fonts.add(f);
        }
        list.setModel(new AbstractListModel<>() {
            @Override
            public int getSize() {
                return fonts.size();
            }

            @Override
            public String getElementAt(int index) {
                return fonts.get(index);
            }
        });
        repaint();
        validate();
    }
}
