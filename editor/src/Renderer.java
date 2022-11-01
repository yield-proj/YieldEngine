/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Renderer extends JPanel implements Runnable {
    private static Renderer instance;
    private static boolean canUpdate = true;
    private List<Renderable> renderableList = new ArrayList<>();
    private static NumberWrapper<Integer> panelWidth = new NumberWrapper<>(1280), panelHeight = new NumberWrapper<>(720);

    public Renderer() {
        renderableList.add(new Button(new Point(-300, 100), "Button"));
        instance = this;
        new Thread(this).start();
    }

    private static BufferedImage image = new BufferedImage(1280 / 2, 720 / 2, BufferedImage.TYPE_INT_RGB);
    private static boolean running;

    @Override
    public void update(Graphics g) {
        paintComponent(g);
    }

    @Override
    public void run() {
        repaint();
        running = true;
        while (running) {
            try {
                Thread.sleep(33);
                Point mouse = MouseInfo.getPointerInfo().getLocation();
                Point point = new Point(mouse.x - EditorMain.getFrame().getX() + 22, mouse.y - EditorMain.getFrame().getY());
                if (EditorMain.getMousePressing().contains(MouseEvent.BUTTON1)) {
                    if (point.x > 0 && point.y > 0 && point.x < getWidth() && point.y < getHeight()) {
                        repaint();
                    }
                }
                try {
                    for(Renderable renderable : Renderer.getInstance().getRenderableList()) {
                        if(renderable instanceof Logical) ((Logical) renderable).update();
                    }
                } catch (ConcurrentModificationException ignore) {

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage getImage() {
        return image;
    }

    public static void setImage(BufferedImage image) {
        Renderer.image = image;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Renderer.running = running;
    }

    @Override
    protected void paintComponent(Graphics g) {
        getPanelWidth().setValue(getWidth());
        getPanelHeight().setValue(getHeight());
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(DefColors.background);
        g.fillRect(0, 0, getWidth(), getHeight());
        /*if (image == null || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }*/
        Graphics2D imageG = image.createGraphics();
        imageG.setColor(DefColors.previewBackground);
        imageG.fillRect(0, 0, image.getWidth(), image.getHeight());
        imageG.setColor(Color.RED);
        g.drawImage(image, 0, 0, null);
        try {
            for(Renderable renderable : renderableList) {
                renderable.render(g);
            }
        } catch (ConcurrentModificationException ignore) {

        }
    }

    public static boolean isCanUpdate() {
        return canUpdate;
    }

    public static void setCanUpdate(boolean canUpdate) {
        Renderer.canUpdate = canUpdate;
    }

    public static NumberWrapper<Integer> getPanelWidth() {
        return panelWidth;
    }

    public static void setPanelWidth(NumberWrapper<Integer> panelWidth) {
        Renderer.panelWidth = panelWidth;
    }

    public static NumberWrapper<Integer> getPanelHeight() {
        return panelHeight;
    }

    public static void setPanelHeight(NumberWrapper<Integer> panelHeight) {
        Renderer.panelHeight = panelHeight;
    }

    public List<Renderable> getRenderableList() {
        return renderableList;
    }

    public void setRenderableList(List<Renderable> renderableList) {
        this.renderableList = renderableList;
    }

    public static Renderer getInstance() {
        return instance;
    }

    public static void setInstance(Renderer instance) {
        Renderer.instance = instance;
    }
}
