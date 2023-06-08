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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Editor extends JFrame {
    public Editor(Project project) {
        setTitle("Yield 5 Editor | " + project.getName());
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int opt = JOptionPane.showConfirmDialog(Editor.this, "Be sure to check if you saved all your work!", "Confirm Close", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION)
                    dispose();
            }
        });
        setJMenuBar(menuBar());
        setIconImage(Assets.images.get("yieldIcon.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar menuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(new AbstractAction("Back to Projects") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
                if (!isVisible())
                    Entry.openProjects();
            }
        });
        menu.add(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(Editor.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        JMenuItem item = new JMenuItem(new AbstractAction("Repaint") {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, KeyEvent.ALT_DOWN_MASK));
        menuBar.add(item);
        item = new JMenuItem(new AbstractAction("Run") {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.ALT_DOWN_MASK));
        menuBar.add(item);


        return menuBar;
    }
}
