package com.imstargg.core.enums;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerStatusTest {

    @Test
    void 업데이트_가능_기간이_지나야_업데이트_가능하다() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime updatedAt = now.minusSeconds(121);
        assertTrue(PlayerStatus.NEW.isNextUpdateCooldownOver(now, updatedAt));
    }

    @Test
    void 업데이트_가능_기간이_지나기_전에는_업데이트_불가능하다() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime updatedAt = now.minusSeconds(119);
        assertFalse(PlayerStatus.NEW.isNextUpdateCooldownOver(now, updatedAt));
    }

}