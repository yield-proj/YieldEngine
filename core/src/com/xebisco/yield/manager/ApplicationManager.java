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

package com.xebisco.yield.manager;

import com.xebisco.yield.AbstractBehavior;
import com.xebisco.yield.Application;
import com.xebisco.yield.Context;
import com.xebisco.yield.ContextTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * It's a class that manages multiple applications.
 */
public class ApplicationManager implements Runnable {

    private List<Application> applications = new ArrayList<>();
    private Context managerContext;
    private boolean disposeWhenEmpty = true;

    public ApplicationManager(ContextTime contextTime) {
        managerContext = new Context(contextTime, new ApplicationManagerBehavior(), "Application Manager");
    }

    @Override
    public void run() {
        managerContext.thread().start();
    }

    /**
     * It runs the applications
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
}
