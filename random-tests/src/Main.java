import com.xebisco.yield.*;

public class Main extends Scene {

    public static final Entity2DPrefab HELLO_WORLD_TEXT_PREFAB = StandardPrefabs.texRectangle("com/xebisco/yield/img.png");


    public Main(Application application) {
        super(application);
    }

    Entity2D e;

    @Override
    public void onStart() {
        e = instantiate(HELLO_WORLD_TEXT_PREFAB);
        e.getComponent(TextureRectangle.class);
    }

    private double a;

    @Override
    public void onUpdate() {
        a += getApplication().getAxis(HORIZONTAL) / 100.0;
        e.getComponent(TextureRectangle.class).getColor().setRed(Global.clamp(a, 0, 1));
        System.out.println(1 / getTime().getDeltaTime());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(0);
        ApplicationManager manager = new ApplicationManager(time);
        new Application(manager, Main.class, Global.Platforms.swingOpenAL(), new PlatformInit(PlatformInit.INPUT_DEFAULT));
        manager.run();
    }
}