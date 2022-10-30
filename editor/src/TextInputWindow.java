/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicReference;

public class TextInputWindow {
    public static String request(Object contextObject) {
        final String[] s = {""};
        JFrame frame = new JFrame();
        Font font = new Font("arial", Font.PLAIN, 40);
        int startWidth = 400;
        frame.setSize(startWidth, 40);
        frame.setLocation(MouseInfo.getPointerInfo().getLocation());
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.requestFocus();
        frame.setTitle("Yield Editor Text Insertion");
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.setFont(font);
                g.drawString(s[0], 2, getHeight() - 5);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                int sw = g.getFontMetrics().stringWidth(s[0]) + 5;
                g.drawLine(sw, 5, sw, getHeight() - 5);
                if (frame.getWidth() < sw) frame.setSize(sw, frame.getHeight());
                else if (sw < startWidth) frame.setSize(startWidth, frame.getHeight());
            }
        };
        frame.add(panel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (Character.isAlphabetic(e.getKeyChar()) || Character.isDigit(e.getKeyChar()) || e.getKeyChar() == ' ')
                    s[0] += e.getKeyChar();
                else switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE:
                        if (s[0].length() > 0)
                            s[0] = s[0].substring(0, s[0].length() - 1);
                        break;
                    case KeyEvent.VK_ENTER:
                        synchronized (contextObject) {
                            contextObject.notify();
                        }
                        frame.setVisible(false);
                        frame.dispose();
                        break;
                }
                panel.repaint();
            }
        });

        synchronized (contextObject) {
            try {
                contextObject.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return s[0];
    }
}
