package com.xebisco.yieldengine.uiutils.props;

import com.xebisco.yieldengine.uiutils.Srd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class SettingsPropDialog extends JDialog {
    public SettingsPropDialog(Frame owner, Map<String, Prop[]> sections, Runnable apply) {
        super(owner, true);

        setTitle("Settings");
        JPanel values = new JPanel();
        values.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        values.setLayout(new BorderLayout());

        JScrollPane valuesPane = new JScrollPane(values);
        valuesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        valuesPane.setBorder(null);
        add(valuesPane);

        JList<String> sectionsList = new JList<>(sections.keySet().toArray(new String[0]));
        sectionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sectionsList.addListSelectionListener(e -> {
            values.removeAll();
            values.add(new PropPanel(sections.get(sectionsList.getSelectedValue())));
            values.updateUI();
        });
        JScrollPane sectionsPane = new JScrollPane(sectionsList);
        sectionsPane.setBorder(null);
        add(sectionsPane, BorderLayout.WEST);

        setMinimumSize(new Dimension(500, 600));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = new JButton(new AbstractAction(Srd.LANG.getProperty("misc_apply")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply.run();
            }
        });
        JButton cancelButton = new JButton(new AbstractAction(Srd.LANG.getProperty("misc_cancel")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JButton okButton = new JButton(new AbstractAction(Srd.LANG.getProperty("misc_ok")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyButton.getAction().actionPerformed(null);
                dispose();
            }
        });
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        getRootPane().setDefaultButton(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        sectionsList.setSelectedIndex(0);
        setVisible(true);
    }
}
