package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public class BigListCellRenderer extends JLabel implements ListCellRenderer<String> {
  public BigListCellRenderer() {
    setOpaque(true);
  }

  public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
    setText(value);
    setHorizontalAlignment(CENTER);

    Color background;

    if (isSelected) {
      background = UIManager.getColor("Tree.selectionBackground");
    } else {
      background = UIManager.getColor("Tree.background");
    }

    setBackground(background);

    return this;
  }
}