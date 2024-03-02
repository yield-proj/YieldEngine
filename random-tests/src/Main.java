import com.xebisco.yield.*;
import com.xebisco.yield.manager.ApplicationManager;
import com.xebisco.yield.texture.TexturedRectangleMesh;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    private final static Entity2DPrefab HELLO_WORLD_PREFAB = new Entity2DPrefab(
            new ComponentCreation(TexturedRectangleMesh.class),
            new ComponentCreation(TextMesh.class, c -> ((TextMesh) c).setContents("Hello, World!"))
    );


    @Override
    public void onStart() {
        super.onStart();
        instantiate(HELLO_WORLD_PREFAB);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime().setTargetFPS(60);
        ApplicationManager manager = new ApplicationManager(time);
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.PC_DEFAULT));
        manager.run();
    }
}