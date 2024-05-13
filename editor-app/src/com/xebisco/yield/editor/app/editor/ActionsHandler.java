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

package com.xebisco.yield.editor.app.editor;

import javax.swing.*;
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

    private JMenu actionsMenu = new JMenu("Actions");

    public ActionsHandler() {
        JMenuBar menuBar = new JMenuBar();

        JMenu editMenu = new JMenu("Edit");

        editMenu.add(undoRedoActions[0] = new JMenuItem(new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
                if(!undoAvailable()) undoRedoActions[0].setEnabled(false);
            }
        }));

        editMenu.add(undoRedoActions[1] = new JMenuItem(new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
                if(!redoAvailable()) undoRedoActions[1].setEnabled(false);
            }
        }));

        editMenu.add(actionsMenu);

        undoRedoActions[0].setEnabled(false);
        undoRedoActions[0].getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "asda");
        undoRedoActions[1].setEnabled(false);



        menuBar.add(editMenu);

        add(menuBar);

        updateActionsMenu();
    }

    public void push(ActionHA a) {
        actions.add(a);
        actualAction++;
        for(int i = actualAction + 1; i < actions.size();) {
            actions.remove(actualAction + 1);
        }
        undoRedoActions[0].setEnabled(true);
        updateActionsMenu();
    }

    public boolean redoAvailable() {
        return actualAction < actions.size() - 1;
    }

    public void redo() {
        if(!redoAvailable()) return;
        actions.get(actualAction++).action.run();
        updateActionsMenu();
    }

    public boolean undoAvailable() {
        return actualAction >= 0;
    }

    public void undo() {
        if(!undoAvailable()) return;
        actions.get(actualAction--).reverse.run();
        undoRedoActions[1].setEnabled(true);
        updateActionsMenu();
    }

    private void updateActionsMenu() {
        actionsMenu.removeAll();
        actionsMenu.setEnabled(!actions.isEmpty());
        for(ActionHA a : actions) {
            actionsMenu.add(a.name);
        }
    }
}
