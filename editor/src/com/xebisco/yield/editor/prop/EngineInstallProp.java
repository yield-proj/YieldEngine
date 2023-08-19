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

import com.xebisco.yield.editor.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class EngineInstallProp extends Prop {
    private Image image;
    private static boolean installingEngine;

    private final JPanel imagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    };

    private final EngineInstall install;

    public EngineInstallProp(EngineInstall install) {
        super(install.install(), null);
        this.install = install;
        imagePanel.setBorder(BorderFactory.createTitledBorder(new YieldBorder(), install.install()));
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
        JLabel name = new JLabel(install.name());
        name.setFont(name.getFont().deriveFont(Font.BOLD).deriveFont(name.getFont().getSize2D() + 3));
        mainP.add(name, BorderLayout.NORTH);
        mainP.add(new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setVerticalAlignment(SwingConstants.TOP);
                StringBuilder t = new StringBuilder("<html>");
                int w = 0;
                for (char c : install.description().toCharArray()) {
                    w += g.getFontMetrics().charWidth(c);
                    if (w >= getParent().getWidth()) {
                        w = 0;
                        t.append("<br>");
                    }
                    t.append(c);
                }

                t.append("</html>");

                setText(t.toString());
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        if (!Assets.engineInstalls.contains(install)) {
            JButton custom = new JButton(), def = new JButton();
            custom.setAction(new AbstractAction("Custom Install") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(installingEngine) {
                        JOptionPane.showMessageDialog(null, "There is another install operation in this editor, please wait for it to finishes.");
                        return;
                    }
                    String[][] optionsDefault = options();
                    Map<String, Prop[]> props = new HashMap<>();
                    OptionsProp[] pA = new OptionsProp[optionsDefault.length];
                    for (int i = 0; i < pA.length; i++) {
                        pA[i] = new OptionsProp(i == 0 ? "core_module" : "implementation", optionsDefault[i]);
                    }
                    props.put("install_options", pA);
                    new PropsWindow(props, () -> {
                        Entry.splashDialog("Downloading and installing engine");
                        installingEngine = true;
                        CompletableFuture.runAsync(() -> {
                            File dir = new File(Utils.EDITOR_DIR, "installs/" + install.install());
                            custom.setEnabled(false);
                            def.setEnabled(false);
                            dir.mkdirs();
                            if (dir.exists()) {
                                deleteAllFiles(dir, false);
                            }
                            for (Prop o : props.get("install_options")) {
                                write((String) o.getValue(), dir);
                            }
                            Assets.engineInstalls.add(install);
                            Entry.splashDialog.dispose();
                            installingEngine = false;
                        });
                    }, null, "custom_install");
                }
            });
            buttonPanel.add(custom);

            def.setAction(new AbstractAction(Assets.language.getProperty("default_install")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(installingEngine) {
                        JOptionPane.showMessageDialog(null, "There is another install operation in this editor, please wait for it to finishes.");
                        return;
                    }
                    String[][] options = options();
                    Entry.splashDialog("Downloading and installing engine");
                    installingEngine = true;
                    CompletableFuture.runAsync(() -> {
                        File dir = new File(Utils.EDITOR_DIR, "installs/" + install.install());
                        custom.setEnabled(false);
                        def.setEnabled(false);
                        dir.mkdirs();
                        if (dir.exists()) {
                            deleteAllFiles(dir, false);
                        }
                        for (String[] o : options) {
                            write(o[0], dir);
                        }
                        Assets.engineInstalls.add(install);
                        Entry.splashDialog.dispose();
                        installingEngine = false;
                    });
                }
            });

            buttonPanel.add(def);
        } else {
            name.setText(name.getText() + " (Installed)");
            JButton remove = new JButton();
            remove.setAction(new AbstractAction(Assets.language.getProperty("remove_install")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this install?", "Confirm install remove", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        remove.setEnabled(false);
                        deleteAllFiles(new File(Utils.EDITOR_DIR + "/installs/" + install.install()), true);
                        Assets.engineInstalls.remove(install);
                    }
                }
            });
            buttonPanel.add(remove);
        }
        mainP.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(mainP, BorderLayout.CENTER);
        return panel;
    }

    private static void deleteAllFiles(File dir, boolean deleteItself) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            } else {
                deleteAllFiles(file, true);
            }
        }
        if (deleteItself)
            dir.delete();
    }

    private void write(String f, File dir) {
        ReadableByteChannel rbc;
        try {
            rbc = Channels.newChannel(new URL("https://raw.githubusercontent.com/yield-proj/yield-engine-downloads/master/" + install.install() + "/" + f).openStream());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        File file = new File(dir, f);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String[][] options() {
        java.util.List<String[]> files = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/yield-proj/yield-engine-downloads/master/" + install.install() + "/files.txt").openStream()))) {
            String l;
            while ((l = reader.readLine()) != null) {
                files.add(l.split("/"));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return files.toArray(new String[0][0]);
    }
}
