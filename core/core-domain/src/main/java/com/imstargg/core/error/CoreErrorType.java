package com.imstargg.core.error;

import static com.imstargg.core.error.CoreErrorKind.SERVER_ERROR;
import static com.imstargg.core.error.CoreErrorLevel.ERROR;

public enum CoreErrorType {

    DEFAULT_ERROR(SERVER_ERROR, "서버에 문제가 발생했습니다.", ERROR),
    ;

    private final CoreErrorKind kind;
    private final String message;
    private final CoreErrorLevel level;

    CoreErrorType(CoreErrorKind kind, String message, CoreErrorLevel level) {
        this.kind = kind;
        this.message = message;
        this.level = level;
    }

    public CoreErrorKind getKind() {
        return kind;
    }

    public String getMessage() {
        return message;
    }

    public CoreErrorLevel getLevel() {
        return level;
    }
}
