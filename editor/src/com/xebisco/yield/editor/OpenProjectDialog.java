package com.xebisco.yield.editor;

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
