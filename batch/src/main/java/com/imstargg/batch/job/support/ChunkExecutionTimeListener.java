package com.imstargg.batch.job.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public class ChunkExecutionTimeListener implements ChunkListener {

    private static final Logger log = LoggerFactory.getLogger(ChunkExecutionTimeListener.class);

    private final ThreadLocal<LocalDateTime> startTimeHolder = new ThreadLocal<>();
    private final Clock clock;
    private final long warningItemAvgExecutionTimeMillis;

    public ChunkExecutionTimeListener(Clock clock, long warningItemAvgExecutionTimeMillis) {
        this.clock = clock;
        this.warningItemAvgExecutionTimeMillis = warningItemAvgExecutionTimeMillis;
    }

    public ChunkExecutionTimeListener(Clock clock) {
        this(clock, 500);
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        startTimeHolder.set(LocalDateTime.now(clock));
    }

    @Override
    public void afterChunk(ChunkContext context) {
        LocalDateTime endTime = LocalDateTime.now(clock);
        LocalDateTime startTime = startTimeHolder.get();


        Duration executionTime = Duration.between(startTime, endTime);
        long readCount = context.getStepContext().getStepExecution().getReadCount();
        long executionTimeMillis = executionTime.toMillis();
        long itemAvgExecutionTimeMillis = executionTimeMillis / readCount;

        log.debug("Chunk execution time: {}s. Item average execution time: {}s",
                executionTimeMillis / 1000.0, itemAvgExecutionTimeMillis / 1000.0);
        if (itemAvgExecutionTimeMillis > this.warningItemAvgExecutionTimeMillis) {
            log.warn("청크 처리 시간이 기준 시간을 초과했습니다. 청크 전체 처리 시간: {}ms, 아이템 처리 시간: {}ms, 아이템 기준 시간: {}ms",
                    executionTimeMillis, itemAvgExecutionTimeMillis, this.warningItemAvgExecutionTimeMillis);
        }

        startTimeHolder.remove();
    }
}
