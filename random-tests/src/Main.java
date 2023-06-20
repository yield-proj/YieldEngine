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
        System.setProperty("sun.java2d.opengl", "True");
        ContextTime time = new ContextTime();
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        //platformInit.setStretchViewport(true);
        Application application = new Application(applicationManager, Main.class, Global.swingALPlatform(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getSystems().add(new ToggleFullScreenSystem());
        //setBackGroundColor(Colors.BLACK);
        getApplication().getScene().getSystems().add(new ExitWithEscapeKey());
        Entity2D e = instantiate(new Entity2DPrefab(new ComponentCreation(TextureRectangle.class, c -> {
            ((TextureRectangle) c).getVertexShaders().add(new VertexShader() {
                @Override
                public void run() {
                    position.sumLocal(new Vector2D(Math.cos(getFrames() / 100.) * 100, Math.cos(getFrames() / 100.) * 100));
                }
            });
        }), new ComponentCreation(TextureRectangleLoader.class, c -> {
            ((TextureRectangleLoader) c).setTexturePath("com/xebisco/yield/yieldIcon.png");
        }), new ComponentCreation(CircleCollider.class), new ComponentCreation(PhysicsBody.class), new ComponentCreation(AnimationPlayer.class), new ComponentCreation(A.class)));
        e.getTransform().translate(0, 100);
        e.setIndex(-1);
        instantiate(new Entity2DPrefab(new ComponentCreation(Rectangle.class), new ComponentCreation(RectangleCollider.class), new ComponentCreation(PhysicsBody.class, c -> {
            ((PhysicsBody) c).setType(PhysicsType.STATIC);
        }), new ComponentCreation(AnimationPlayer.class), new ComponentCreation(A.class))).getTransform().translate(0, -200);
        //instantiate(StandardPrefabs.text("Test"));
    }

    public static boolean a;

    @Override
    public void onUpdate() {
        /*if (getApplication().isPressingKey(Input.Key.VK_SPACE)) {
            if (a)
                getApplication().changeScene(Main.class, new BasicChangeTransition.SlideUp(3, true));
            a = false;
        } else a = true;*/
    }
}