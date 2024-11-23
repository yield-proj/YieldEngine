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

import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.Logger;
import com.xebisco.yieldengine.core.LoopContext;
import com.xebisco.yieldengine.core.Scene;
import com.xebisco.yieldengine.editorfactories.CF;
import com.xebisco.yieldengine.editorfactories.CamControl;
import com.xebisco.yieldengine.editorfactories.TestEF;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Test2 {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException, InterruptedException {
        Logger.setInstance(new Logger(true, true));
        LoopContext l = Global.getOpenGLOpenALLoopContext(1280, 720);
        Scene s = new Scene(new ArrayList<>());

        s.getEntityFactories().add(new TestEF());
        s.getEntityFactories().add(new CamControl());
        s.getEntityFactories().add(new CF());

        Global.setCurrentScene(s);
        s.create();

        l.startThread();
    }
}
