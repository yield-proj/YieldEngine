import com.xebisco.yield.*;

public class Main extends Scene {
    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(16666);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        Application application = new Application(new Main(), PlatformGraphics.swingGraphics(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getEntities().add(new Entity2D(null, new Rectangle(), new Comp()));
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
        getEntity().getTransform().setzRotation(getFrames());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {

    }
}