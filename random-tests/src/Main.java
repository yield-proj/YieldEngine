import com.xebisco.yield.*;

public class Main {
    public static void main(String[] args) {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(16);
        Context context = new Context(time, () -> {
            System.out.println( time.getDeltaTime());
        });
        context.getThread().start();
    }
}