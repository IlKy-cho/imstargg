package com.imstargg.batch.job.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutionTimeLoggingItemWriteListener implements ItemWriteListener<Object> {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeLoggingItemWriteListener.class);

    private AtomicInteger writeCount = new AtomicInteger();
    private AtomicLong totalExecutionTime = new AtomicLong();
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public void beforeWrite(Chunk<?> items) {
        startTime.set(System.currentTimeMillis());
    }

    @Override
    public void afterWrite(Chunk<?> items) {
        long executionTime = System.currentTimeMillis() - startTime.get();
        totalExecutionTime.addAndGet(executionTime);
        writeCount.incrementAndGet();

        log.info("Write count: {}, Execution time: {}ms, Average execution time: {}ms",
                writeCount.get(), executionTime, totalExecutionTime.get() / writeCount.get());
    }
}
