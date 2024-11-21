package com.imstargg.module.timetracer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Aspect
@Component
@Order
public class ExecutionTimeTraceAspect {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeTraceAspect.class);

    private final Clock clock;
    private final ExecutionTimeLogger executionTimeLogger;

    public ExecutionTimeTraceAspect(Clock clock, ExecutionTimeLogger executionTimeLogger) {
        this.clock = clock;
        this.executionTimeLogger = executionTimeLogger;
    }

    @Around("@annotation(com.imstargg.module.timetracer.TimeTrace)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = clock.millis();
        Object result = joinPoint.proceed();

        long executionTime = clock.millis() - start;
        executionTimeLogger.log(joinPoint.getSignature(), executionTime, result);

        return result;
    }
}
