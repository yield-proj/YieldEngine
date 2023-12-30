import com.xebisco.yield.ComponentBehavior;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.Global;
import com.xebisco.yield.physics.PhysicsBody;

public class Script extends ComponentBehavior {

    private PhysicsBody pb;

    @Override
    public void onStart() {
        pb = component(PhysicsBody.class);
    }

    @Override
    public void onUpdate(ContextTime time) {
        pb.applyAngularImpulse(application().axis(Global.HORIZONTAL) * 100);
    }
}
