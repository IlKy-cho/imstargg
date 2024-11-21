package com.imstargg.core.api.config;

import com.imstargg.core.domain.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (ex instanceof CoreException) {
            switch (((CoreException) ex).getErrorType().getLevel()) {
                case ERROR -> log.error("{} : {}", CoreException.class.getSimpleName(), ex.getMessage(), ex);
                case WARN -> log.warn("{} : {}", CoreException.class.getSimpleName(), ex.getMessage(), ex);
                case INFO -> log.info("{} : {}", CoreException.class.getSimpleName(), ex.getMessage(), ex);
            }
        } else {
            log.error("{} : {}", Exception.class, ex.getMessage(), ex);
        }
    }


}
