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

package com.xebisco.yield.tileeditor.app;

import com.xebisco.yield.tileeditor.app.tool.PaintTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class MapEditor extends JFrame {
    private final Project project;
    private final MapCanvas mapCanvas;
    private final Dimension gridSize, grid = new Dimension(16, 16);
    private Tool selectedTool;

    private Tool[] tools = new Tool[]{
            new PaintTool()
    };

    public MapEditor(Project project) {
        this.project = project;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO do you want to save?
            }
        });
        setTitle("Tile Editor | " + project.name());
        setSize(1280, 720);
        setMinimumSize(new Dimension(600, 600));
        add(new JScrollPane(mapCanvas = new MapCanvas()));

        JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
        for (Tool tool : tools) {
            JButton button = new JButton(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedTool = tool;
                }
            });
            button.setIcon(tool.icon());
            button.setToolTipText(tool.showName());
            toolBar.add(button);
        }

        add(toolBar, BorderLayout.WEST);

        setLocationRelativeTo(null);
        setVisible(true);

        gridSize = new Dimension(project.map().map().length, project.map().map()[0].length);


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    class MapCanvas extends JPanel implements MouseMotionListener, MouseListener {

        public MapCanvas() {
            addMouseMotionListener(this);
            addMouseListener(this);
        }

        private Point gridLocation16(Point2D.Double point) {
            boolean negativeX = point.x < 0, negativeY = point.y < 0;
            Point p = new Point((((int) Math.abs(point.getX() - (double) grid.width / 2) + grid.width / 2) / grid.width) * grid.width, ((((int) Math.abs(point.getY() - (double) grid.height / 2) + grid.height / 2) / grid.height) * grid.height));
            if (negativeX) p.x *= -1;
            if (negativeY) p.y *= -1;
            return p;
        }

        private Point gridLocation(Point2D.Double point) {
            boolean negativeX = point.x < 0, negativeY = point.y < 0;
            Point p = new Point((((int) Math.abs(point.getX() - (double) grid.width / 2) + grid.width / 2) / grid.width), ((((int) Math.abs(point.getY() - (double) grid.height / 2) + grid.height / 2) / grid.height)));
            if (negativeX) p.x *= -1;
            if (negativeY) p.y *= -1;
            return p;
        }


        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            mapCanvas.setPreferredSize(new Dimension(gridSize.width * grid.width, gridSize.height * grid.height));

            g.setColor(new Color(95, 179, 252, 65));

            Point mp = getMousePosition();

            if (mp != null) {
                Point2D.Double p = new Point2D.Double(mp.x, mp.y);
                mp = gridLocation16(p);

                g.fillRect(mp.x, mp.y, grid.width, grid.height);
            }


            g.setColor(new Color(180, 180, 180, 80));
            ((Graphics2D) g).setStroke(new BasicStroke(1));

            for (int x = 0; x < project.map().map().length; x++) {
                g.drawLine(x * grid.width, 0, x * grid.width, getHeight());
            }
            for (int y = 0; y < project.map().map()[0].length; y++) {
                g.drawLine(0, y * grid.height, getWidth(), y * grid.height);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedTool != null)
                selectedTool.press(gridLocation(new Point2D.Double(e.getPoint().x, e.getPoint().y)), MapEditor.this);
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectedTool != null)
                selectedTool.press(gridLocation(new Point2D.Double(e.getPoint().x, e.getPoint().y)), MapEditor.this);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedTool != null)
                selectedTool.process(gridLocation(new Point2D.Double(e.getPoint().x, e.getPoint().y)), MapEditor.this);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public Project project() {
        return project;
    }

    public MapCanvas mapCanvas() {
        return mapCanvas;
    }

    public Dimension gridSize() {
        return gridSize;
    }

    public Dimension grid() {
        return grid;
    }

    public Tool selectedTool() {
        return selectedTool;
    }

    public MapEditor setSelectedTool(Tool selectedTool) {
        this.selectedTool = selectedTool;
        return this;
    }
}
