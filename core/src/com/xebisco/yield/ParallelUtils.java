package com.xebisco.yield;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ParallelUtils {
    /**
     * The function performs a parallel iteration over an array and executes a process on each element, using
     * synchronization to ensure completion.
     *
     * @param array         an array of type T that contains the elements to be processed
     * @param objectProcess objectProcess is an interface that defines a method called "process" which takes a single
     *                      parameter of type T (the generic type used in the method signature). This method is called for each element in the
     *                      input array, allowing the user to perform some operation on each element.
     */
    public static <T> void parallelForEach(T[] array, IObjectProcess<T> objectProcess) {
        final AtomicBoolean finished = new AtomicBoolean(false), locked = new AtomicBoolean(false);
        final AtomicInteger processed = new AtomicInteger(0);
        final Object lockObject = new Object();

        IntStream.range(0, array.length).forEach(i -> {
            objectProcess.process(array[i]);
            if (processed.incrementAndGet() == array.length) {
                finished.set(true);
                if (locked.get())
                    lockObject.notify();
            }
        });

        if (!finished.get()) {
            locked.set(true);
            synchronized (lockObject) {
                try {
                    lockObject.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void callASync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }
}
