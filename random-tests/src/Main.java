import com.xebisco.yield.*;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(16666);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        platformInit.setFullscreen(true);
        Application application = new Application(applicationManager, Main.class, PlatformGraphics.swingGraphics(), platformInit);
        Global.setMainApplication(application);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        instantiate(new Entity2DPrefab(new TextureRectangle(), new Comp()));
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void dispose() {

    }
}

class Comp extends ComponentBehavior {
    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
        getEntity().getTransform().translate(getApplication().getAxis(HORIZONTAL, VERTICAL).multiplyLocal(getTime().getDeltaTime() * 100));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {

    }
}