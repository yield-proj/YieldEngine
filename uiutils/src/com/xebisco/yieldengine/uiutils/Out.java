package com.xebisco.yieldengine.uiutils;

import javax.swing.*;

public class Out {
    public static void error(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
