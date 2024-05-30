/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine.manager;

import com.xebisco.yieldengine.AbstractBehavior;
import com.xebisco.yieldengine.Application;
import com.xebisco.yieldengine.Context;
import com.xebisco.yieldengine.ContextTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code ApplicationManager} class manages the lifecycle of applications.
 * It runs the applications and handles their disposal when necessary.
 */
public class ApplicationManager implements Runnable {

    private List<Application> applications = new ArrayList<>();
    private Context managerContext;
    private boolean disposeWhenEmpty = true;

    /**
     * Constructs an instance of {@link ApplicationManager} with the specified context time.
     *
     * @param contextTime The context time for the manager.
     */
    public ApplicationManager(ContextTime contextTime) {
        managerContext = new Context(contextTime, new ApplicationManagerBehavior(), "Application Manager");
    }

    /**
     * Starts the thread for the manager. It starts all the {@link Application}s in this {@link ApplicationManager}.
     */
    @Override
    public void run() {
        managerContext.thread().start();
    }

    public void runAndWait() {
        managerContext.run();
    }

    /**
     * Handles the behavior of the manager.
     */
    class ApplicationManagerBehavior extends AbstractBehavior {

        @Override
        public void close() throws IOException {
            applications = null;
            managerContext = null;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onUpdate(ContextTime time) {
            for (Application application : applications) {
                application.tick(time);
            }
            boolean removed = applications.removeIf(a -> {
                boolean remove = a.applicationPlatform().graphicsManager().shouldClose();
                if (remove) {
                    try {
                        a.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return remove;
            });
            if (removed && applications.isEmpty()) managerContext.running().set(false);
        }
    }

    /**
     * Returns the list of applications managed by this manager.
     *
     * @return The list of applications.
     */
    public List<Application> applications() {
        return applications;
    }

    /**
     * Sets the list of applications managed by this manager.
     *
     * @param applications The list of applications.
     * @return This instance of {@link ApplicationManager} for method chaining.
     */
    public ApplicationManager setApplications(List<Application> applications) {
        this.applications = applications;
        return this;
    }

    /**
     * Returns the context in which this manager operates.
     *
     * @return The context.
     */
    public Context managerContext() {
        return managerContext;
    }

    /**
     * Sets the context in which this manager operates.
     *
     * @param managerContext The context.
     * @return This instance of {@link ApplicationManager} for method chaining.
     */
    public ApplicationManager setManagerContext(Context managerContext) {
        this.managerContext = managerContext;
        return this;
    }

    /**
     * Returns the flag indicating whether to dispose of the manager when there are no more applications.
     *
     * @return The flag.
     */
    public boolean disposeWhenEmpty() {
        return disposeWhenEmpty;
    }

    /**
     * Sets the flag indicating whether to dispose of the manager when there are no more applications.
     *
     * @param disposeWhenEmpty The flag.
     * @return This instance of {@link ApplicationManager} for method chaining.
     */
    public ApplicationManager setDisposeWhenEmpty(boolean disposeWhenEmpty) {
        this.disposeWhenEmpty = disposeWhenEmpty;
        return this;
    }
}
