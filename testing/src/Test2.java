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
import com.xebisco.yieldengine.core.components.Text;

import java.lang.reflect.InvocationTargetException;

public class Test2 {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Logger.setInstance(new Logger(true, true));
        LoopContext l = Global.getOpenGLOpenALLoopContext(1280, 720);
        Scene s = new Scene();
        Entity e = new Entity("hw", new Transform());
        e.getComponents().add(new Text("Hello, World!"));
        s.getEntities().add(e);
        s.create();
        Global.setCurrentScene(s);
        l.getThread().start();
    }
}
