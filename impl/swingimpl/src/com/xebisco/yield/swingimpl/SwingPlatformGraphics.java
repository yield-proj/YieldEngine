/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield.swingimpl;

import com.xebisco.yield.DrawInstruction;
import com.xebisco.yield.PlatformGraphics;
import com.xebisco.yield.PlatformInit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;

public class SwingPlatformGraphics implements PlatformGraphics {
    private VolatileImage renderImage;
    private GraphicsDevice device;
    private GraphicsConfiguration graphicsConfiguration;
    private JPanel panel;
    private JFrame frame;
    private Graphics2D graphics;
    private AffineTransform defaultTransform = new AffineTransform();

    class SwingImplPanel extends JPanel {
        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            Dimension imageSize = onSizeBoundary(renderImage, getSize());
            g.drawImage(renderImage, (getWidth() - imageSize.width) / 2, (getHeight() - imageSize.height) / 2, imageSize.width, imageSize.height, this);
        }
    }

    public static Dimension onSizeBoundary(Image image, Dimension boundary) {
        int original_width = image.getWidth(null);
        int original_height = image.getHeight(null);
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width;
        int new_height;

        new_height = bound_height;
        new_width = (new_height * original_width) / original_height;
        if (new_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        return new Dimension(new_width, new_height);
    }

    @Override
    public void dispose() {
        renderImage.flush();
        panel = null;
        frame.dispose();
        frame = null;
    }

    @Override
    public void init(PlatformInit platformInit) {
        System.setProperty("sun.java2d.opengl", "True");
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        graphicsConfiguration = device.getDefaultConfiguration();
        panel = new SwingImplPanel();
        frame = new JFrame();
        frame.add(panel);
        renderImage = graphicsConfiguration.createCompatibleVolatileImage(
                (int) platformInit.getResolution().getWidth(),
                (int) platformInit.getResolution().getHeight(),
                Transparency.OPAQUE
        );
        if (platformInit.isUndecorated()) {
            frame.setUndecorated(true);
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(platformInit.getTitle());
        frame.setSize((int) platformInit.getWindowSize().getWidth(), (int) platformInit.getWindowSize().getHeight());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        if (platformInit.isFullscreen() && device.isFullScreenSupported()) {
            device.setFullScreenWindow(frame);
        }
    }

    @Override
    public void frame() {
        graphics = renderImage.createGraphics();
        defaultTransform = graphics.getTransform();
    }

    @Override
    public void draw(DrawInstruction drawInstruction) {
        int x = (int) (drawInstruction.getPosition().getX() - drawInstruction.getSize().getWidth() / 2.0) + renderImage.getWidth() / 2,
                y = (int) (drawInstruction.getPosition().getY() - drawInstruction.getSize().getHeight() / 2.0) + renderImage.getHeight() / 2,
                w = (int) drawInstruction.getSize().getWidth(),
                h = (int) drawInstruction.getSize().getHeight();
        if (drawInstruction.getRotation() != 0)
            graphics.rotate(Math.toRadians(-drawInstruction.getRotation()), x, y);
        switch (drawInstruction.getType()) {
            case RECTANGLE -> {
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillRect(x, y, w, h);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawRect(x, y, w, h);
                }
            }
            case OVAL -> {
                if (drawInstruction.isFilled()) {
                    graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                    graphics.fillOval(x, y, w, h);
                }
                if (drawInstruction.getBorderColor() != null) {
                    graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                    graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                    graphics.drawOval(x, y, w, h);
                }
            }
            case IMAGE -> graphics.drawImage((Image) drawInstruction.getRenderRef(), x, y, w, h, null);
            case TEXT -> {
                graphics.setColor(awtColor(drawInstruction.getInnerColor()));
                graphics.drawString((String) drawInstruction.getRenderRef(), x, y);
            }
            case SIMPLE_LINE -> {
                graphics.setColor(awtColor(drawInstruction.getBorderColor()));
                graphics.setStroke(new BasicStroke((float) drawInstruction.getBorderThickness()));
                graphics.drawLine(x, y, w, h);
            }
            default -> throw new UnsupportedDrawInstructionException(drawInstruction.getType().name());
        }
    }

    @Override
    public void resetRotation() {
        graphics.setTransform(new AffineTransform(defaultTransform));
    }

    @Override
    public boolean shouldClose() {
        if (!frame.isDisplayable()) {
            dispose();
            return true;
        }
        return false;
    }

    @Override
    public void conclude() {
        graphics.dispose();
        panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
    }

    public static Color awtColor(com.xebisco.yield.Color yieldColor) {
        return new Color(yieldColor.getRGBA(), true);
    }

    public VolatileImage getRenderImage() {
        return renderImage;
    }

    public void setRenderImage(VolatileImage renderImage) {
        this.renderImage = renderImage;
    }

    public GraphicsDevice getDevice() {
        return device;
    }

    public void setDevice(GraphicsDevice device) {
        this.device = device;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return graphicsConfiguration;
    }

    public void setGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        this.graphicsConfiguration = graphicsConfiguration;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public AffineTransform getDefaultTransform() {
        return defaultTransform;
    }

    public void setDefaultTransform(AffineTransform defaultTransform) {
        this.defaultTransform = defaultTransform;
    }
}
