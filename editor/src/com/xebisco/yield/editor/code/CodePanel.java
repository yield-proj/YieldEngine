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
import com.xebisco.yield.editor.prop.ComponentProp;
import com.xebisco.yield.editor.prop.Props;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultHighlighter;
import javax.tools.ToolProvider;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;

public class CodePanel extends JPanel {

    private final RSyntaxTextArea textArea;
    private final File file;

    private final EngineInstall engineInstall;
    private boolean saved;

    private final String savedTitle, unsavedTitle;
    private final YieldInternalFrame frame;

    public CodePanel(File file, EngineInstall engineInstall, YieldInternalFrame frame, IRecompile recompile) {
        this.file = file;
        this.engineInstall = engineInstall;
        this.frame = frame;
        savedTitle = file.getName() + " (Script)";
        unsavedTitle = file.getName() + " (Script) UNSAVED";
        frame.setTitle(savedTitle);

        setLayout(new BorderLayout());

        JavaLanguageSupport jls = (JavaLanguageSupport) LanguageSupportFactory.get().getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);

        try {
            jls.getJarManager().addClassFileSource(new File(Utils.EDITOR_DIR, "lang-rt.jar"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File[] engine = new File(Utils.EDITOR_DIR + "/installs/" + engineInstall.install()).listFiles();
        assert engine != null;
        for (File jar : engine) {
            try {
                jls.getJarManager().addClassFileSource(jar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        textArea = createTextArea();
        jls.install(textArea);
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = reader.readLine()) != null) {
                stringBuilder.append(l).append("\n");
            }
        } catch (IOException e) {
            Utils.error(null, e);
        }

        textArea.setText(stringBuilder.substring(0, stringBuilder.length() - 1));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                frame.setTitle(unsavedTitle);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                frame.setTitle(unsavedTitle);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);
        scrollPane.setIconRowHeaderEnabled(true);
        scrollPane.getGutter().setBookmarkingEnabled(true);

        add(scrollPane);
    }

    class ToggleLayeredHighlightsAction extends AbstractAction {

        ToggleLayeredHighlightsAction() {
            putValue(NAME, "Layered Selection Highlights");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultHighlighter h = (DefaultHighlighter) textArea.getHighlighter();
            h.setDrawsLayeredHighlights(!h.getDrawsLayeredHighlights());
        }

    }

    public static YieldInternalFrame newCodeFrame(JDesktopPane desktopPane, EngineInstall engineInstall, File file, YieldInternalFrame parent, IRecompile recompile) {
        YieldInternalFrame frame = new YieldInternalFrame(parent);
        CodePanel codePanel = new CodePanel(file, engineInstall, frame, recompile);
        frame.setFrameIcon(Assets.images.get("scriptIcon.png"));
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

                try (FileWriter writer = new FileWriter(file)) {
                    writer.append(textArea.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                saved = true;
                frame.setTitle(savedTitle);
                final StringBuilder builder = new StringBuilder();
                OutputStream error = new OutputStream() {
                    @Override
                    public void write(int b) {
                        builder.append((char) b);
                    }
                };
                File core = new File(Utils.EDITOR_DIR.getPath() + "/installs/" + engineInstall.install() + "/yield-core.jar");
                ToolProvider.getSystemJavaCompiler().run(null, null, error, "-cp", core.getPath(), "-d", ComponentProp.DEST.getPath(), file.getPath());
                try {
                    error.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (!builder.isEmpty())
                    Utils.errorNoStackTrace(CodePanel.this, new CompilationException(builder.toString()));
            }
        }));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menuBar.add(menu);
        menu = new JMenu("View");
        menu.add(new JCheckBoxMenuItem(new ToggleLayeredHighlightsAction()));
        JMenu menu2 = new JMenu("Font");
        JMenu fontSizes = new JMenu("Font size");
        for (int i = 0; i < 15; i++) {
            int finalI = (i * 2 + 10);
            if(Props.get(Assets.editorSettings.get("code_editor"), "font_size").getValue().equals(finalI + "px"))
                textArea.setFont(textArea.getFont().deriveFont((float) finalI));
            fontSizes.add(new AbstractAction(finalI + "px") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Props.get(Assets.editorSettings.get("code_editor"), "font_size").setValue(finalI + "px");
                    Projects.saveSettings();
                    textArea.setFont(textArea.getFont().deriveFont((float) finalI));
                }
            });
        }
        menu2.add(fontSizes);
        menu.add(menu2);
        menuBar.add(menu);
        return menuBar;
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
        textArea.setCaretPosition(0);
        textArea.setAntiAliasingEnabled(true);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.requestFocusInWindow();
        textArea.setMarkOccurrences(true);
        textArea.setCodeFoldingEnabled(true);
        return textArea;
    }


    /**
     * Focuses the text area.
     */
    void focusTextArea() {
        textArea.requestFocusInWindow();
    }


}