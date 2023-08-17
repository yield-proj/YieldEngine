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

import javax.swing.*;
import java.awt.*;

public class PrettyLabel extends JPanel {
    private String text;

    public PrettyLabel() {
        setFont(getFont().deriveFont(Font.BOLD));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRoundRect(0, 2, getWidth() - 5, getHeight() - 4, 10, 10);
        g.fillRect(0, 2, 10, getHeight() - 4);
        g.setColor(getForeground());
        int sw = g.getFontMetrics().stringWidth(text);
        g.drawString(text, getWidth() / 2 - sw / 2, getHeight() / 2 + g.getFont().getSize() / 3);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
