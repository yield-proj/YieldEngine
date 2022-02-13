package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;

public class Renderer extends Component {
    private final YldGraphics graphics = new YldGraphics();
    private boolean show = true;

    @Override
    public void update(float delta) {
        if (show) {
            for (Obj obj : graphics.shapeRends) {
                if (!game.getScene().getGraphics().shapeRends.contains(obj))
                    game.getScene().getGraphics().shapeRends.add(obj);
            }
        } else {
            for (Obj obj : graphics.shapeRends) {
                game.getScene().getGraphics().shapeRends.remove(obj);
            }
        }
        while (graphics.shapeRends.size() > 0)
            graphics.shapeRends.clear();
    }

    public YldGraphics getGraphics() {
        return graphics;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
