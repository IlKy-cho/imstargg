package com.imstargg.batch.job.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;

public class ChunkErrorLogListener implements ChunkListener {

    private static final Logger log = LoggerFactory.getLogger(ChunkErrorLogListener.class);

    @Override
    public void afterChunkError(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        for (Throwable failureException : stepContext.getStepExecution().getFailureExceptions()) {
            log.warn("Chunk Error 발생. stepName={}", stepContext.getStepName(), failureException);
        }
    }
}
