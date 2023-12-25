import com.xebisco.yield.*;
import com.xebisco.yield.manager.ApplicationManager;
import com.xebisco.yield.physics.BodyType;
import com.xebisco.yield.physics.PhysicsBody;
import com.xebisco.yield.physics.PhysicsSystem;
import com.xebisco.yield.texture.TexturedSquareMesh;

import java.util.concurrent.CompletableFuture;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        systems().add(new PhysicsSystem());
        instantiate(new Entity2DPrefab(new ComponentCreation(TextMesh.class), new ComponentCreation(Movement.class), new ComponentCreation(PhysicsBody.class, c -> ((PhysicsBody) c).setType(BodyType.DYNAMIC).setGravityScale(0))));
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        ApplicationManager manager = new ApplicationManager(time);
        PlatformInit init = new PlatformInit(PlatformInit.PC_DEFAULT);
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), init);
        manager.run();
    }
}