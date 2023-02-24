package com.xebisco.yield.editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Splash extends JFrame {

    private final Image splashBkg;

    public Splash() throws IOException {
        setUndecorated(true);
        setAlwaysOnTop(true);
        pack();
        setSize(500, 330);
        //noinspection ConstantConditions
        splashBkg = ImageIO.read(Splash.class.getResourceAsStream("/splashBkg.png")).getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(splashBkg, 0, 0, this);
        g.dispose();
        if(Icons.YIELD_ICON == null)
            Icons.loadAll();
    }

    @Override
    public void dispose() {
        splashBkg.flush();
        super.dispose();
    }
}
