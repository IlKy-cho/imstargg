package com.imstargg.test.java;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LongIncrementUtilTest {

    @Test
    void 증가하는_아이디를_생성한다() {
        long num1 = LongIncrementUtil.next();
        long num2 = LongIncrementUtil.next();
        long num3 = LongIncrementUtil.next();

        assertThat(num2).isEqualTo(num1 + 1);
        assertThat(num3).isEqualTo(num2 + 1);
    }
}