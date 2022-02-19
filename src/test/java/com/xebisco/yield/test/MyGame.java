package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Rectangle;
import com.xebisco.yield.components.Sprite;
import com.xebisco.yield.extensions.AntialiasingExtension;
import com.xebisco.yield.extensions.FullscreenDetectorExtension;
import com.xebisco.yield.utils.TransformSave;

import java.awt.event.KeyEvent;

class MyGame extends YldGame {

    Entity player;

    @Override
    public void create() {
        addExtension(new FullscreenDetectorExtension());
        addExtension(new AntialiasingExtension());
        player = instantiate();
        player.addComponent(new Sprite());
        player.addComponent(new Rectangle());
        player.addComponent(new MovementScript());
        player.getSelfTransform().translate(100, 100);
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
        new View(427, 240);
    }
}

class MovementScript extends Component {

    float w, h;

    @Override
    public void create() {
        Sprite s = getComponent(Sprite.class);
        Rectangle r = getComponent(Rectangle.class);
        r.setWidth(100);
        r.setHeight(30);
        w = s.getWidth();
        h = s.getHeight();
    }

    @Override
    public void update(float delta) {
        if (input.isPressing(KeyEvent.VK_LEFT))
            transform.translate(-1, 0);
        if (input.isPressing(KeyEvent.VK_RIGHT))
            transform.translate(1, 0);
        if (input.isPressing(KeyEvent.VK_UP))
            transform.translate(0, -1);
        if (input.isPressing(KeyEvent.VK_DOWN))
            transform.translate(0, 1);
        if (transform.position.x - w / 2 < 0)
            transform.translate(1, 0);
        if (transform.position.y - h / 2 < 0)
            transform.translate(0, 1);
        if (transform.position.x + w / 2 > View.getActView().getWidth())
            transform.translate(-1, 0);
        if (transform.position.y + h / 2 > View.getActView().getHeight())
            transform.translate(0, -1);
        if (input.isPressing(KeyEvent.VK_D))
            transform.rotate(1);
        if (input.isPressing(KeyEvent.VK_A))
            transform.rotate(-1);
        if(input.justPressed(KeyEvent.VK_SPACE)) {
            transform.saveTo(new TransformSave("/com/xebisco/yield/test/Save"));
        }
        if(input.justPressed(KeyEvent.VK_ENTER)) {
            transform.load(new TransformSave("/com/xebisco/yield/test/Save"));
        }
    }
}