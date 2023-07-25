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
package com.xebisco.yield.editor.code;

import com.xebisco.yield.editor.*;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.rsta.ac.java.classreader.attributes.Code;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;

public class CodePanel extends JPanel {

    private RSyntaxTextArea textArea;
    private final File file;


    public CodePanel(File file, EngineInstall engineInstall) {
        this.file = file;

        setLayout(new BorderLayout());

        JavaLanguageSupport jls = (JavaLanguageSupport) LanguageSupportFactory.get().getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);
        jls.setAutoCompleteEnabled(true);

        try {
            jls.getJarManager().addClassFileSource(new File(Utils.EDITOR_DIR, "jre6-rt.jar"));
            File[] engine = new File(Utils.EDITOR_DIR + "/installs/" + engineInstall.install()).listFiles();
            assert engine != null;
            for(File jar : engine)
                jls.getJarManager().addClassFileSource(jar);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


        textArea = createTextArea();
        jls.install(textArea);
        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = reader.readLine()) != null) {
                stringBuilder.append(l).append("\n");
            }
        } catch (IOException e) {
            Utils.error(null, e);
        }

        textArea.setText(stringBuilder.substring(0, stringBuilder.length() - 1));

        RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);

        add(scrollPane);
    }

    public static YieldInternalFrame newCodeFrame(JDesktopPane desktopPane, EngineInstall engineInstall, File file, YieldInternalFrame parent) {
        CodePanel codePanel = new CodePanel(file, engineInstall);
        String title = file.getName().split("\\.java")[0] + " (Script)";
        YieldInternalFrame frame = new YieldInternalFrame(parent);
        frame.setFrameIcon(Assets.images.get("windowIcon.png"));
        frame.add(codePanel);

        frame.setTitle(file.getName() + " - Script Editor");
        frame.setJMenuBar(codePanel.codeMenuBar());
        frame.setClosable(true);
        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setBounds(100, 100, 600, 500);

        desktopPane.add(frame);
        frame.setVisible(true);
        return frame;
    }

    private JMenuBar codeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item;
        menu.add(item = new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try(FileWriter writer = new FileWriter(file)) {
                    writer.append(textArea.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menuBar.add(menu);
        menu = new JMenu("View");
        JMenu menu2 = new JMenu("Font size");
        menu2.add(new AbstractAction("10px") {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(10f));
            }
        });
        menu2.add(new AbstractAction("12px") {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(12f));
            }
        });
        menu2.add(new AbstractAction("14px") {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(14f));
            }
        });
        menu2.add(new AbstractAction("16px") {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(16f));
            }
        });
        menu2.add(new AbstractAction("20px") {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(20f));
            }
        });
        menu.add(menu2);
        menuBar.add(menu);
        return menuBar;
    }


    private void addItem(Action a, ButtonGroup bg, JMenu menu) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(a);
        bg.add(item);
        menu.add(item);
    }


    /**
     * Creates the text area for this application.
     *
     * @return The text area.
     */
    private RSyntaxTextArea createTextArea() {
        RSyntaxTextArea textArea = new RSyntaxTextArea(25, 80);
        try {
            Theme.load(CodePanel.class.getResourceAsStream("/textAreaTheme.xml")).apply(textArea);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LanguageSupportFactory.get().register(textArea);
        textArea.setCaretPosition(0);
        textArea.setAntiAliasingEnabled(true);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.requestFocusInWindow();
        textArea.setMarkOccurrences(true);
        textArea.setCodeFoldingEnabled(true);
        textArea.setTabsEmulated(true);
        textArea.setTabSize(3);
        ToolTipManager.sharedInstance().registerComponent(textArea);
        return textArea;
    }


    /**
     * Focuses the text area.
     */
    void focusTextArea() {
        textArea.requestFocusInWindow();
    }


}