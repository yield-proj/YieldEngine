import com.xebisco.yield.*;

public class Main {
    public static void main(String[] args) {
        ContextTime time = new ContextTime();
        Context context = new Context(time, () -> {
            System.out.println(1 / time.getDeltaTime());
        });
        context.getThread().start();
    }
}