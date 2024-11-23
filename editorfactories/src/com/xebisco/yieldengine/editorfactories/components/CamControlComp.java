package com.xebisco.yieldengine.editorfactories.components;

import com.xebisco.yieldengine.annotations.Visible;
import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.Time;
import com.xebisco.yieldengine.core.camera.OrthoCamera;
import com.xebisco.yieldengine.core.input.Axis;
import com.xebisco.yieldengine.core.input.Input;
import com.xebisco.yieldengine.core.input.Key;
import com.xebisco.yieldengine.core.input.MouseButton;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;

public class CamControlComp extends Component {
    @Visible
    private float cameraSpeed = 400f, zoomIntensity = 5f;

    Axis back = new Axis(Key.VK_BACK_SPACE);
    private Vector2fc lastMouse = Input.getInstance().getMousePosition();

    @Override
    public void onUpdate() {
        OrthoCamera cam = ((OrthoCamera) Global.getCurrentScene().getCamera());
        Vector2f mouseDelta = new Vector2f();
        Input.getInstance().getMousePosition().sub(lastMouse, mouseDelta).mul(-cam.getViewport().x() * cam.getTransform().getScale().x() * 1.22f, -cam.getViewport().y() * cam.getTransform().getScale().y() * 1.22f);
        lastMouse = Input.getInstance().getMousePosition();
        if(lastMouse.x() != -1 && lastMouse.y() != -1 && Input.getInstance().isMouseButtonPressed(MouseButton.BUTTON_2)) {
            cam.getTransform().translate(mouseDelta);
        }

        cam.getTransform().translate(
                        new Vector2f(-Axis.HORIZONTAL.getValue(), -Axis.VERTICAL.getValue())
                                .mul(cameraSpeed * Time.getDeltaTime() * cam.getTransform().getScale().x()))
                .scale(new Vector3f(-Input.getInstance().getScrollWheel() * Time.getDeltaTime() * zoomIntensity, -Input.getInstance().getScrollWheel() * Time.getDeltaTime() * zoomIntensity, 0));
        if(Input.getInstance().isKeyPressed(Key.VK_BACK_SPACE)) cam.getTransform().scale(new Vector3f(back.getValue() * Time.getDeltaTime(), back.getValue() * Time.getDeltaTime(), 0));

    }

    @Override
    public void onLateUpdate() {
        }
}
