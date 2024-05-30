/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine.editor.app.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ActionsHandler extends JPanel {

    public record ActionHA(String name, Runnable action, Runnable reverse) {
    }

    private List<ActionHA> actions = new ArrayList<>();
    private int actualAction = -1;
    private final JMenuItem[] undoRedoActions = new JMenuItem[2];
    private JPopupMenu actionsMenu = new JPopupMenu("Actions");
    private JButton actionsButton = new JButton();

    public ActionsHandler(Component[] additionalMenus) {
        setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();

        JMenu editMenu = new JMenu("Edit");

        editMenu.add(undoRedoActions[0] = new JMenuItem(new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        }));

        editMenu.add(undoRedoActions[1] = new JMenuItem(new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        }));

        //editMenu.add(actionsMenu);

        undoRedoActions[0].setEnabled(false);
        undoRedoActions[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoRedoActions[1].setEnabled(false);
        undoRedoActions[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));


        menuBar.add(editMenu);

        for(Component menu : additionalMenus) menuBar.add(menu);

        add(menuBar, BorderLayout.WEST);

        actionsButton.setAction(new AbstractAction("Actions") {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionsMenu.show(actionsButton, -actionsMenu.getWidth() + actionsButton.getWidth(), actionsButton.getHeight());
            }
        });

        add(actionsButton, BorderLayout.EAST);


        updateActionsMenu();
    }

    public void push(ActionHA a) {
        actionsButton.setText(a.name);
        actualAction++;
        while (actions.size() > actualAction) {
            actions.remove(actions.size() - 1);
        }
        actions.add(a);
        a.action().run();
        undoRedoActions[0].setEnabled(true);
        updateActionsMenu();
    }

    public boolean redoAvailable() {
        return actualAction < actions.size() - 1;
    }

    public void redo() {
        if (redoAvailable()) {
            actions.get(++actualAction).action.run();
        }
        updateActionsMenu();
    }

    public boolean undoAvailable() {
        return actualAction >= 0;
    }

    public void undo() {
        if (undoAvailable()) {
            actions.get(actualAction--).reverse.run();
        }
        updateActionsMenu();
    }

    private void updateActionsMenu() {
        undoRedoActions[0].setEnabled(undoAvailable());
        undoRedoActions[1].setEnabled(redoAvailable());
        actionsMenu.removeAll();
        actionsMenu.setEnabled(!actions.isEmpty());
        for (int i = actions.size() - 1; i >= 0; i--) {
            actionsMenu.add(actionItem(i));
        }
    }

    private JMenuItem actionItem(int finalI) {
        JMenuItem a = new JMenuItem(new AbstractAction(actions.get(finalI).name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                int diff = finalI - actualAction;
                if(diff > 0) {
                    for(int i = 0; i < Math.abs(diff); i++) {
                        redo();
                    }
                } else {
                    for(int i = 0; i < Math.abs(diff); i++) {
                        undo();
                    }
                }
            }
        });
        if (finalI != actualAction)
            a.setForeground(a.getForeground().darker());
        return a;
    }
}
