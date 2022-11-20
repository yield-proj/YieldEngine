/*
 * Copyright [2022] [Xebisco]
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

package com.xebisco.yield.engine;

import com.xebisco.yield.YldAction;

/**
 * It's a wrapper for a YldAction that contains information about whether it should
 * be repeated, and an id
 */
public class YldEngineAction {
    private final YldAction action;
    private int toExec;
    private final int initialToExec;
    private long id;
    private boolean repeat;

    public YldEngineAction(YldAction action, int toExec, boolean repeat, long id) {
        this.initialToExec = toExec;
        this.toExec = initialToExec;
        this.action = action;
        this.repeat = repeat;
    }

    /**
     * Returns the action that can be performed on the object.
     *
     * @return The action of the YldEngineAction object.
     */
    public YldAction getAction() {
        return action;
    }

    /**
     * This function returns the number of frames until be executed.
     *
     * @return The value of the variable toExec.
     */
    public int getToExec() {
        return toExec;
    }

    /**
     * This function sets the value of the variable toExec to the value of the parameter toExec.
     *
     * @param toExec The number of frames until the YldAction be executed.
     */
    public void setToExec(int toExec) {
        this.toExec = toExec;
    }

    /**
     * This function returns the initialToExec variable
     *
     * @return The initialToExec variable is being returned.
     */
    public int getInitialToExec() {
        return initialToExec;
    }

    /**
     * This function returns the value of the repeat variable
     *
     * @return The boolean value of the variable repeat.
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * This function sets the repeat variable to the value of the parameter.
     *
     * @param repeat If true, the YldAction will repeat.
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * This function returns the id of the object
     *
     * @return The id of the object.
     */
    public long getId() {
        return id;
    }

    /**
     * This function sets the id of the object to the value of the id parameter.
     *
     * @param id The id of the YldAction.
     */
    public void setId(long id) {
        this.id = id;
    }
}
