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

import com.xebisco.yield.*;
import com.xebisco.yield.manager.ApplicationManager;

public class Main extends Scene {
    public static final Entity2DPrefab HELLO_WORLD_TEXT_PREFAB = new Entity2DPrefab(new ComponentCreation(TextMesh.class, c -> ((TextMesh) c).setContents("Hello, World!")));


    public Main(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        instantiate(HELLO_WORLD_TEXT_PREFAB);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ApplicationManager manager = new ApplicationManager(new ContextTime());
        new Application(manager, Main.class, Global.Platforms.openGLOpenAL(), new PlatformInit(PlatformInit.PC_DEFAULT));
        manager.run();
    }
}