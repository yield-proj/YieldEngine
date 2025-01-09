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

package com.xebisco.yieldengine.utils.concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ASyncFunction<R> implements IAWait {
    private final LockProcess lockProcess;
    private R returnValue;
    private final AtomicBoolean timedOut = new AtomicBoolean();

    private ASyncFunction(LockProcess lockProcess) {
        this.lockProcess = lockProcess;
    }

    public static <R> ASyncFunction<R> aSync(IRunnableWithReturnValue<R> run, int timeOutMillis) {
        ASyncFunction<R> aSyncFunction = new ASyncFunction<>(new LockProcess());

        Timer[] timeOutTimer = new Timer[1];

        Thread[] functionThread = new Thread[1];

        if (timeOutMillis > 0) {
            timeOutTimer[0] = new Timer();
            timeOutTimer[0].schedule(new TimerTask() {
                @Override
                public void run() {
                    if (aSyncFunction.getLockProcess().isRunning()) {
                        aSyncFunction.getTimedOut().set(true);
                        aSyncFunction.setReturnValue(null);

                        aSyncFunction.getLockProcess().unlock();
                        functionThread[0].interrupt();
                        try {
                            throw new TimedOutException();
                        } catch (TimedOutException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, timeOutMillis);
        }

        functionThread[0] = new Thread(() -> {
            aSyncFunction.setReturnValue(run.apply());
            aSyncFunction.getLockProcess().unlock();

            if(timeOutTimer[0] != null) {
                timeOutTimer[0].cancel();
            }

            Thread.currentThread().interrupt();
        }, "ASYNCFUNC");
        functionThread[0].setDaemon(true);
        functionThread[0].start();


        return aSyncFunction;
    }

    public static <R> ASyncFunction<R> aSync(IRunnableWithReturnValue<R> run) {
        return aSync(run, 0);
    }

    public static ASyncFunction<Void> aSync(Runnable run, int timeOutMillis) {
        return aSync(() -> {
            run.run();
            return null;
        }, timeOutMillis);
    }

    public static ASyncFunction<Void> aSync(Runnable run) {
        return aSync(run, 0);
    }

    @Override
    public void aWait() throws InterruptedException, TimedOutException {
        getLockProcess().aWait();
        if(isTimedOut()) throw new TimedOutException();
    }

    public boolean isRunning() {
        return getLockProcess().isRunning();
    }

    public boolean isTimedOut() {
        return getTimedOut().get();
    }

    public R getReturnValue() {
        return returnValue;
    }

    private void setReturnValue(R returnValue) {
        this.returnValue = returnValue;
    }

    private LockProcess getLockProcess() {
        return lockProcess;
    }

    private AtomicBoolean getTimedOut() {
        return timedOut;
    }
}
