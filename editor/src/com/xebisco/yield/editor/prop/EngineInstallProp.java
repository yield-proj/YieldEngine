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

package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.EngineInstall;
import com.xebisco.yield.editor.Utils;
import com.xebisco.yield.editor.YieldBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class EngineInstallProp extends Prop {
    private Image image;

    private final JPanel imagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    };

    private final EngineInstall install;

    public EngineInstallProp(EngineInstall install) {
        super(install.getInstall(), null);
        this.install = install;
        imagePanel.setBorder(BorderFactory.createTitledBorder(new YieldBorder(), install.getInstall()));
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageProp.class.getResourceAsStream("/yieldIcon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagePanel.setPreferredSize(new Dimension(100, (int) (100 * ((double) image.getHeight(null) / image.getWidth(null)))));
        imagePanel.setSize(imagePanel.getPreferredSize());
        Component c = imagePanel;
        while (c != null && !(c instanceof Window))
            c = c.getParent();
        if (c != null)
            c.revalidate();
    }

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(imagePanel, BorderLayout.WEST);
        JPanel mainP = new JPanel();
        mainP.setLayout(new BorderLayout());
        JLabel name = new JLabel(install.getName());
        name.setFont(name.getFont().deriveFont(Font.BOLD).deriveFont(name.getFont().getSize2D() + 3));
        mainP.add(name, BorderLayout.NORTH);
        mainP.add(new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setVerticalAlignment(SwingConstants.TOP);
                StringBuilder t = new StringBuilder("<html>");
                int w = 0;
                for(char c : install.getDescription().toCharArray()) {
                    w += g.getFontMetrics().charWidth(c);
                    if(w >= getParent().getWidth()) {
                        w = 0;
                        t.append("<br>");
                    }
                    t.append(c);
                }

                t.append("</html>");

                setText(t.toString());
            }
        });
        panel.add(mainP, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons.add(new JButton(new AbstractAction("Get") {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL core;
                try {
                    core = new URL("");
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
                try (InputStream in = core.openStream()) {
                    File dir = new File(Utils.defaultDirectory() + "/.yield_editor", install.getInstall());
                    dir.mkdir();
                    File file = new File(dir, "core.jar");
                    file.createNewFile();
                    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Utils.error(null, ex);
                }
            }
        }));
        return panel;
    }
}
