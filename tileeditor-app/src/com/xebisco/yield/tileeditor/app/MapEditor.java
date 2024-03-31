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
import com.xebisco.yield.uiutils.props.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.Objects;

public class MapEditor extends JFrame {
    private final Project project;
    private final TileMap map;
    private final MapCanvas mapCanvas;
    private final Dimension gridSize, grid = new Dimension(16, 16);
    private Tool selectedTool;
    private JScrollPane mapScrollPane;
    private JList<Tile> tileJList = new JList<>();

    private Tool[] tools = new Tool[]{
            new PaintTool()
    };

    public MapEditor(Project project) {
        this.project = project;

        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(ProjectEditor.class.getResourceAsStream("/logo/logo.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        JPanel mapPanel = new JPanel(new BorderLayout());

        mapPanel.add(mapScrollPane = new JScrollPane(mapCanvas = new MapCanvas()));

        JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
        for (Tool tool : tools) {
            JButton button = new JButton(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setTool(tool);
                }
            });
            button.setIcon(tool.icon());
            button.setToolTipText(tool.showName());
            toolBar.add(button);
        }

        mapPanel.add(toolBar, BorderLayout.WEST);

        try(ObjectInputStream oi = new ObjectInputStream(new FileInputStream(new File(project.path().getParentFile(), "te_map.ser")))) {
            map = (TileMap) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        gridSize = new Dimension(map.map().length, map.map()[0].length);


        //TileSet

        JPanel tileSetPanel = new JPanel(new BorderLayout());

        tileJList.setCellRenderer(new TileListRenderer());
        tileJList.setVisibleRowCount(0);

        tileJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        tileSetPanel.add(new JScrollPane(tileJList));

        updateTiles();


        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Tile Map", mapPanel);

        tabbedPane.addTab("Tile Set", tileSetPanel);

        add(tabbedPane);


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new AbstractAction("New Tile From Image") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(MapEditor.this, true);
                dialog.setTitle("New Tile");

                Prop[] props = new Prop[]{
                        new TextFieldProp("Name", "Empty Tile"),
                        new TextFieldProp("Instance Entity", ""),
                        new ImageFileProp("Tile Image", null),
                        new SizeProp("Size", new Dimension(grid))
                };

                PropPanel propPanel = new PropPanel(props);
                propPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                dialog.add(propPanel);

                JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton applyButton = new JButton(new AbstractAction("Create") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Map<String, Serializable> values = PropPanel.values(props);
                        try {
                            BufferedImage image;
                            if (values.get("Tile Image") == null || ((String) values.get("Tile Image")).isEmpty()) image = null;
                            else image = ImageIO.read(new File((String) values.get("Tile Image")));
                            map.tileSet().tiles().add(new Tile((String) values.get("Name"), (String) values.get("Instance Entity"), image, ((Dimension) values.get("Size")).width, ((Dimension) values.get("Size")).height));
                            updateTiles();
                            tabbedPane.setSelectedIndex(1);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(dialog, ex.getMessage(), ex.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                        }
                        dialog.dispose();
                    }
                });
                dialog.getRootPane().setDefaultButton(applyButton);
                bottomPanel.add(applyButton);
                bottomPanel.add(new JButton(new AbstractAction("Cancel") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                }));

                dialog.add(bottomPanel, BorderLayout.SOUTH);

                dialog.setMinimumSize(new Dimension(500, 350));
                dialog.setLocationRelativeTo(MapEditor.this);
                dialog.setVisible(true);
                updateTiles();
            }
        });

        fileMenu.addSeparator();

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTiles() {
        tileJList.setModel(new AbstractListModel<>() {
            @Override
            public int getSize() {
                return map.tileSet().tiles().size();
            }

            @Override
            public Tile getElementAt(int index) {
                return map.tileSet().tiles().get(index);
            }
        });
    }

    private void setTool(Tool tool) {
        selectedTool = tool;
    }

    class MapCanvas extends JPanel implements MouseMotionListener, MouseListener {

        public MapCanvas() {
            addMouseMotionListener(this);
            addMouseListener(this);
        }

        private Point gridLocation16(Point2D.Double point) {
            if (point.x >= gridSize.width * grid.width || point.y >= gridSize.height * grid.height)
                return new Point(0, 0);
            boolean negativeX = point.x < 0, negativeY = point.y < 0;
            Point p = new Point((((int) Math.abs(point.getX() - (double) grid.width / 2) + grid.width / 2) / grid.width) * grid.width, ((((int) Math.abs(point.getY() - (double) grid.height / 2) + grid.height / 2) / grid.height) * grid.height));
            if (negativeX) p.x *= -1;
            if (negativeY) p.y *= -1;
            return p;
        }

        private Point gridLocation(Point2D.Double point) {
            if (point.x >= gridSize.width * grid.width || point.y >= gridSize.height * grid.height)
                return new Point(0, 0);
            boolean negativeX = point.x < 0, negativeY = point.y < 0;
            Point p = new Point((((int) Math.abs(point.getX() - (double) grid.width / 2) + grid.width / 2) / grid.width), ((((int) Math.abs(point.getY() - (double) grid.height / 2) + grid.height / 2) / grid.height)));
            if (negativeX) p.x *= -1;
            if (negativeY) p.y *= -1;
            return p;
        }


        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, gridSize.width * grid.width, gridSize.height * grid.height);

            setPreferredSize(new Dimension(gridSize.width * grid.width, gridSize.height * grid.height));
            validate();

            g.setColor(new Color(95, 179, 252, 65));

            Point mp = getMousePosition();

            if (mp != null) {
                Point2D.Double p = new Point2D.Double(mp.x, mp.y);
                mp = gridLocation16(p);

                g.fillRect(mp.x, mp.y, grid.width, grid.height);
            }


            g.setColor(new Color(180, 180, 180, 80));
            ((Graphics2D) g).setStroke(new BasicStroke(1));

            for (int x = 0; x < map.map().length; x++) {
                g.drawLine(x * grid.width, 0, x * grid.width, getHeight());
            }
            for (int y = 0; y < map.map()[0].length; y++) {
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
