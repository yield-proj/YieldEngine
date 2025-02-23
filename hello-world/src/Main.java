
import com.xebisco.yieldengine.core.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Initialize the game loop context with window dimensions
        LoopContext loop = Global.getOpenGLOpenALLoopContext(1280, 720);

        // Create a new scene
        Scene scene = new Scene();

        // Add text entity to the scene
        scene.getEntityFactories().add(Global.textFactory("Hello, World!"));

        // Set and initialize the scene
        Global.setCurrentScene(scene);
        scene.create();

        // Start the game loop
        loop.startThread();
    }
}