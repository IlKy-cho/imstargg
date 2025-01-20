package com.imstargg.core.domain.utils;

import com.imstargg.core.error.CoreException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class FutureUtils {

    public static <T> List<T> get(List<Future<T>> futures) {
        return futures.stream()
                .map(FutureUtils::get)
                .toList();
    }

    public static<T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new CoreException("Failed to get future results", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CoreException("Failed to get future results", e);
        }
    }

    private FutureUtils() {
        throw new UnsupportedOperationException();
    }
}
