import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.test.blankrendermaster.BlankRenderMaster;

public class Main extends YldGame {
    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.renderMaster = new BlankRenderMaster();
        launch(new Main(), config);
    }
}