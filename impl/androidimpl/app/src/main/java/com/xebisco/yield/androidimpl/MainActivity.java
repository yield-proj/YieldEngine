package com.xebisco.yield.androidimpl;

import android.app.Activity;
import android.os.Bundle;
import com.xebisco.yield.Application;
import com.xebisco.yield.ApplicationManager;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.PlatformInit;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextTime time = new ContextTime();
        ApplicationManager manager = new ApplicationManager(time);
        PlatformInit init = new PlatformInit();
        MainView mainView = new MainView(this);
        new Application(manager, TestScene.class, mainView, init);
        setContentView(mainView);
        CompletableFuture.runAsync(manager::run);
    }


}