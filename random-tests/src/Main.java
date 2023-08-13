import com.xebisco.yield.*;

import java.util.concurrent.CompletableFuture;

public class Main extends Scene {

    public static final Entity2DPrefab HELLO_WORLD_TEXT_PREFAB = StandardPrefabs.texRectangle("com/xebisco/yield/img.png");


    public Main(Application application) {
        super(application);
    }

    Entity2D e;

    @Override
    public void onStart() {
        e = instantiate(HELLO_WORLD_TEXT_PREFAB);
    }

    private double a;

    @Override
    public void onUpdate() {
        e.transform().rotate(getApplication().getAxis(HORIZONTAL));
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        ApplicationManager manager = new ApplicationManager(time);
        PlatformInit init = new PlatformInit(PlatformInit.INPUT_DEFAULT);
        init.setVerticalSync(false);
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), init);
        manager.run();
    }
}