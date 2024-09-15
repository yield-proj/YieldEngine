package com.xebisco.yieldengine.uiutils;

import com.xebisco.yieldengine.uiutils.fields.FieldsPanel;

import javax.swing.*;
import java.awt.*;

public class FieldsDialog extends JDialog {


    public FieldsDialog(Window owner, String name, FieldsPanel fieldsPanel) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        setTitle("");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel top = new JPanel();
        JLabel titleLabel = new JLabel("<html><h1>" + name + "</h1></html>");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titleLabel.setOpaque(false);
        top.setBackground(titleLabel.getBackground().darker());
        top.add(titleLabel);
        add(top, BorderLayout.NORTH);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(fieldsPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(owner);
    }

    /*
    public static boolean create(Frame owner, String name, FieldsPanel fieldsPanel) {
        FieldsDialog dialog = new FieldsDialog(owner, name, fieldsPanel);
        JPanel bottom = new JPanel();
        dialog.add(bottom, BorderLayout.SOUTH);
        boolean[] resp = new boolean[1];
        JButton okButton = new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                resp[0] = true;
                dialog.dispose();
            }
        });
        bottom.add(okButton);
        bottom.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }));
        dialog.pack();
        bottom.getRootPane().setDefaultButton(okButton);
        dialog.setVisible(true);
        return resp[0];
    }
    */

}
