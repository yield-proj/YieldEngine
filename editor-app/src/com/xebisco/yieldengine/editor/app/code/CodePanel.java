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
import com.xebisco.yieldengine.editor.app.Global;
import com.xebisco.yieldengine.editor.app.editor.Editor;
import com.xebisco.yieldengine.uiutils.Srd;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class CodePanel extends JPanel {

    private JTabbedPane tabbedPane = new JTabbedPane();

    private final Editor editor;

    public CodePanel(Editor editor) {
        super(new BorderLayout());
        this.editor = editor;
        add(tabbedPane);
    }

    private void addJavaCodeText(String title, String code) {
        JPanel panel = new JPanel(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea(code);
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

        tabbedPane.add(panel);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), getTitlePanel(tabbedPane, panel, title));
    }

    public void addJavaCodeFile(File file) {
        try {
            addJavaCodeText(file.getName(), Srd.readFile(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JPanel getTitlePanel(final JTabbedPane tabbedPane, final JPanel panel, String title) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        titlePanel.add(titleLbl);
        JButton closeButton = new JButton("x");
        closeButton.setOpaque(false);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabbedPane.remove(panel);
            }
        });
        titlePanel.add(closeButton);

        return titlePanel;
    }
}
