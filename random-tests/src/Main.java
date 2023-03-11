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
        instantiate(new Entity2DPrefab(new TextureRectangle(), new PhysicsBody(), new Comp(), new RectangleCollider())).setContactAdapter(new ContactAdapter() {
            Entity2D e = instantiate(new Entity2DPrefab(new Text()));

            @Override
            public void onContactBegin(Collider entity, Collider colliding) {
                e.getComponent(Text.class).setContents("COSTÔ");
            }

            @Override
            public void onContactEnd(Collider entity, Collider colliding) {
                e.getComponent(Text.class).setContents("DESENCOSTÔ");
            }
        });
        instantiate(new Entity2DPrefab(new Rectangle(), new PhysicsBody(), new Comp2(), new RectangleCollider()));
    }
}

class Comp extends ComponentBehavior {
    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
        getComponent(PhysicsBody.class).setLinearVelocity(new Vector2D(getApplication().getAxis("Horizontal") * 300 * getTime().getDeltaTime(), getComponent(PhysicsBody.class).getLinearVelocity().y));
        if (getApplication().getAxis("Fire") > 0)
            getComponent(PhysicsBody.class).addForce(new Vector2D(0, 50), ForceType.LINEAR_IMPULSE);
        getApplication().getScene().getCamera().sumLocal(new Vector2D(getApplication().getAxis("HorizontalCam") * 30, getApplication().getAxis("VerticalCam") * 30));
        if (getApplication().getAxis("RightFire") != 0)
            getComponent(PhysicsBody.class).addForce(-getApplication().getAxis("RightFire") * 100, ForceType.ANGULAR_IMPULSE);
        else
            getComponent(PhysicsBody.class).addForce(getApplication().getAxis("LeftFire") * 100, ForceType.ANGULAR_IMPULSE);
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
        getComponent(RectangleCollider.class).setFriction(0f);
    }

    @Override
    public void onUpdate() {

        ///System.out.println(getTransform().getPosition());
    }
}