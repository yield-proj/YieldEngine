import com.xebisco.yield.*;
import com.xebisco.yield.manager.ApplicationManager;
import com.xebisco.yield.physics.BodyType;
import com.xebisco.yield.physics.PhysicsBody;
import com.xebisco.yield.physics.PhysicsSystem;
import com.xebisco.yield.physics.colliders.BoxCollider2D;
import com.xebisco.yield.physics.colliders.CircleCollider2D;
import com.xebisco.yield.physics.colliders.EdgeCollider2D;

public class Main extends Scene {

    private static final Entity2DPrefab prefab = new Entity2DPrefab(
            new ComponentCreation(TextMesh.class, c -> ((TextMesh) c).paint().setText("Hello, World!")),
            new ComponentCreation(Script.class),
            new ComponentCreation(PhysicsBody.class, c -> ((PhysicsBody) c).setType(BodyType.DYNAMIC)),
            new ComponentCreation(CircleCollider2D.class)
    ), prefab2 = new Entity2DPrefab(
            new ComponentCreation(TextMesh.class, c -> ((TextMesh) c).paint().setText("GROUND")),
            new ComponentCreation(PhysicsBody.class, c -> ((PhysicsBody) c).setType(BodyType.STATIC)),
            new ComponentCreation(EdgeCollider2D.class)
    );

    public Main(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        systems().add(new PhysicsSystem().setGravity(new Vector2D(0, -100)));
        instantiate(prefab);
        instantiate(prefab2).transform().translate(0, -100);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.PC_DEFAULT));
        manager.run();
    }
}