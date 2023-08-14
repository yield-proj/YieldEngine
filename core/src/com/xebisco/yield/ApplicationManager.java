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
        managerContext.getThread().start();
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
                boolean remove = a.applicationPlatform().getGraphicsManager().shouldClose();
                if(remove) a.dispose();
                return remove;
            });
            if(removed && applications.size() == 0) managerContext.getRunning().set(false);
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

    /**
     * The function returns a list of Application objects.
     *
     * @return A List of Application objects is being returned.
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * This function sets the list of applications of this manager.
     *
     * @param applications The parameter "applications" is a List of objects of type "Application". This method sets the
     * value of the instance variable "applications" to the value passed as the parameter.
     */
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    /**
     * The function returns the manager context.
     *
     * @return The method is returning this manager`s context.
     */
    public Context getManagerContext() {
        return managerContext;
    }

    /**
     * This function sets the context for this manager.
     *
     * @param managerContext The managerContext value to set.
     */
    public void setManagerContext(Context managerContext) {
        this.managerContext = managerContext;
    }

    /**
     * The function returns a context runnable object of type ApplicationManagerContextRunnable.
     *
     * @return The method is returning the `ApplicationManagerContextRunnable` of this manager.
     */
    public ApplicationManagerContextRunnable getContextRunnable() {
        return contextRunnable;
    }

    /**
     * The function returns an object of type ApplicationManagerContextDisposable.
     *
     * @return The method is returning the `ApplicationManagerContextDisposable` of this manager.
     */
    public ApplicationManagerContextDisposable getContextDisposable() {
        return contextDisposable;
    }

    /**
     * This function returns a boolean value indicating whether an object should be disposed when it is empty.
     *
     * @return The method is returning a boolean value, which is the value of the variable `disposeWhenEmpty`.
     */
    public boolean isDisposeWhenEmpty() {
        return disposeWhenEmpty;
    }

    /**
     * This function sets a boolean value for whether an object should be disposed when it is empty.
     *
     * @param disposeWhenEmpty disposeWhenEmpty is a boolean parameter that determines whether this manager should be disposed
     * of when it becomes empty. If set to true, the object will be disposed of when it becomes empty. If set to false, the
     * object will not be disposed of when it becomes empty.
     */
    public void setDisposeWhenEmpty(boolean disposeWhenEmpty) {
        this.disposeWhenEmpty = disposeWhenEmpty;
    }
}
