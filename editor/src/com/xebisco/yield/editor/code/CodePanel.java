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
import com.xebisco.yield.editor.prop.FontProp;
import com.xebisco.yield.editor.prop.Props;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.DefaultHighlighter;
import javax.tools.ToolProvider;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.concurrent.CompletableFuture;

public class CodePanel extends JPanel {

    private final RSyntaxTextArea textArea;
    private final File file;
    private boolean saved = true;

    private final EngineInstall engineInstall;

    private final String savedTitle, unsavedTitle;
    private final YieldInternalFrame frame;
    private final Font startFont;

    private final IRecompile recompile;

    public CodePanel(File file, EngineInstall engineInstall, YieldInternalFrame frame, IRecompile recompile) {
        this.file = file;
        this.engineInstall = engineInstall;
        this.frame = frame;
        this.recompile = recompile;
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (saved || JOptionPane.showConfirmDialog(frame, "There are unsaved changes in this file, close anyway?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    frame.dispose();
                }
            }
        });
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
        textArea.setClearWhitespaceLinesEnabled(true);
        startFont = textArea.getFont();
        Font f = (Font) Props.get(Assets.editorSettings.get("code_editor"), "code_editor_font").getValue();
        if (f != null)
            textArea.setFont(f);
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
                saved = false;
                frame.setTitle(unsavedTitle);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                saved = false;
                frame.setTitle(unsavedTitle);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);
        scrollPane.setBorder(null);
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

    public static YieldInternalFrame newCodeFrame(EngineInstall engineInstall, File file, YieldInternalFrame frame, IRecompile recompile) {
        CodePanel codePanel = new CodePanel(file, engineInstall, frame, recompile);
        frame.add(codePanel);
        frame.setSize(600, 500);

        frame.setTitle(file.getName() + " - Script Editor");
        frame.setJMenuBar(codePanel.codeMenuBar());

        return frame;
    }

    private JMenuBar codeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(Assets.language.getProperty("file"));
        JMenuItem item;
        menu.add(item = new JMenuItem(new AbstractAction(Assets.language.getProperty("save")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.append(textArea.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.setTitle(savedTitle);
                saved = true;
                CompletableFuture.runAsync(recompile::recompileProject);
            }
        }));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menu.add(new JMenuItem(new AbstractAction(Assets.language.getProperty("close")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (InternalFrameListener l : frame.getInternalFrameListeners()) l.internalFrameClosing(null);
            }
        }));
        menuBar.add(menu);
        menu = new JMenu("View");
        menu.add(new JCheckBoxMenuItem(new ToggleLayeredHighlightsAction()));
        item = new JMenuItem(new AbstractAction(Assets.language.getProperty("increase_font_size")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() + 1f));
                repaint();
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));
        menu.add(item);
        item = new JMenuItem(new AbstractAction(Assets.language.getProperty("decrease_font_size")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.getFont().getSize() > 1) {
                    textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() - 1f));
                    repaint();
                }
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
        menu.add(item);


        menu.add(new JMenuItem(new AbstractAction("Font...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((FontProp) Props.get(Assets.editorSettings.get("code_editor"), "code_editor_font")).updateFont(CodePanel.this, null);
                Projects.saveSettings();
                Font f = (Font) Props.get(Assets.editorSettings.get("code_editor"), "code_editor_font").getValue();
                if (f != null)
                    textArea.setFont(f);
                else textArea.setFont(startFont);

                CodePanel.this.repaint();
            }
        }));
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