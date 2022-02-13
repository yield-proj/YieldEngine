package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;

import java.awt.*;

public abstract class Shape extends Component {

    private Renderer renderer;
    private Obj obj;
    private Color color = Color.CYAN;

    @Override
    public void start() {
        super.start();
        renderer = (Renderer) getComponent("Renderer");
        Color previous = renderer.getGraphics().getColor();
        renderer.getGraphics().setColor(color);
        parameters(renderer.getGraphics());
        obj = renderer.getGraphics().sample();
        renderer.getGraphics().setColor(previous);
        previous(renderer.getGraphics());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        obj.color = color;
        process(obj);
        renderer.getGraphics().shapeRends.add(obj);
    }

    public abstract void process(Obj obj);

    public abstract void parameters(YldGraphics graphics);
    public abstract void previous(YldGraphics graphics);

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
