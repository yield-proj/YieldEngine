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
        body.applyLinearImpulse(application().axis2D("Horizontal", "Vertical").multiplyLocal(time.deltaTime()));
        transform().rotate(1);
    }
}
