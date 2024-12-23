package com.imstargg.core.error;

import static com.imstargg.core.error.CoreErrorKind.SERVER_ERROR;
import static com.imstargg.core.error.CoreErrorLevel.ERROR;
import static com.imstargg.core.error.CoreErrorLevel.INFO;
import static com.imstargg.core.error.CoreErrorLevel.WARN;

public enum CoreErrorType {

    DEFAULT_ERROR(SERVER_ERROR, "서버에 문제가 발생했습니다.", ERROR),

    PLAYER_NOT_FOUND(CoreErrorKind.NOT_FOUND, "플레이어를 찾을 수 없습니다.", INFO),
    PLAYER_ALREADY_RENEWED(CoreErrorKind.FORBIDDEN, "이미 갱신된 플레이어입니다.", INFO),
    PLAYER_RENEW_UNAVAILABLE(CoreErrorKind.UNAVAILABLE,
            "플레이어 갱신 요청이 많아 처리가 불가능합니다. 잠시후에 다시 시도해주세요.", WARN)
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
