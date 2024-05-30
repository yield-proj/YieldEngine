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

package com.xebisco.yieldengine.editor.app.run;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PlayPanel extends JPanel {
    private boolean wasInScenePanel;

    private JProgressBar progressBar;

    private Console console = new Console();

    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    //TODO entity viewer

    public class Console extends JTextArea {
        public Console() {
            setEditable(false);
            setLineWrap(true);
        }

        public void println(String s) {
            setText(getText() + s + '\n');
        }

        public void clear() {
            setText("Yield Editor Console: " + new Date() + '\n');
        }
    }

    public PlayPanel() {
        super(new BorderLayout());
        console.clear();
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);

        JTabbedPane leftPane = new JTabbedPane();

        leftPane.setTabPlacement(JTabbedPane.LEFT);

        JScrollPane consoleScrollPane = new JScrollPane(console);
        consoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        leftPane.add("Console", consoleScrollPane);

        splitPane.setLeftComponent(leftPane);

        add(splitPane);
    }

    public boolean isWasInScenePanel() {
        return wasInScenePanel;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public Console getConsole() {
        return console;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public void start() {
        add(progressBar, BorderLayout.NORTH);
        updateUI();
    }

    public void progress(String a) {
        progressBar.setString(a);
    }

    public void done() {
        remove(progressBar);
        repaint();
    }

    public boolean wasInScenePanel() {
        return wasInScenePanel;
    }

    public PlayPanel setWasInScenePanel(boolean wasInScenePanel) {
        this.wasInScenePanel = wasInScenePanel;
        return this;
    }

    public JProgressBar progressBar() {
        return progressBar;
    }

    public PlayPanel setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    public Console console() {
        return console;
    }

    public PlayPanel setConsole(Console console) {
        this.console = console;
        return this;
    }

    public JSplitPane splitPane() {
        return splitPane;
    }

    public PlayPanel setSplitPane(JSplitPane splitPane) {
        this.splitPane = splitPane;
        return this;
    }


}
