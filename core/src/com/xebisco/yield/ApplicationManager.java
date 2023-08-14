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

package com.xebisco.yield;

import java.util.ArrayList;
import java.util.List;

/**
 * It's a class that manages multiple applications.
 */
public class ApplicationManager implements Runnable {

    private List<Application> applications = new ArrayList<>();
    private Context managerContext;
    private boolean disposeWhenEmpty = true;
    private final ApplicationManagerContextRunnable contextRunnable = new ApplicationManagerContextRunnable();
    private final ApplicationManagerContextDisposable contextDisposable = new ApplicationManagerContextDisposable();

    public ApplicationManager(ContextTime contextTime) {
        managerContext = new Context(contextTime, contextRunnable, contextDisposable, "Application Manager");
    }

    @Override
    public void run() {
        managerContext.thread().start();
    }

    /**
     * It runs the applications
     */
    class ApplicationManagerContextRunnable implements Runnable {
        @Override
        public void run() {
            for(Application application : applications) {
                application.setFrames(application.frames() + 1);
                if(application.frames() == 1) {
                    application.onStart();
                }
                application.onUpdate();
            }
            boolean removed = applications.removeIf(a -> {
                boolean remove = a.applicationPlatform().graphicsManager().shouldClose();
                if(remove) a.dispose();
                return remove;
            });
            if(removed && applications.size() == 0) managerContext.running().set(false);
        }
    }
    /**
     * It disposes of all the applications in the manager context when the manager context is disposed
     */
    class ApplicationManagerContextDisposable implements Disposable {
        @Override
        public void dispose() {
            applications = null;
            managerContext = null;
        }
    }

    public List<Application> applications() {
        return applications;
    }

    public ApplicationManager setApplications(List<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Context managerContext() {
        return managerContext;
    }

    public ApplicationManager setManagerContext(Context managerContext) {
        this.managerContext = managerContext;
        return this;
    }

    public boolean disposeWhenEmpty() {
        return disposeWhenEmpty;
    }

    public ApplicationManager setDisposeWhenEmpty(boolean disposeWhenEmpty) {
        this.disposeWhenEmpty = disposeWhenEmpty;
        return this;
    }

    public ApplicationManagerContextRunnable contextRunnable() {
        return contextRunnable;
    }

    public ApplicationManagerContextDisposable contextDisposable() {
        return contextDisposable;
    }
}
