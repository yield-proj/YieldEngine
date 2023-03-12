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
        time.setTargetSleepTime(16666);
        ApplicationManager applicationManager = new ApplicationManager(time);
        PlatformInit platformInit = new PlatformInit();
        platformInit.setFullscreen(true);
        platformInit.setResolution(new Size2D(1920, 1080));
        Application application = new Application(applicationManager, Main.class, Global.swingPlatform(), platformInit);
        applicationManager.getApplications().add(application);
        applicationManager.run();
    }

    @Override
    public void onStart() {
        getApplication().getScene().getSystems().add(new ExitWithEscapeKey());
        instantiate(new Entity2DPrefab(new TextureRectangle(), new PhysicsBody(), new Comp(), new RectangleCollider())).setContactAdapter(new ContactAdapter() {
            Entity2D e = instantiate(new Entity2DPrefab(new Text()));

            @Override
            public void onContactBegin(Collider entity, Collider colliding) {
                e.getComponent(Text.class).setContents("COSTÔ");
            }

            @Override
            public void onContactEnd(Collider entity, Collider colliding) {
                e.getComponent(Text.class).setContents("DESENCOSTÔ");
            }
        });
        instantiate(new Entity2DPrefab(new Rectangle(), new PhysicsBody(), new Comp2(), new RectangleCollider()));
    }
}

class Comp extends ComponentBehavior {
    @Override
    public void onStart() {
        getComponent(TextureRectangle.class).setPixelProcessor(pixel -> {
            pixel.setAsOriginal();
            pixel.setX(pixel.getX() + 100);
            return pixel;
        });
    }

    @Override
    public void onUpdate() {
        getComponent(PhysicsBody.class).setLinearVelocity(new Vector2D(getApplication().getAxis("Horizontal") * 300 * getTime().getDeltaTime(), getComponent(PhysicsBody.class).getLinearVelocity().y));
        if (getApplication().getAxis("Fire") > 0)
            getComponent(PhysicsBody.class).addForce(new Vector2D(0, 50), ForceType.LINEAR_IMPULSE);
        getApplication().getScene().getCamera().sumLocal(new Vector2D(getApplication().getAxis("HorizontalCam") * 30, getApplication().getAxis("VerticalCam") * 30));
        if (getApplication().getAxis("RightFire") != 0)
            getComponent(PhysicsBody.class).addForce(-getApplication().getAxis("RightFire") * 100, ForceType.ANGULAR_IMPULSE);
        else
            getComponent(PhysicsBody.class).addForce(getApplication().getAxis("LeftFire") * 100, ForceType.ANGULAR_IMPULSE);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {

    }
}

class Comp2 extends ComponentBehavior {
    @Override
    public void onStart() {
        getComponent(PhysicsBody.class).setPosition(new Vector2D(0, -720 / 2f));
        getComponent(PhysicsBody.class).setType(PhysicsType.STATIC);
        getComponent(RectangleCollider.class).setFriction(0f);
    }

    @Override
    public void onUpdate() {

        ///System.out.println(getTransform().getPosition());
    }
}