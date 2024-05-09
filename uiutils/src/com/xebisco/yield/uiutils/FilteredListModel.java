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

package com.xebisco.yield.uiutils;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.io.Serial;
import java.util.ArrayList;

public class FilteredListModel extends AbstractListModel<String> {
    @Serial
    private static final long serialVersionUID = -1442671115070996507L;

    public static interface Filter {
        boolean accept(String element);
    }

    private final ListModel<String> _source;
    private Filter _filter;
    private final ArrayList<Integer> _indices = new ArrayList<Integer>();

    public FilteredListModel(ListModel<String> source) {
        if (source == null)
            throw new IllegalArgumentException("Source is null");
        _source = source;
        _source.addListDataListener(new ListDataListener() {
            public void intervalRemoved(ListDataEvent e) {
                doFilter();
            }

            public void intervalAdded(ListDataEvent e) {
                doFilter();
            }

            public void contentsChanged(ListDataEvent e) {
                doFilter();
            }
        });
    }

    public FilteredListModel(String[] source) {
        this(new AbstractListModel<>() {

            @Override
            public int getSize() {
                return source.length;
            }

            @Override
            public String getElementAt(int index) {
                return source[index];
            }
        });
    }

    public FilteredListModel setFilter(Filter f) {
        _filter = f;
        doFilter();
        return this;
    }

    public void doFilter() {
        _indices.clear();

        Filter f = _filter;
        if (f != null) {
            int count = _source.getSize();
            for (int i = 0; i < count; i++) {
                String element = _source.getElementAt(i);
                if (f.accept(element)) {
                    _indices.add(i);
                }
            }
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }

    public int getSize() {
        return (_filter != null) ? _indices.size() : _source.getSize();
    }

    public String getElementAt(int index) {
        return (_filter != null) ? _source.getElementAt(_indices.get(index)) : _source.getElementAt(index);
    }
}