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

package com.xebisco.yield.editor.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Objects;

public class Splash extends JFrame implements AutoCloseable {
    private final Image splashImage;

    public Splash() throws IOException {
        setResizable(false);
        setUndecorated(true);
        setBackground(Color.BLACK);
        splashImage = ImageIO.read(Objects.requireNonNull(Splash.class.getResource("/logo/splash.png"))).getScaledInstance(500, -1, Image.SCALE_SMOOTH);
        SplashPanel splashPanel;
        add(splashPanel = new SplashPanel());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        splashPanel.paintImmediately(0, 0, 500, 300);
        requestFocus();
    }

    class SplashPanel extends JPanel {

        public SplashPanel() {
            setPreferredSize(new Dimension(500, 300));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(splashImage, 0, getHeight() / 2 - splashImage.getHeight(null) / 2, null);
            g.setColor(Color.WHITE);
            String splashText = "Yield Editor " + Global.VERSION;
            g.setFont(g.getFont().deriveFont(32f));
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(splashText, g);
            g.drawString(splashText, (int) (getWidth() / 2 - bounds.getWidth() / 2), (int) (getHeight() / 2 + bounds.getHeight() / 4));
        }
    }

    @Override
    public void close() {
        dispose();
    }
}
