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

package com.xebisco.yieldengine.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ParallelForLoop implements IAWait {
    private final LockProcess lockProcess;
    private final AtomicInteger processesLeftAtomic = new AtomicInteger();

    private ParallelForLoop(LockProcess lockProcess) {
        this.lockProcess = lockProcess;
    }

    public static ParallelForLoop executorParallelFor(IntegerRange range, Function<Integer, Void> prc) {
        ParallelForLoop parallelForLoop = new ParallelForLoop(new LockProcess());
        parallelForLoop.getProcessesLeftAtomic().set(range.getMax() - range.getMin() + 1);

        ExecutorService executorService = Executors.newFixedThreadPool(range.getMax() - range.getMin() + 1);
        for (int i = range.getMin(); i <= range.getMax(); i += range.getStep()) {
            int finalI = i;
            executorService.submit(() -> {
                prc.apply(finalI);
                if (parallelForLoop.getProcessesLeftAtomic().decrementAndGet() <= 0) {
                    parallelForLoop.getLockProcess().unlock();
                }
            });
        }

        executorService.shutdown();

        return parallelForLoop;
    }

    public static ParallelForLoop streamParallelFor(IntegerRange range, Function<Integer, Void> prc, int timeOutMillis) {
        ParallelForLoop parallelForLoop = new ParallelForLoop(new LockProcess());
        parallelForLoop.getProcessesLeftAtomic().set(range.getMax() - range.getMin() + 1);

        IntStream.range(0, range.getMax() - range.getMin() + 1).parallel().forEach(i -> {
            ASyncFunction.aSync(() -> {
                prc.apply(i);
                if (parallelForLoop.getProcessesLeftAtomic().decrementAndGet() <= 0) {
                    parallelForLoop.getLockProcess().unlock();
                }
                return null;
            }, timeOutMillis);
        });

        return parallelForLoop;
    }

    public static ParallelForLoop parallelFor(IntegerRange range, Function<Integer, Void> prc, int timeOutMillis) {
        ParallelForLoop parallelForLoop = new ParallelForLoop(new LockProcess());
        parallelForLoop.getProcessesLeftAtomic().set(range.getMax() - range.getMin() + 1);


        for (int i = range.getMin(); i <= range.getMax(); i += range.getStep()) {
            int finalI = i;
            ASyncFunction.aSync(() -> {
                prc.apply(finalI);
                if (parallelForLoop.getProcessesLeftAtomic().decrementAndGet() <= 0) {
                    parallelForLoop.getLockProcess().unlock();
                }
                return null;
            }, timeOutMillis);
        }

        return parallelForLoop;
    }

    public static ParallelForLoop streamParallelFor(IntegerRange range, Function<Integer, Void> prc) {
        return streamParallelFor(range, prc, 0);
    }

    public static ParallelForLoop parallelFor(IntegerRange range, Function<Integer, Void> prc) {
        return parallelFor(range, prc, 0);
    }

    @Override
    public void aWait() throws InterruptedException {
        getLockProcess().aWait();
    }

    public int getProcessesLeft() {
        return getProcessesLeftAtomic().get();
    }

    public LockProcess getLockProcess() {
        return lockProcess;
    }

    private AtomicInteger getProcessesLeftAtomic() {
        return processesLeftAtomic;
    }
}
