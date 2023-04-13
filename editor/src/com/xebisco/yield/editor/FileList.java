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

package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileList extends JList<File> {

    private List<File> favorites;

    private File directory;

    public FileList(File directory) {
        super(directory.listFiles());
        this.directory = directory;
    }

    public void reload() {
        if (!directory.isDirectory()) {
            setDirectory(directory.getParentFile());
        }
        System.out.println(Arrays.toString(directory.listFiles()));
        setListData(directory.listFiles());
    }

    @Override
    public JPopupMenu getComponentPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        if (getSelectedValue() != null) {
            popupMenu.add(getSelectedValue().getName());
            popupMenu.addSeparator();
            if (favorites.contains(getSelectedValue()))
                popupMenu.add(new JMenuItem(new AbstractAction("Remove from favorites") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        favorites.remove(getSelectedValue());
                    }
                }));
            else
                popupMenu.add(new JMenuItem(new AbstractAction("Add to favorites") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        favorites.add(getSelectedValue());
                    }
                }));

            popupMenu.add(new JMenuItem(new AbstractAction("Delete") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int r = JOptionPane.showConfirmDialog(getRootPane(), "Delete file '" + getSelectedValue().getAbsolutePath() + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (r == JOptionPane.YES_OPTION) {
                        getSelectedValue().delete();
                        reload();
                    }
                }
            }));
            popupMenu.addSeparator();
        }
        popupMenu.add(new JMenuItem(new AbstractAction("Reload") {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        }));
        return popupMenu;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public List<File> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<File> favorites) {
        this.favorites = favorites;
    }
}
