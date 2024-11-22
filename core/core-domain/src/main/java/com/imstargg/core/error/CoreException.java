package com.imstargg.core.error;

public class CoreException extends RuntimeException {

    private final CoreErrorType errorType;

    public CoreException(String message) {
        super(message);
        this.errorType = CoreErrorType.DEFAULT_ERROR;
    }

    public CoreException(CoreErrorType errorType) {
        super(errorType.toString());
        this.errorType = errorType;
    }

    public CoreException(CoreErrorType errorType, String message) {
        super(errorType + ": " + message);
        this.errorType = errorType;
    }

    public CoreException(CoreErrorType errorType, Throwable cause) {
        super(errorType.toString(), cause);
        this.errorType = errorType;
    }

    public CoreException(CoreErrorType errorType, String message, Throwable cause) {
        super(errorType + ": " + message, cause);
        this.errorType = errorType;
    }

    public CoreErrorType getErrorType() {
        return errorType;
    }
}
