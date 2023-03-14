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
import com.xebisco.yield.physics.PhysicsBody;
import com.xebisco.yield.physics.PhysicsType;
import com.xebisco.yield.physics.RectangleCollider;
import com.xebisco.yield.physics.TriangleCollider;

public class Main extends Scene {
    public Main(Application application) {
        super(application);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ContextTime time = new ContextTime();
        time.setTargetSleepTime(16666);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        Application application = new Application(applicationManager, Main.class, Global.swingPlatform(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getApplication().getScene().getSystems().add(new ExitWithEscapeKey());
        instantiate(new Entity2DPrefab(new ComponentCreation(Rectangle.class), new ComponentCreation(RectangleCollider.class), new ComponentCreation(PhysicsBody.class)));
        instantiate(new Entity2DPrefab(new ComponentCreation(EquilateralTriangle.class), new ComponentCreation(TriangleCollider.class), new ComponentCreation(PhysicsBody.class), new ComponentCreation(Comp2.class)));
    }
}

class Comp2 extends ComponentBehavior {
    @Override
    public void onStart() {
        getComponent(PhysicsBody.class).setType(PhysicsType.STATIC);
        getComponent(PhysicsBody.class).setPosition(new TwoAnchorRepresentation(10, -300));
    }

    @Override
    public void onUpdate() {
        getApplication().getScene().getCamera().sumLocal(new Vector2D(getApplication().getAxis("HorizontalCam") * 30, getApplication().getAxis("VerticalCam") * 30));
    }
}