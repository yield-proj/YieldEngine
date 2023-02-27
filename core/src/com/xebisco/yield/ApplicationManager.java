/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

import java.util.ArrayList;
import java.util.List;

public class ApplicationManager implements Runnable {

    private List<Application> applications = new ArrayList<>();
    private Context managerContext;
    private boolean disposeWhenEmpty = true;
    private final ApplicationManagerContextRunnable contextRunnable = new ApplicationManagerContextRunnable();
    private final ApplicationManagerContextDisposable contextDisposable = new ApplicationManagerContextDisposable();

    public ApplicationManager(ContextTime contextTime) {
        managerContext = new Context(contextTime, contextRunnable, contextDisposable);
    }

    @Override
    public void run() {
        managerContext.getThread().start();
    }

    class ApplicationManagerContextRunnable implements Runnable {
        @Override
        public void run() {
            for(Application application : applications) {
                application.setFrames(application.getFrames() + 1);
                if(application.getFrames() == 1) {
                    application.onStart();
                }
                application.onUpdate();
            }
            boolean removed = applications.removeIf(a -> a.getPlatformGraphics().shouldClose());
            if(removed && applications.size() == 0) managerContext.getRunning().set(false);
        }
    }
    class ApplicationManagerContextDisposable implements Disposable {
        @Override
        public void dispose() {
            for(Application application : applications) {
                application.dispose();
            }
            applications.clear();
            applications = null;
            managerContext = null;
        }
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public Context getManagerContext() {
        return managerContext;
    }

    public void setManagerContext(Context managerContext) {
        this.managerContext = managerContext;
    }

    public ApplicationManagerContextRunnable getContextRunnable() {
        return contextRunnable;
    }

    public ApplicationManagerContextDisposable getContextDisposable() {
        return contextDisposable;
    }

    public boolean isDisposeWhenEmpty() {
        return disposeWhenEmpty;
    }

    public void setDisposeWhenEmpty(boolean disposeWhenEmpty) {
        this.disposeWhenEmpty = disposeWhenEmpty;
    }
}
