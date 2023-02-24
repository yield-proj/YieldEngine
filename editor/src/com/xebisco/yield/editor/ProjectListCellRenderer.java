package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public class ProjectListCellRenderer extends JPanel implements ListCellRenderer<Project> {

    public ProjectListCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<? extends Project> list, Project value, int index, boolean isSelected, boolean cellHasFocus) {
        Color background;
        if (isSelected) {
            background = UIManager.getColor("Tree.selectionBackground");
        } else {
            background = UIManager.getColor("Tree.background");
        }

        removeAll();

        JPanel main = new JPanel(new FlowLayout(FlowLayout.LEFT));
        main.setOpaque(false);
        JLabel label = new JLabel(value.getProjectName());
        label.setFont(label.getFont().deriveFont(Font.BOLD).deriveFont(20f));
        main.add(label);
        label = new JLabel(value.getDirectory().getPath());
        main.add(label);
        JPanel star = new JPanel(new BorderLayout());
        star.setOpaque(false);
        star.setPreferredSize(new Dimension(32, 32));
        if (value.isStarred())
            star.add(new JLabel(Icons.STARRED_ICON_16x16), BorderLayout.CENTER);
        else star.add(new JLabel(Icons.NOT_STARRED_ICON_16x16), BorderLayout.CENTER);

        add(star, BorderLayout.EAST);

        add(main, BorderLayout.CENTER);


        setBackground(background);

        return this;
    }
}