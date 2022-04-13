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

import com.xebisco.yield.utils.YldAction;

public class YldEngineAction
{
    private final YldAction action;
    private int toExec;
    private final int initialToExec;
    private long id;
    private final boolean repeat;

    public YldEngineAction(YldAction action, int toExec, boolean repeat, long id)
    {
        this.initialToExec = toExec;
        this.toExec = initialToExec;
        this.action = action;
        this.repeat = repeat;
    }

    public YldAction getAction()
    {
        return action;
    }

    public int getToExec()
    {
        return toExec;
    }

    public void setToExec(int toExec)
    {
        this.toExec = toExec;
    }

    public int getInitialToExec()
    {
        return initialToExec;
    }

    public boolean isRepeat()
    {
        return repeat;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
