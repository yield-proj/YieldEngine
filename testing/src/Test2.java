/*
 * Copyright [2022-2024] [Xebisco]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.xebisco.yieldengine.core.*;
import com.xebisco.yieldengine.core.input.Axis;
import com.xebisco.yieldengine.core.io.texture.TextureMap;
import com.xebisco.yieldengine.core.tilemap.Tile;
import com.xebisco.yieldengine.core.tilemap.TileMap;
import org.joml.Vector4f;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Test2 {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Logger.setInstance(new Logger(true, true));
        LoopContext l = Global.getOpenGLOpenALLoopContext(1280, 720);
        Scene s = new Scene();

        Entity e = new Entity("hw", new Transform());
        TextureMap map = new TextureMap("yieldIcon.png");

        HashMap<Integer, Tile> tileSet = new HashMap<>();

        tileSet.put(0, new Tile(
                map.getTexture(0, 0, 600, 600),
                false,
                new Vector4f(1, 1, 1, 1),
                (EntityFactory) () -> null
        ));

        TileMap tileMap = new TileMap(tileSet, new int[][]{
                new int[]{0, 0, 0},
                new int[]{0, -1, 0},
                new int[]{-1, -1, 0}
        }, 100, 100, map);

        e.getComponents().add(tileMap);
        e.getComponents().add(new Component() {
            float v;

            @Override
            public void onUpdate() {
                getTransform().translate(Axis.HORIZONTAL.getValue(), Axis.VERTICAL.getValue());
                v += Axis.JUMP.getValue() * Time.getDeltaTime() / 10;
                getTransform().rotate(v);
            }
        });


        /*
        e.getComponents().add(new Sprite(new Texture("carlinhos.jpg")));
        e.getComponents().add(new AudioEmitter(new Audio("carlinhos.ogg"), true));

        e.getComponents().add(new AudioListener());

        e.getComponents().add(new Component() {

            @Override
            public void onUpdate() {
                getTransform().translate(Axis.HORIZONTAL.getValue(), Axis.VERTICAL.getValue());
            }
        });
        */

        s.getEntities().add(e);

        Global.setCurrentScene(s);
        s.create();

        l.getThread().start();
    }
}
