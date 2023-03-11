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

package com.xebisco.yield.editor.old;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OpenProjectDialog extends JDialog {

    enum Response {THIS, NEW, CANCEL}
    private Response response = Response.CANCEL;

    public OpenProjectDialog(Frame owner) {
        super(owner, true);
        setSize(400, 100);
        setLocationRelativeTo(owner);
        setTitle("Open Project");
        setResizable(false);
        setIconImage(Icons.YIELD_ICON.getImage());
        add(new JLabel("Open this project in:"), BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                response = Response.CANCEL;
                dispose();
            }
        }));
        buttons.add(new JButton(new AbstractAction("New Window") {
            @Override
            public void actionPerformed(ActionEvent e) {
                response = Response.NEW;
                dispose();
            }
        }));
        JButton button = new JButton(new AbstractAction("This Window") {
            @Override
            public void actionPerformed(ActionEvent e) {
                response = Response.THIS;
                dispose();
            }
        });
        buttons.add(button);
        getRootPane().setDefaultButton(button);
        add(buttons, BorderLayout.SOUTH);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
