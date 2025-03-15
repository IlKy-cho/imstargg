package com.imstargg.test.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public abstract class MultiThreadUtil {

    public static <T> List<T> execute(Callable<T> callable, int threadCount) throws Exception {
        return executeTasks(i -> callable, threadCount);
    }

    public static <T> List<T> execute(IntFunction<T> function, int threadCount) throws Exception {
        return executeTasks(i -> () -> function.apply(i), threadCount);
    }

    private static <T> List<T> executeTasks(IntFunction<Callable<T>> taskSupplier, int threadCount) throws Exception {
        List<Future<T>> futures = new ArrayList<>(threadCount);
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            IntStream.range(0, threadCount)
                    .mapToObj(taskSupplier)
                    .map(executorService::submit)
                    .forEach(futures::add);
        }
        List<T> results = new ArrayList<>(threadCount);
        for (Future<T> future : futures) {
            results.add(future.get());
        }

        return Collections.unmodifiableList(results);
    }

    private MultiThreadUtil() {
        throw new UnsupportedOperationException();
    }
}
