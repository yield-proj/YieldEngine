package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.config.GameConfiguration;
import com.xebisco.yield.graphics.Material;
import com.xebisco.yield.graphics.Texture;
import com.xebisco.yield.physics.components.Rigidbody;
import com.xebisco.yield.utils.Vector2;

public class MyGame extends YldGame {

    @Override
    public void create() {
        new View(1280, 720);
        Entity e = entity(new Texture(new Material()));
        e.addComponent(new Controller());
        e.addComponent(new Rigidbody());
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }

}

class Controller extends Component {

    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {
        Rigidbody rigidbody = ((Rigidbody) getComponent("Rigidbody"));

        if(getFrames() == 60) {
            rigidbody.addForce(new Vector2(1000, 0));
        }

        if(getFrames() == 300) {
            rigidbody.addForce(new Vector2(0, -1000));
        }

        if (transform.position.y + transform.size.y > View.getActView().getHeight()) {
            rigidbody.velocity.y = -150;
            rigidbody.colliding = true;
        } else {
            rigidbody.colliding = false;
        }
    }

    public void changeSpeed(Integer x, Integer y) {

    }
}