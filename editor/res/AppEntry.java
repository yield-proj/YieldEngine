import com.xebisco.yield.*;

import com.xebisco.yield.editor.overhead.StartScene;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;

public class AppEntry {
    public static void main(String[] args) {
        final var manager = new ApplicationManager(new ContextTime());
        PlatformInit init;
        try(ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(AppEntry.class.getResourceAsStream("/init.ser")))) {
            init = (PlatformInit) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String platform;
        try(ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(AppEntry.class.getResourceAsStream("/platform.ser")))) {
            platform = (String) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ApplicationPlatform ap;
        try {
            ap = (ApplicationPlatform) Global.Platforms.class.getMethod(platform).invoke(null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        new Application(manager, StartScene.class, ap, init);
        manager.run();
    }
}