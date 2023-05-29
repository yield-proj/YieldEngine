/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.editor;

import com.xebisco.yield.ini.Ini;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropsWindow extends JDialog {

    private static final Pattern keyPattern = Pattern.compile("^([^(]+)\\(([^)]*)\\)$");

    public PropsWindow(Ini ini, File iniFile, Runnable apply, Frame owner) {
        super(owner);
        try {
            ini.load(new FileInputStream(iniFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        setTitle("New Project");
        setModal(true);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        setSize(600, 400);
        setLocationRelativeTo(owner);

        contentPane.setLayout(new BorderLayout());

        List<JButton> sections = new ArrayList<>();
        JList<JButton> sectionsJL;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane pane = new JScrollPane();
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        for (String section : ini.getSections().keySet()) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            gbc.gridy = 0;
            for (Object k : ini.getSections().get(section).keySet()) {
                String sk = ((String) k).replace("__", "\12").replace("_", " ").replace("\12", "_");
                Matcher matcher = keyPattern.matcher(sk);
                matcher.matches();

                PropType pt = null;
                try {
                    pt = PropType.valueOf(matcher.group(2));
                } catch (IllegalArgumentException ignore) {
                }
                JComponent comp = null;
                if (pt == null) {
                    gbc.gridx = 0;
                    panel.add(new JLabel(matcher.group(1)), gbc);
                    gbc.gridx = 1;
                    comp = new JComboBox<>(matcher.group(2).split(";"));
                    //noinspection unchecked
                    ((JComboBox<String>) comp).setSelectedItem(ini.getSections().get(section).get(k));
                    //noinspection unchecked
                    JComboBox<String> finalComp2 = (JComboBox<String>) comp;
                    //noinspection unchecked
                    ((JComboBox<String>) comp).addActionListener(e -> ini.getSections().get(section).replace(k, String.valueOf(finalComp2.getSelectedItem())));
                } else
                    switch (pt) {
                        case STR:
                            gbc.gridx = 0;
                            panel.add(new JLabel(matcher.group(1)), gbc);
                            gbc.gridx = 1;
                            comp = new JTextField(ini.getSections().get(section).getProperty(sk));
                            comp.setToolTipText(ini.getSections().get(section).getProperty(sk, sk));
                            JTextField finalComp = (JTextField) comp;
                            ((JTextField) comp).getDocument().addDocumentListener(new DocumentListener() {
                                @Override
                                public void insertUpdate(DocumentEvent e) {
                                    ini.getSections().get(section).replace(k, finalComp.getText());
                                }

                                @Override
                                public void removeUpdate(DocumentEvent e) {
                                    ini.getSections().get(section).replace(k, finalComp.getText());
                                }

                                @Override
                                public void changedUpdate(DocumentEvent e) {
                                    ini.getSections().get(section).replace(k, finalComp.getText());
                                }
                            });
                            break;
                        case BOOL:
                            gbc.gridx = 1;
                            comp = new JCheckBox(matcher.group(1), ini.getSections().get(section).getBoolean(String.valueOf(k)));
                            ((JCheckBox) comp).addItemListener(e -> ini.getSections().get(section).replace(k, String.valueOf(e.getStateChange() == ItemEvent.SELECTED)));
                            break;
                        case PATH:
                            gbc.gridx = 0;
                            panel.add(new JLabel(matcher.group(1)), gbc);
                            gbc.gridx = 1;
                            comp = new JButton();
                            JComponent finalComp1 = comp;
                            ((JButton) comp).setAction(new AbstractAction() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    JFileChooser fileChooser = new JFileChooser();
                                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                    if (fileChooser.showDialog(PropsWindow.this, null) == JFileChooser.APPROVE_OPTION) {
                                        ((JButton) finalComp1).setText(fileChooser.getSelectedFile().getPath());
                                        ini.getSections().get(section).replace(k, fileChooser.getSelectedFile().getPath());
                                    }
                                }
                            });
                            ((JButton) comp).setText(ini.getSections().get(section).getProperty(sk));
                            break;
                    }
                panel.add(comp, gbc);
                gbc.gridy++;
            }
            sections.add(new JButton(new AbstractAction(section) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pane.setViewportView(panel);
                }
            }));
        }

        Collections.reverse(sections);

        sectionsJL = new JList<>(sections.toArray(new JButton[0]));
        sectionsJL.setCellRenderer(new ButtonListCellRenderer<>());

        sectionsJL.addListSelectionListener(new ButtonSelectionListener(sectionsJL));
        sectionsJL.setSelectedIndex(0);
        sectionsJL.getSelectedValue().getAction().actionPerformed(null);

        contentPane.add(sectionsJL, BorderLayout.WEST);
        /*JPanel np = new JPanel();
        np.setLayout(new BorderLayout());
        np.add(contents, BorderLayout.NORTH);
        contentPane.add(np, BorderLayout.CENTER);*/

        contentPane.add(pane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton button = new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ini.store(new FileOutputStream(iniFile));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
                apply.run();
            }
        });
        getRootPane().setDefaultButton(button);
        buttons.add(button);
        button = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttons.add(button);

        contentPane.add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }
}
