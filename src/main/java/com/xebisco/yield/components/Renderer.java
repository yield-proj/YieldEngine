package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Obj;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGraphics;
import com.xebisco.yield.graphics.Texture;

import java.awt.*;

public class Renderer extends Component {
    private YldGraphics graphics;
    private Obj obj;
    private Texture texture;
    private boolean show = true;

    public Renderer(YldGraphics graphics, Texture texture) {
        this.graphics = graphics;
        this.texture = texture;
    }

    @Override
    public void create() {
        Image image = null;
        Color color = Color.RED;
        if (texture.isImg()) {
            image = texture.getImage();
        } else {
            color = texture.getMaterial().getColor();
        }
        obj = new Obj(0, 0, 0, 0, Obj.ShapeType.RECT, true, color, null, null, image);
    }

    @Override
    public void update(float delta) {
        if (show) {
            if (!graphics.shapeRends.contains(obj)) {
                graphics.shapeRends.add(obj);
            }
            obj.x = (int) transform.position.x;
            obj.y = (int) transform.position.y;
            obj.x2 = (int) transform.scale.x;
            obj.y2 = (int) transform.scale.y;
        } else {
            graphics.shapeRends.remove(obj);
        }

    }

    public YldGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(YldGraphics graphics) {
        this.graphics = graphics;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
