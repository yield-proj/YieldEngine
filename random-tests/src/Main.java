import com.xebisco.yield.*;
import com.xebisco.yield.physics.PhysicsBody;
import com.xebisco.yield.physics.RectangleCollider;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(16666);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        Application application = new Application(applicationManager, Main.class, PlatformGraphics.swingGraphics(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getApplication().getScene().getSystems().add(new ExitWithEscapeKey());
        instantiate(new Entity2DPrefab(new TextureRectangle(), new Comp(), new PhysicsBody(), new RectangleCollider()));
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void dispose() {

    }
}

class Comp extends ComponentBehavior {
    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
        if(getApplication().getAxis("Vertical") > 0)
            getComponent(PhysicsBody.class).setLinearVelocity(new org.jbox2d.common.Vec2(0, 1000));
        //getEntity().getTransform().translate(getApplication().getAxis(HORIZONTAL, VERTICAL).multiplyLocal(getTime().getDeltaTime() * 100));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {

    }
}