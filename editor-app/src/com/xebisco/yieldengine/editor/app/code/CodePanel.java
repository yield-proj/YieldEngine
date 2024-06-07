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

package com.xebisco.yieldengine.editor.app.code;

import com.xebisco.yieldengine.editor.app.Entry;
import com.xebisco.yieldengine.editor.app.FileBrowser;
import com.xebisco.yieldengine.editor.app.Global;
import com.xebisco.yieldengine.editor.app.editor.ActionsHandler;
import com.xebisco.yieldengine.editor.app.editor.Editor;
import com.xebisco.yieldengine.uiutils.Srd;
import com.xebisco.yieldengine.utils.Pair;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CodePanel extends JPanel {

    private JTabbedPane tabbedPane = new JTabbedPane();

    private final Editor editor;
    private final ActionsHandler actionsHandler;
    private final List<Pair<File, JPanel>> openedFiles = new ArrayList<>();
    private final FileBrowser scriptBrowser;

    public CodePanel(Editor editor) {
        super(new BorderLayout());
        this.editor = editor;
        actionsHandler = new ActionsHandler(new Component[]{});
        scriptBrowser = new FileBrowser(pathname -> pathname.getName().endsWith(".java") || pathname.getName().endsWith(".txt"), editor.project().scriptsPackage(), this::addJavaCodeFile, actionsHandler);
        JPanel left = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(scriptBrowser);
        scrollPane.setBorder(null);
        left.add(scrollPane);
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton(new AbstractAction("Reload") {
            @Override
            public void actionPerformed(ActionEvent e) {
                scriptBrowser.update();
            }
        }));
        left.add(toolBar, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, tabbedPane);
        add(splitPane);
        add(actionsHandler, BorderLayout.NORTH);
        SwingUtilities.invokeLater(() -> {
            splitPane.setDividerLocation(.2);
        });
    }

    private void reloadFile(RSyntaxTextArea textArea, File file) throws IOException {
        textArea.setText(Srd.readFile(file));
    }

    public void addJavaCodeFile(File file) {
        for (Pair<File, JPanel> pair : openedFiles) {
            if (pair.first().equals(file)) {
                tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(pair.second()));
                return;
            }
        }

        JPanel panel = new JPanel(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea();
        try {
            reloadFile(textArea, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        try {
            Theme.load(CodePanel.class.getResourceAsStream("/octagontext.xml")).apply(textArea);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        JavaLanguageSupport support = (JavaLanguageSupport) lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);
        try {
            support.getJarManager().addClassFileSource(new File(Entry.saveDir, "java_base.jar"));
            for (File lib : Global.listf(editor.project().librariesDirectory())) {
                support.getJarManager().addClassFileSource(lib);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        support.setShowDescWindow(true);
        support.setParameterAssistanceEnabled(true);
        support.setAutoCompleteEnabled(true);
        support.setAutoActivationEnabled(true);
        support.setAutoActivationDelay(800);
        textArea.getSyntaxScheme().setStyle(TokenTypes.RESERVED_WORD_2, textArea.getSyntaxScheme().getStyle(TokenTypes.RESERVED_WORD));

        support.install(textArea);

        RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);
        scrollPane.setIconRowHeaderEnabled(true);
        scrollPane.getGutter().setBookmarkingEnabled(true);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFile(file, textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFile(file, textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFile(file, textArea.getText());
            }
        });

        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(!file.exists()) {
                    tabbedPane.remove(panel);
                    openedFiles.remove(new Pair<>(file, panel));
                }
                int caretPos = textArea.getCaretPosition();
                try {
                    textArea.setText(Srd.readFile(file));
                } catch (IOException ex) {
                    tabbedPane.remove(panel);
                }
                textArea.setCaretPosition(caretPos);
            }
        });

        tabbedPane.add(panel);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), getTitlePanel(tabbedPane, panel, file));
        tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(panel));
        openedFiles.add(new Pair<>(file, panel));
        SwingUtilities.invokeLater(scriptBrowser::update);
    }

    private void updateFile(File file, String text) {
        CompletableFuture.runAsync(() -> {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private JPanel getTitlePanel(final JTabbedPane tabbedPane, final JPanel panel, File file) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel titleLbl = new JLabel(file.getName());
        titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        titlePanel.add(titleLbl);
        JButton closeButton = new JButton("x");
        closeButton.setOpaque(false);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabbedPane.remove(panel);
                openedFiles.remove(new Pair<>(file, panel));
            }
        });
        titlePanel.add(closeButton);

        return titlePanel;
    }
}
