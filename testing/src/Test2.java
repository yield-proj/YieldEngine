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
import com.xebisco.yieldengine.core.components.Rectangle;
import org.joml.Vector2f;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Test2 {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, InterruptedException {
        LoopContext l = Global.getOpenGLOpenALLoopContext(1280, 720);
        Scene s = new Scene(new ArrayList<>());

        s.getEntityFactories().add((EntityFactory) () -> {
            Entity e = new Entity("", new Transform());
            e.addComponents(
                    new Rectangle(new Vector2f(10, 10))
            );
            e.getTransform().rotateZ((float) Math.toRadians(180));e.getTransform().scale(2, 0);

            return e;
        });

        Global.setCurrentScene(s);
        s.create();

        l.startThread();
    }
}
