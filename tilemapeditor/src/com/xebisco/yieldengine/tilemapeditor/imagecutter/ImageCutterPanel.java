package com.xebisco.yieldengine.tilemapeditor.imagecutter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class ImageCutterPanel extends JPanel {

    private BufferedImage image;
    private JLabel zoomLabel = new JLabel();

    private ZoomPanel imagePanel = new ZoomPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            processZoom((Graphics2D) g);
            g.drawImage(image, 0, 0, this);
        }
    };

    public ImageCutterPanel(BufferedImage image) {
        setLayout(new BorderLayout());
        this.image = image;

        add(imagePanel, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.add(zoomLabel);

        add(toolBar, BorderLayout.NORTH);
    }
}
