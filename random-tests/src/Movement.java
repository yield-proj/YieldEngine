import com.xebisco.yield.*;
import com.xebisco.yield.physics.PhysicsBody;

public class Movement extends ComponentBehavior {

    private PhysicsBody body;

    @Override
    public void onStart() {
        body = component(PhysicsBody.class);
    }

    @Override
    public void onUpdate(ContextTime time) {
        if (application().isKeyPressed(Input.Key.VK_W))
            body.setLinearVelocity(new Vector2D(body.linearVelocity().x(), 300));
        transform().rotate(1);
    }
}
