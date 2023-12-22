import com.xebisco.yield.*;
import com.xebisco.yield.manager.ApplicationManager;

public class Main extends Scene {
    public static final Entity2DPrefab HELLO_WORLD_TEXT_PREFAB = new Entity2DPrefab(new ComponentCreation(TextMesh.class, c -> ((TextMesh) c).paint().setText("Hello, World!")));


    public Main(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        instantiate(HELLO_WORLD_TEXT_PREFAB);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.PC_DEFAULT));
        manager.run();
    }
}