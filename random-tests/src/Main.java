import com.xebisco.yield.*;
import com.xebisco.yield.physics.*;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(16666);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        Application application = new Application(applicationManager, Main.class, Global.swingPlatform(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getApplication().getScene().getSystems().add(new ExitWithEscapeKey());

        instantiate(new Entity2DPrefab(new Rectangle(), new Comp(), new PhysicsBody(), new RectangleCollider())).setContactAdapter(new ContactAdapter() {
            Entity2D e, e1;

            @Override
            public void onContactBegin(Collider entity, Collider colliding) {
                e = instantiate(new Entity2DPrefab(new Text("ENCOSTOU AAAAA")));
                e.setVisible(true);
                if (e1 != null)
                    e1.setVisible(false);
            }

            @Override
            public void onContactEnd(Collider entity, Collider colliding) {
                e1 = instantiate(new Entity2DPrefab(new Text("DESINCONSTAL AAAAA")));
                e1.setVisible(true);
                e.setVisible(false);
            }
        });
        instantiate(new Entity2DPrefab(new Rectangle(), new PhysicsBody(), new Comp2(), new RectangleCollider()));
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
        if (getApplication().getAxis("Vertical") > 0)
            getComponent(PhysicsBody.class).setLinearVelocity(new org.jbox2d.common.Vec2(0, 10));
        getEntity().getTransform().translate(getApplication().getAxis(HORIZONTAL, VERTICAL).multiplyLocal(getTime().getDeltaTime() * 100));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {

    }
}

class Comp2 extends ComponentBehavior {
    @Override
    public void onStart() {
        getComponent(PhysicsBody.class).setPosition(new Vector2D(0, -720 / 2f));
        getComponent(PhysicsBody.class).setType(PhysicsType.STATIC);
    }

    @Override
    public void onUpdate() {

        ///System.out.println(getTransform().getPosition());
    }
}