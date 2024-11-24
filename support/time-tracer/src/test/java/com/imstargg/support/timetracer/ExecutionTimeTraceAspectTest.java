package com.imstargg.support.timetracer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExecutionTimeTraceAspectTest {

    @Mock
    private Clock clock;

    @Mock
    private ExecutionTimeLogger executionTimeLogger;

    @InjectMocks
    private ExecutionTimeTraceAspect executionTimeTraceAspect;

    @Test
    void 실행시간을_로그로_남긴다() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        given(clock.millis()).willReturn(100L, 200L);
        given(joinPoint.proceed()).willReturn("result");

        // when
        Object result = executionTimeTraceAspect.logExecutionTime(joinPoint);

        // then
        assertThat(result).isEqualTo("result");
        then(executionTimeLogger).should().log(joinPoint.getSignature(), 100L, "result");
    }

}