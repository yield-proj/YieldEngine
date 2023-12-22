import com.xebisco.yield.ComponentBehavior;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.Vector2D;

public class Movement extends ComponentBehavior {
    @Override
    public void onUpdate(ContextTime time) {
        transform().translate(application().axis2D("Horizontal", "Vertical").multiplyLocal(new Vector2D(5, 5)));
        transform().rotate(1);
    }
}
