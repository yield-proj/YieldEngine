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
            new ComponentCreation(TextMesh.class, c -> ((TextMesh) c).paint().setText("Hello, World!"))
    );

    public Main(Application application) {
        super(application);
    }

    Entity2D e;

    @Override
    public void onStart() {
        super.onStart();
        e = instantiate(prefab);
    }

    @Override
    public void onUpdate(ContextTime time) {
        super.onUpdate(time);
        e.transform().rotate(60 * time.deltaTime());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.PC_DEFAULT));
        manager.run();
    }
}