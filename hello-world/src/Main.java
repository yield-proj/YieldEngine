
import com.xebisco.yieldengine.core.*;

public class Main {
    public static void main(String[] args) throws Exception {
        LoopContext loop = Global.getOpenGLOpenALLoopContext(1280, 720);
        Scene scene = new Scene();

        scene.getEntityFactories().add(Global.textFactory("Hello, World!"));

        Global.setCurrentScene(scene);
        scene.create();

        loop.startThread();
    }
}
