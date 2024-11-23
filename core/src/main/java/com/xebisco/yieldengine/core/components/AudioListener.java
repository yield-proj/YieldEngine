package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Global;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.audio.IAudioPlayer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class AudioListener extends Component {

    private boolean rotateWithCamera;

    public AudioListener(boolean rotateWithCamera) {
        this.rotateWithCamera = rotateWithCamera;
    }

    public AudioListener() {
        this(true);
    }

    @Override
    public void onLateUpdate() {
        IAudioPlayer audioPlayer = IO.getInstance().getAudioPlayer();
        Vector3fc position = getEntity().getNewWorldTransform().getTranslation();
        audioPlayer.setListenerPosition(position);
        Matrix4f cameraMatrix;
        if (rotateWithCamera)
            cameraMatrix = Global.getCurrentScene().getCamera().getViewMatrix();
        else cameraMatrix = new Matrix4f();
        Vector3f at = new Vector3f();
        cameraMatrix.positiveZ(at).negate();
        Vector3f up = new Vector3f();
        cameraMatrix.positiveY(up);
        audioPlayer.setListenerOrientation(at, up);
    }
}
