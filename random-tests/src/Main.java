/*
 * Copyright [2022-2023] [Xebisco]
 *
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
import com.xebisco.yield.physics.*;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        Application application = new Application(applicationManager, Main.class, Global.openGLALPlatform(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }
    private Entity2D e;
    public double s;

    @Override
    public void onStart() {
        getSystems().add(new ToggleFullScreenSystem());
        instantiate(INPUT_UI_PREFAB);
        e = instantiate(StandardPrefabs.text("Hello, World!", Colors.WHITE, new Font("com/xebisco/yield/Pixeboy.ttf", 100, getApplication().getApplicationPlatform().getFontLoader())));
    }

    @Override
    public void onUpdate() {
        s += getApplication().getAxis(HORIZONTAL) * getTime().getDeltaTime();
        e.getTransform().translate(0, getApplication().getAxis(VERTICAL));
        e.getTransform().rotate(s);
    }
}