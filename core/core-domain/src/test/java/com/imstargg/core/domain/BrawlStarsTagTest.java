package com.imstargg.core.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class BrawlStarsTagTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "#123456",
            "#123456789ABCDEFG",
    })
    void 태그는_6자이상_16이하로_구성된다(String tag) {
        BrawlStarsTag brawlStarsTag = new BrawlStarsTag(tag);
        assertThat(brawlStarsTag.isValid()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "#12345",
            "#123456789ABCDEFGH",
    })
    void 태그가__6자미만_16자초과인경우_예외(String tag) {
        BrawlStarsTag brawlStarsTag = new BrawlStarsTag(tag);
        assertThat(brawlStarsTag.isValid()).isFalse();
    }
}