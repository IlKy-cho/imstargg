package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.core.enums.Language;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record NewMessageCollection(
        Map<Language, String> messages
) {

    private static final EnumMap<Language, Pattern> VALIDATION_PATTERNS = new EnumMap<>(Map.of(
            Language.ENGLISH, Pattern.compile("[A-Za-z0-9\\-'&\\s.!(),?%]+"),
            Language.KOREAN, Pattern.compile("[가-힣0-9\\-'&\\s.!(),?%]+")
    ));

    static {
        if (VALIDATION_PATTERNS.size() != Language.values().length) {
            throw new AdminException(AdminErrorKind.SERVER_ERROR,
                    "메시지 유효성 검사 패턴이 모든 언어를 지원해야 합니다. 제공된 언어: " + VALIDATION_PATTERNS.keySet() +
                            ", 필요한 언어: " + Arrays.toString(Language.values()));
        }
    }

    public NewMessageCollection {
        messages = messages.entrySet().stream()
                .filter(entry -> !entry.getValue().isBlank())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().trim()));
    }

    public void validate() {
        if (!messages.containsKey(Language.ENGLISH)) {
            throw new AdminException(AdminErrorKind.VALIDATION_FAILED,
                    "메시지는 영어를 제공해야 합니다. 제공된 언어: " + messages.keySet());
        }
        messages.forEach((language, message) -> {
            if (!VALIDATION_PATTERNS.get(language).matcher(message).matches()) {
                throw new AdminException(AdminErrorKind.VALIDATION_FAILED,
                        "메시지가 유효하지 않습니다. 언어: " + language + ", 메시지: " + message);
            }
        });
    }
}
