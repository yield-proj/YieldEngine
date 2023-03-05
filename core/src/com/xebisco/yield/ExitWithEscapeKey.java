package com.xebisco.yield;

public class ExitWithEscapeKey extends SystemBehavior {
    @Override
    public void onUpdate() {
        if(getScene().getApplication().getInputManager().getPressingKeys().contains(Input.Key.VK_ESCAPE))
            getScene().getApplication().getApplicationManager().getManagerContext().getRunning().set(false);
    }
}
