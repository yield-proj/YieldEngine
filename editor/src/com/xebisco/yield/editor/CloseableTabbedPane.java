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
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class CloseableTabbedPane extends DnDTabbedPane {

    static int count = 0;
    private static Icon CLOSING_ICON;
    private static Icon CLOSING_ICON_SELECTED;
    private TabClosingListener tabClosingListener;
    private String iconFileName = "closeIcon.png";
    private String selectedIconFileName = "selectedCloseIcon.png";

    public CloseableTabbedPane() {
        super();
    }

    public CloseableTabbedPane(TabClosingListener aTabClosingListener) {
        super();
        tabClosingListener = aTabClosingListener;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(getTabCount() == 0) {
            String nos = "No Open Scenes";
            g.drawString(nos, getWidth() / 2 - g.getFontMetrics().stringWidth(nos) / 2, getHeight() / 2 - g.getFont().getSize() / 4);
        }
    }

    /**
     * Sets the file name of the closing icon along with the optional variant of the icon when the mouse is over the icon.
     */
    public void setClosingIconFileName(String aIconFileName, String aSelectedIconFileName) {
        iconFileName = aIconFileName;
        selectedIconFileName = aSelectedIconFileName;
    }

    /**
     * Makes the close button at the specified indes visible or invisible
     */
    public void setCloseButtonVisibleAt(int aIndex, boolean aVisible) {
        CloseButtonTab cbt = (CloseButtonTab) getTabComponentAt(aIndex);
        cbt.closingLabel.setVisible(aVisible);
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        setTabComponentAt(index, new CloseButtonTab(component, title, icon));
    }

    @Override
    public void setTitleAt(int index, String title) {
        super.setTitleAt(index, title);
        CloseButtonTab cbt = (CloseButtonTab) getTabComponentAt(index);
        cbt.label.setText(title);
    }

    @Override
    public void setIconAt(int index, Icon icon) {
        super.setIconAt(index, icon);
        CloseButtonTab cbt = (CloseButtonTab) getTabComponentAt(index);
        cbt.label.setIcon(icon);
    }

    @Override
    public void setComponentAt(int index, Component component) {
        CloseButtonTab cbt = (CloseButtonTab) getTabComponentAt(index);
        super.setComponentAt(index, component);
        cbt.tab = component;
    }

    private Icon getImageIcon(String aImageName) {
        URL imageUrl = YieldEditor.class.getResource(aImageName);
        if (imageUrl == null) {
            return new PaintedCrossIcon();
        }
        ImageIcon result = new ImageIcon(imageUrl);
        if (result.getIconWidth() != -1) {
            return result;
        } else {
            return null;
        }
    }

    interface TabClosingListener {
        /**
         * @param aTabIndex the index of the tab that is about to be closed
         * @return true if the tab can be really closed
         */
        boolean tabClosing(int aTabIndex);

        /**
         * @param aTabIndex the index of the tab that is about to be closed
         * @return true if the tab should be selected before closing
         */
        boolean selectTabBeforeClosing(int aTabIndex);
    }

    static class PaintedCrossIcon implements Icon {

        int size = 10;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.WHITE);
            g.drawLine(x + 2, y + 2, x + size - 2, y + size - 2);
            g.drawLine(x + size - 2, y + 2, x + 2, y + size - 2);
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

    }

    private class CloseButtonTab extends JPanel {
        private Component tab;
        private JLabel label;
        private JLabel closingLabel;

        public CloseButtonTab(Component aTab, String aTitle, Icon aIcon) {
            tab = aTab;
            setOpaque(false);
            setLayout(new GridBagLayout());
            setVisible(true);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 5);

            label = new JLabel(aTitle);
            label.setIcon(aIcon);
            add(label, gbc);
            if (CLOSING_ICON == null) {
                CLOSING_ICON = getImageIcon(iconFileName);
                CLOSING_ICON_SELECTED = getImageIcon(selectedIconFileName);
            }
            closingLabel = new JLabel(CLOSING_ICON);
            closingLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JTabbedPane tabbedPane = (JTabbedPane) getParent().getParent().getParent().getParent();
                    int tabIndex = indexOfComponent(tab);
                    if (tabClosingListener != null) {
                        if (tabClosingListener.selectTabBeforeClosing(tabIndex)) {
                            tabbedPane.setSelectedIndex(tabIndex);
                        }
                        if (tabClosingListener.tabClosing(tabIndex)) {
                            tabbedPane.removeTabAt(tabIndex);
                        }
                    } else {
                        tabbedPane.removeTabAt(tabIndex);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (CLOSING_ICON_SELECTED != null) {
                        closingLabel.setIcon(CLOSING_ICON_SELECTED);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (CLOSING_ICON_SELECTED != null) {
                        closingLabel.setIcon(CLOSING_ICON);
                    }
                }
            });
            gbc.insets = new Insets(0, 0, 0, 0);
            add(closingLabel, gbc);
        }
    }
}