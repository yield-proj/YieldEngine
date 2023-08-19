import com.xebisco.yield.Colors;
import com.xebisco.yield.Texture;
import com.xebisco.yield.TextureRectangle;

public class Tringle extends TextureRectangle {

    int x, y;

    @Override
    public void onStart() {
        setTexture(new Texture("com/xebisco/yield/img.png", textureManager()));
    }

    @Override
    public void onUpdate() {
        x = (int) application().mousePosition().x();
        y = (int) -application().mousePosition().y();
    }

    @Override
    public void setupX(float[] verticesX) {
        super.setupX(verticesX);
        verticesX[0] = (float) x;
        verticesX[1] = (float) (size().width() / 2.);
        verticesX[2] = (float) (size().width() / 2.);
        verticesX[3] = (float) (-size().width() / 2.);
    }

    @Override
    public void setupY(float[] verticesY) {
        super.setupY(verticesY);
        verticesY[0] = (float) y;
        verticesY[1] = (float) (-size().height() / 2.);
        verticesY[2] = (float) (size().height() / 2.);
        verticesY[3] = (float) (size().height() / 2.);
    }
}
