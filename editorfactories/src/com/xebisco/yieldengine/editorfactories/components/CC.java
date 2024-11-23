package com.xebisco.yieldengine.editorfactories.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.camera.OrthoCamera;
import com.xebisco.yieldengine.core.input.Input;
import org.joml.Vector2f;

public class CC extends Component {
    @Override
    public void onUpdate() {
        getTransform().getTransformMatrix().translation(((OrthoCamera) Global.getCurrentScene().getCamera()).getTransform().getTranslation());
        getTransform().translate(Input.getInstance().getMousePosition().mul(new Vector2f(((OrthoCamera) Global.getCurrentScene().getCamera()).getTransform().getScale()), new Vector2f()).mul(((OrthoCamera) Global.getCurrentScene().getCamera()).getViewport()).sub(((OrthoCamera) Global.getCurrentScene().getCamera()).getViewport().div(2, new Vector2f()).mul(new Vector2f(((OrthoCamera) Global.getCurrentScene().getCamera()).getTransform().getScale()))));
    }
}
