import com.xebisco.yield.*;

public class Main extends Scene {

    public static final Entity2DPrefab HELLO_WORLD_TEXT_PREFAB = StandardPrefabs.text("Hello, World!");


    public Main(Application application) {
        super(application);
    }

    Entity2D e;

    @Override
    public void onStart() {
        instantiate(HELLO_WORLD_TEXT_PREFAB);
    }

    @Override
    public void onUpdate() {
        System.out.println(1 / getTime().getDeltaTime());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(0);
        ApplicationManager manager = new ApplicationManager(time);
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.INPUT_DEFAULT));
        manager.run();
    }
}