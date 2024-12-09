package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.core.enums.Language;

import java.util.Arrays;
import java.util.EnumMap;

public record NewBrawler(
        long brawlStarsId,
        BrawlerRarity rarity,
        BrawlerRole role,
        EnumMap<Language, String> names
) {

    public void validate() {
        if (names.size() != Language.values().length) {
            throw new AdminException(AdminErrorKind.VALIDATION_FAILED,
                    "브롤러 이름은 모든 언어를 지원해야 합니다. 제공된 언어: " + names.keySet() +
                            ", 필요한 언어: " + Arrays.toString(Language.values()));
        }
    }
}
