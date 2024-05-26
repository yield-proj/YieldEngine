import com.xebisco.yield.*;

public class *_CLASSSCENENAME_* extends Scene {
    private final Entity2DPrefab[] prefabs;

    public *_CLASSSCENENAME_*(Application app, Entity2DPrefab[] prefabs) {
        super(app);
        this.prefabs = prefabs;
    }

    @Override
    public void onStart() {
        for(Entity2DPrefab prefab : prefabs) {
            instantiate(prefab);
        }
    }
}