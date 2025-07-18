package com.imstargg.core.error;

import static com.imstargg.core.error.CoreErrorKind.SERVER_ERROR;
import static com.imstargg.core.error.CoreErrorLevel.ERROR;
import static com.imstargg.core.error.CoreErrorLevel.INFO;
import static com.imstargg.core.error.CoreErrorLevel.WARN;

public enum CoreErrorType {

    DEFAULT_ERROR(SERVER_ERROR, "서버에 문제가 발생했습니다.", ERROR),
    EXTERNAL_ERROR(SERVER_ERROR, "외부 서버에 문제가 발생했습니다.", ERROR),

    PLAYER_NOT_FOUND(CoreErrorKind.NOT_FOUND, "플레이어를 찾을 수 없습니다.", INFO),
    PLAYER_RENEWAL_UNAVAILABLE(CoreErrorKind.FORBIDDEN, "현재 플레이어 갱신이 불가능 합니다. 잠시후에 다시 시도해주세요.", INFO),
    PLAYER_RENEWAL_TOO_MANY(CoreErrorKind.UNAVAILABLE,
            "플레이어 갱신 요청이 많아 처리가 불가능합니다. 잠시후에 다시 시도해주세요.", WARN),
    PLAYER_ALREADY_EXISTS(CoreErrorKind.VALIDATION_FAILED, "플레이어가 이미 존재합니다.", INFO),

    CLUB_NOT_FOUND(CoreErrorKind.NOT_FOUND, "클럽을 찾을 수 없습니다.", INFO),

    BATTLE_EVENT_NOT_FOUND(CoreErrorKind.NOT_FOUND, "존재하지 않는 이벤트 입니다.", INFO),

    BRAWLER_NOT_FOUND(CoreErrorKind.NOT_FOUND, "존재하지 않는 브롤러 입니다.", INFO),

    BRAWLSTARS_IN_MAINTENANCE(CoreErrorKind.UNAVAILABLE, "브롤스타즈 서버가 점검중입니다.", INFO),
    BRAWLSTARS_INVALID_TAG(CoreErrorKind.VALIDATION_FAILED, "잘못된 브롤스타즈 태그입니다.", INFO),
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
