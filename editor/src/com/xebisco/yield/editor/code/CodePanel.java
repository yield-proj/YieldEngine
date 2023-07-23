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

import com.xebisco.yield.editor.Utils;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.rsta.ac.java.classreader.attributes.Code;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CodePanel extends JPanel {

    private RSyntaxTextArea textArea;
    private final File file;


    public CodePanel(File file) {
        this.file = file;

        setLayout(new BorderLayout());

        JavaLanguageSupport jls = (JavaLanguageSupport) LanguageSupportFactory.get().getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);
        jls.setAutoCompleteEnabled(true);

        try {
            jls.getJarManager().addClassFileSource(new File(Utils.EDITOR_DIR, "jre6-rt.jar"));
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

        textArea.setText(stringBuilder.toString().substring(0, stringBuilder.length() - 1));

        RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);

        add(scrollPane);
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