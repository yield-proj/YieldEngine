import com.xebisco.yield.*;

import java.util.concurrent.CompletableFuture;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        instantiate(new Entity2DPrefab(new ComponentCreation(Tringle.class)));
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        ApplicationManager manager = new ApplicationManager(time);
        PlatformInit init = new PlatformInit(PlatformInit.INPUT_DEFAULT);
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), init);
        manager.run();
    }
}