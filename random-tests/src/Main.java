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
        time.setTargetFPS(60);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        Application application = new Application(applicationManager, Main.class, Global.openGLPlatform(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getSystems().add(new ToggleFullScreenSystem());
        setBackGroundColor(Colors.RED);
        getApplication().getScene().getSystems().add(new ExitWithEscapeKey());
        Entity2D e = instantiate(new Entity2DPrefab(new ComponentCreation(Oval.class), new ComponentCreation(CircleCollider.class), new ComponentCreation(PhysicsBody.class), new ComponentCreation(AnimationPlayer.class), new ComponentCreation(A.class)));
        e.getTransform().rotate(40);
        e.getTransform().translate(0, 100);
        instantiate(new Entity2DPrefab(new ComponentCreation(Rectangle.class), new ComponentCreation(RectangleCollider.class), new ComponentCreation(PhysicsBody.class, c -> {
            ((PhysicsBody) c).setType(PhysicsType.STATIC);
        }), new ComponentCreation(AnimationPlayer.class), new ComponentCreation(A.class))).getTransform().translate(0, -200);
        //instantiate(StandardPrefabs.texRectangle("yieldIcon.png"));
    }

    public static boolean a;

    @Override
    public void onUpdate() {
        if (getApplication().isPressingKey(Input.Key.VK_SPACE)) {
            if (a)
                getApplication().changeScene(Main.class, new BasicChangeTransition.SlideUp(3, true));
            a = false;
        } else a = true;
    }
}