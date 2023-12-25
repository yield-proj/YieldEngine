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
        body.applyLinearImpulse(application().axis2D("Horizontal", "Vertical").multiplyLocal(new Vector2D(5, 5)));
        transform().rotate(1);
    }
}
