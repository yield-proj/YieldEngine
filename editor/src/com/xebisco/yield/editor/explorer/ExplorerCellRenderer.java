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

package com.xebisco.yield.editor.explorer;

import com.xebisco.yield.editor.Assets;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;

public class ExplorerCellRenderer  extends DefaultTreeCellRenderer {

    private final File mainDir;

    public ExplorerCellRenderer(File mainDir) {
        this.mainDir = mainDir;
    }

    private static final Icon closed =
            (Icon) UIManager.get("InternalFrame.maximizeIcon");
    private static final Icon open =
            (Icon) UIManager.get("InternalFrame.minimizeIcon");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        setOpenIcon(getDefaultOpenIcon());
        setClosedIcon(getDefaultClosedIcon());
        setLeafIcon(getDefaultLeafIcon());
        super.getTreeCellRendererComponent(
                tree, value, sel, exp, leaf, row, hasFocus);
        String s = value.toString();
        s = s.replace("[", "");
        s = s.replace("]", "");
        s = s.replace(", ", "\\");

        File f = Explorer.fPath(tree.getPathForRow(row), mainDir);
        if(f != null && f.isDirectory()) {
            setIcon(getDefaultClosedIcon());
        }

        return this;
    }
}