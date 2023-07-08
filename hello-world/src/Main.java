import com.xebisco.yield.*;

public class Main extends Scene {

    // Creating a static final variable named `HELLO_WORLD_TEXT_PREFAB` of type `Entity2DPrefab`. It
    // is initialized with a prefab object created using the `StandardPrefabs.text()` method, which creates a prefab for a
    // 2D text entity with the text "Hello, World!". This prefab can be used to instantiate a new instance of the text
    // entity in the `onStart()` method of the `Main` class.
    public static final Entity2DPrefab HELLO_WORLD_TEXT_PREFAB = StandardPrefabs.text("Hello, World!");


    public Main(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        // `instantiate(HELLO_WORLD_TEXT_PREFAB);` is creating a new instance of the `Entity2D` object using the
        // `HELLO_WORLD_TEXT_PREFAB` prefab and adding it to the current scene.
        instantiate(HELLO_WORLD_TEXT_PREFAB);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        // Creating an instance of the `ApplicationManager` class with a `ContextTime` object as a parameter.
        // The `ContextTime` class is a class that provides timing information for the application.
        // The `ApplicationManager` class is responsible for managing the lifecycle of the application, including starting and stopping it.
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        // The line `new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new
        // PlatformInit(PlatformInit.INPUT_DEFAULT));` is creating a new instance of the `Application` class.
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.INPUT_DEFAULT));
        // `manager.run();` is starting the application main loop. This method is responsible for managing
        // the lifecycle of the application, including starting and stopping it. Once this method is called, the
        // application will start running and the initial scene will be displayed.
        manager.run();
    }
}