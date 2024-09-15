package com.xebisco.yieldengine.uiutils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class ColliderRender implements Serializable {

    private Point<Integer> position;
    private Point<Integer> size;

    private boolean positionSelected, sizeSelected;

    public ColliderRender(Point<Integer> position, Point<Integer> size) {
        this.position = position;
        this.size = size;
    }

    public void draw(Graphics2D g, float stroke, boolean fill, String name) {
        g.setStroke(new BasicStroke(stroke));

        Rectangle2D.Double rect = new Rectangle2D.Double();

        if (fill) {
            g.setColor(new Color(0, 0, 0, 50));

            rect.setRect(position.getX(), position.getY(), size.getX(), size.getY());
            g.fill(rect);

            g.setColor(new Color(255, 255, 255, 50));
            g.draw(rect);
        }

        g.setStroke(new BasicStroke(1));

        float s = 8 * stroke;

        if (positionSelected) {
            g.setColor(new Color(69, 70, 94));
        } else {
            g.setColor(new Color(29, 30, 45));
        }

        rect.setRect(position.getX() - s / 2, position.getY() - s / 2, s, s);
        g.fill(rect);

        if (sizeSelected) {
            g.setColor(new Color(69, 70, 94));
        } else {
            g.setColor(new Color(29, 30, 45));
        }

        rect.setRect(position.getX() + size.getX() - s / 2, position.getY() + size.getY() - s / 2, s, s);
        g.fill(rect);

        g.setFont(g.getFont().deriveFont(4f));

        if(name != null && !name.isEmpty()) {
            int tw = g.getFontMetrics().stringWidth(name)  + 2;
            int h = g.getFontMetrics().getHeight();
            g.setColor(new Color(29, 30, 45, 50));
            rect.setRect(position.getX() + size.getX() / 2f - tw / 2f, position.getY() + size.getY() / 2f - h / 2f, tw, h);
            g.fill(rect);
            g.setColor(new Color(255, 255, 255, 100));
            g.drawString(name, position.getX() + size.getX() / 2f - (tw - 2) / 2f, position.getY() + size.getY() / 2f + h / 4f);
        }
    }

    /*public boolean update(Point2D.Double mouse, float stroke, boolean pressing) {
        double px = (double) getPosition().getX(), py = (double) getPosition().getY(), sx = (double) getSize().getX(), sy = (double) getSize().getY();
        double s = 20;
        System.out.println(stroke + ", " +  mouse);
        positionSelected = mouse.getX() >= px - s * stroke && mouse.getY() >= py - s * stroke
                && mouse.getX() <= px + s * stroke && mouse.getY() <= py + s * stroke;

        sizeSelected = mouse.getX() >= px + sx - s * stroke && mouse.getY() >= py + sy - s * stroke
                && mouse.getX() <= px + sx + s * stroke && mouse.getY() <= py + sy + s * stroke;

        if(pressing) {
            if (positionSelected) {
                getPosition().setX((int) mouse.getX());
                getPosition().setY((int) mouse.getY());
            }
            if(sizeSelected) {
                getSize().setX((int) (mouse.getX() - getPosition().getX()));
                getSize().setY((int) (mouse.getY() - getPosition().getY()));
            }
        }

        return positionSelected || sizeSelected;
    }*/

    public boolean positionB(Point2D.Double mouse, float stroke) {
        double px = (double) getPosition().getX(), py = (double) getPosition().getY(), sx = (double) getSize().getX(), sy = (double) getSize().getY();
        double s = 10;
        positionSelected = mouse.getX() >= px - s * stroke && mouse.getY() >= py - s * stroke
                && mouse.getX() <= px + s * stroke && mouse.getY() <= py + s * stroke;
        return positionSelected;
    }

    public boolean sizeB(Point2D.Double mouse, float stroke) {
        double px = (double) getPosition().getX(), py = (double) getPosition().getY(), sx = (double) getSize().getX(), sy = (double) getSize().getY();
        double s = 10;
        if(getSize().getX() < 1) getSize().setX(1);
        if(getSize().getY() < 1) getSize().setY(1);
        sizeSelected = mouse.getX() >= px + sx - s * stroke && mouse.getY() >= py + sy - s * stroke
                && mouse.getX() <= px + sx + s * stroke && mouse.getY() <= py + sy + s * stroke;
        return sizeSelected;
    }

    @Override
    public String toString() {
        return "ColliderRender{" +
                "position=" + position +
                ", size=" + size +
                '}';
    }

    public Point<Integer> getPosition() {
        return position;
    }

    public void setPosition(Point<Integer> position) {
        this.position = position;
    }

    public Point<Integer> getSize() {
        return size;
    }

    public void setSize(Point<Integer> size) {
        this.size = size;
    }
}
