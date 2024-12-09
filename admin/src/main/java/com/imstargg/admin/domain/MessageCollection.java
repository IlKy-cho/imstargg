package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.core.enums.Language;

import java.util.Arrays;
import java.util.EnumMap;

public record MessageCollection(
        EnumMap<Language, String> messages
) {

    public void validate() {
        if (messages.size() != Language.values().length) {
            throw new AdminException(AdminErrorKind.VALIDATION_FAILED,
                    "메시지는 모든 언어를 지원해야 합니다. 제공된 언어: " + messages.keySet() +
                            ", 필요한 언어: " + Arrays.toString(Language.values()));
        }
    }
}
