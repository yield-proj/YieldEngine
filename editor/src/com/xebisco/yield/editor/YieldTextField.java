package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public class YieldTextField extends JTextField {
    private boolean isNull;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isNull) {
            g.setColor(getForeground());
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawString("null", getWidth() / 2 - g.getFontMetrics().stringWidth("null") / 2, (int) (getHeight() / 2 + g.getFont().getSize() / 2.5));
        }
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }
}
