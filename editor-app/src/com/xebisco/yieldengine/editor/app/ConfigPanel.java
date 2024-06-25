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

package com.xebisco.yieldengine.editor.app;

import com.xebisco.yieldengine.editor.app.editor.ComponentProp;
import com.xebisco.yieldengine.editor.app.editor.ConfigProp;
import com.xebisco.yieldengine.editor.app.editor.Editor;
import com.xebisco.yieldengine.uiutils.FilteredListModel;
import com.xebisco.yieldengine.uiutils.PrettyListCellRenderer;
import com.xebisco.yieldengine.uiutils.props.Prop;
import com.xebisco.yieldengine.uiutils.props.PropPanel;
import com.xebisco.yieldengine.utils.Pair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigPanel extends JPanel implements Serializable {
    private final HashMap<String, PropPanel> panelMap = new HashMap<>();
    private final String[] pages;
    private final ConfigProp[][] configs;

    public ConfigPanel(String[] pages, ConfigProp[][] configs) {
        super(new BorderLayout());
        this.pages = pages;
        this.configs = configs;
        for (int i = 0; i < pages.length; i++) {
            panelMap.put(pages[i], new PropPanel(configs[i].clone()));
        }

        JScrollPane panel = new JScrollPane();
        panel.getViewport().setBackground(getBackground());
        panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(panel);

        JToolBar top = new JToolBar();
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Search: "));
        JTextField searchField;
        top.add(searchField = new JTextField());

        JPanel left = new JPanel(new BorderLayout());

        searchField.setMaximumSize(new Dimension(100, 30));

        FilteredListModel listModel = new FilteredListModel(pages).setFilter(element -> element.toUpperCase().contains(searchField.getText().toUpperCase()));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                listModel.doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listModel.doFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listModel.doFilter();
            }
        });
        left.add(top, BorderLayout.NORTH);

        JList<String> pagesList = new JList<>(listModel);
        pagesList.setPreferredSize(new Dimension(150, 150));
        pagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pagesList.setCellRenderer(new PrettyListCellRenderer<>());

        pagesList.addListSelectionListener(e -> {
            panel.setViewportView(panelMap.get(pagesList.getSelectedValue()));
            panel.updateUI();
        });

        pagesList.setSelectedIndex(0);

        left.add(pagesList);
        add(left, BorderLayout.WEST);

        pagesList.requestFocus();
    }

    //Actual return type: HashMap<String, HashMap<String, ArrayList<Pair<Pair<String, String>, String[]>>>>
    public HashMap<String, HashMap<String, Serializable>> values() {
        HashMap<String, HashMap<String, Serializable>> values = new HashMap<>();
        for (String s : panelMap.keySet()) {
            values.put(s, PropPanel.values(panelMap.get(s).props()));
        }
        return values;
    }

    public void saveValues() {
        for (String s : panelMap.keySet()) {
            for (Prop p : panelMap.get(s).props()) {
                ((ConfigProp) p).saveValues();
            }
        }
    }

    public void insert(HashMap<String, HashMap<String, Serializable>> values, Editor editor) {
        for (String s : values.keySet()) {
            for (Prop p : panelMap.get(s).props()) {
                //((ConfigProp) p).addComp(values.addComponent(s));
                //noinspection unchecked
                ((ConfigProp) p).addComp(PropPanel.values(ComponentProp.getProps((ArrayList<Pair<Pair<String, String>, String[]>>) values.get(s).get(((ConfigProp) p).configClass().getSimpleName()), editor).toArray(new Prop[0])));
                //((ConfigProp) p).addComp((HashMap<String, Serializable>) values.addComponent(s).addComponent(((ConfigProp) p).configClass().getSimpleName()));
            }
            //PropPanel.insert(panelMap.addComponent(s).props(), values.addComponent(s));
        }
    }

    public void refresh() {
        for (String s : panelMap.keySet()) {
            for (Prop p : panelMap.get(s).props()) {
                ((ConfigProp) p).addComp();
            }
        }
    }

    public HashMap<String, PropPanel> panelMap() {
        return panelMap;
    }

    public String[] pages() {
        return pages;
    }

    public ConfigProp[][] configs() {
        return configs;
    }
}
