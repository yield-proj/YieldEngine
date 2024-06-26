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

package com.xebisco.yield.concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ASyncFunction<R> {
    private final LockProcess lockProcess;
    private R returnValue;
    private final AtomicBoolean timedOut = new AtomicBoolean();

    private ASyncFunction(LockProcess lockProcess) {
        this.lockProcess = lockProcess;
    }

    public static <R> ASyncFunction<R> aSync(IRunnableWithReturnValue<R> run, int timeOutMillis) {
        ASyncFunction<R> aSyncFunction = new ASyncFunction<>(new LockProcess());

        Thread functionThread = new Thread(() -> {
            aSyncFunction.setReturnValue(run.apply());
            aSyncFunction.getLockProcess().unlock();

            Thread.currentThread().interrupt();
        }, "ASYNCFUNC");
        functionThread.setDaemon(true);
        functionThread.start();

        Timer timeOutTimer = new Timer();
        timeOutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (aSyncFunction.getLockProcess().isRunning()) {
                    aSyncFunction.getTimedOut().set(true);
                    aSyncFunction.setReturnValue(null);

                    aSyncFunction.getLockProcess().unlock();
                    functionThread.interrupt();
                }
            }
        }, timeOutMillis);
        return aSyncFunction;
    }

    public static <R> ASyncFunction<R> aSync(IRunnableWithReturnValue<R> run) {
        return aSync(run, 5000);
    }

    public static ASyncFunction<Void> aSync(Runnable run, int timeOutMillis) {
        return aSync(() -> {
            run.run();
            return null;
        }, timeOutMillis);
    }

    public static ASyncFunction<Void> aSync(Runnable run) {
        return aSync(run, 5000);
    }

    public R aWait() throws InterruptedException, TimeOutException {
        getLockProcess().aWait();
        if (isTimedOut()) {
            throw new TimeOutException();
        }
        return getReturnValue();
    }

    private boolean isRunning() {
        return getLockProcess().isRunning();
    }

    private boolean isTimedOut() {
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
