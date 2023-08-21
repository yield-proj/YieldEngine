package com.xebisco.yield.editor;

import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Prop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropsPanel extends JPanel {

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

    public PropsPanel(Map<String, Prop[]> s, Runnable apply) {
        setLayout(new BorderLayout());

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

        if (s.keySet().size() == 1)
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
            JLabel label = new JLabel(one);
            label.setFont(getFont().deriveFont(Font.BOLD));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);
            pane.setBorder(new YieldBorder());
            setMinimumSize(new Dimension(250, 100));
            setSize(new Dimension(250, 100));
            add(pane, BorderLayout.CENTER);
        }

        Collections.reverse(sections);

        if (one == null) {
            JList<JButton> sectionsJL = new JList<>(sections.toArray(new JButton[0]));
            sectionsJL.setCellRenderer(new ButtonListCellRenderer<>());

            sectionsJL.addListSelectionListener(new ButtonSelectionListener(sectionsJL));
            sectionsJL.setSelectedIndex(0);
            sectionsJL.getSelectedValue().getAction().actionPerformed(null);

            add(sectionsJL, BorderLayout.WEST);
        } else {
            sections.get(0).getAction().actionPerformed(null);
        }
        add(pane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton button = new JButton(new AbstractAction(Assets.language.getProperty("ok")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply.run();
            }
        });
        if(SwingUtilities.getWindowAncestor(this) instanceof RootPaneContainer f) f.getRootPane().setDefaultButton(button);
        buttons.add(button);

        add(buttons, BorderLayout.SOUTH);
    }
}