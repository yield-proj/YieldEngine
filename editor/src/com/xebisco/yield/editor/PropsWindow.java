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

import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;
import com.xebisco.yield.editor.prop.Props;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class PropsWindow extends JDialog {

    private static final Pattern keyPattern = Pattern.compile("^([^(]+)\\(([^)]*)\\)$");

    public static JPanel propsPanel(Prop[] props) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc.gridy = 0;
        for (Prop prop : props) {
            panel.add(prop.panel(), gbc);
            gbc.gridy++;
        }
        return panel;
    }

    public static JPanel compPropsPanel(ComponentProp[] props, YieldInternalFrame frame, IRecompile recompile) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc.gridy = 0;
        for (ComponentProp prop : props) {
            panel.add(prop.panel(frame, recompile), gbc);
            gbc.gridy++;
        }
        return panel;
    }

    public PropsWindow(Map<String, Prop[]> s, Runnable apply, Frame owner, String title) {
        super(owner);
        setTitle("Properties");
        setModal(true);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(650, 400);
        setMaximumSize(new Dimension(1000, 400));
        setMinimumSize(new Dimension(500, 100));
        setLocationRelativeTo(owner);

        contentPane.setLayout(new BorderLayout());

        List<JButton> sections = new ArrayList<>();

        JScrollPane pane = new JScrollPane();
        pane.setBorder(null);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        for (String section : s.keySet()) {
            JPanel panel = propsPanel(s.get(section));
            sections.add(new JButton(new AbstractAction(section) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel p = new JPanel();
                    p.setLayout(new BorderLayout());
                    p.add(panel, BorderLayout.NORTH);
                    pane.setViewportView(p);
                }
            }));
        }

        String one = null;
        int a = 0;

        for (String ss : s.keySet()) {
            one = ss;
            for (Prop ignored : s.get(ss))
                a++;
            if (a > 1) {
                one = null;
                break;
            }
            break;
        }

        if (one != null) {
            setTitle(one);
            setUndecorated(true);
            JLabel label = new JLabel(one);
            label.setFont(getFont().deriveFont(Font.BOLD));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            contentPane.add(label, BorderLayout.NORTH);
            pane.setBorder(new YieldBorder());
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                }
            });
            setMinimumSize(new Dimension(250, 100));
            setSize(new Dimension(250, 100));
            setLocationRelativeTo(owner);
            contentPane.add(pane, BorderLayout.CENTER);
        }

        Collections.reverse(sections);

        if (one == null) {
            JList<JButton> sectionsJL = new JList<>(sections.toArray(new JButton[0]));
            sectionsJL.setCellRenderer(new ButtonListCellRenderer<>());

            sectionsJL.addListSelectionListener(new ButtonSelectionListener(sectionsJL));
            sectionsJL.setSelectedIndex(0);
            sectionsJL.getSelectedValue().getAction().actionPerformed(null);

            contentPane.add(sectionsJL, BorderLayout.WEST);
            JMenuBar menuBar = new JMenuBar();
            menuBar.add(new JMenu(""));
            setJMenuBar(menuBar);
        } else {
            sections.get(0).getAction().actionPerformed(null);
        }
        contentPane.add(pane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton button = new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                apply.run();
            }
        });
        getRootPane().setDefaultButton(button);
        buttons.add(button);

        buttons.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));

        contentPane.add(buttons, BorderLayout.SOUTH);

        if (title != null)
            setTitle(title);

        setVisible(true);
    }
}
