package com.imstargg.support.timetracer;

import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExecutionTimeLogger {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeLogger.class);

    public void log(Signature signature, long executionTime, Object result) {
        log.info("{} ({}ms) >> {}", signature, executionTime, result);
    }
}
