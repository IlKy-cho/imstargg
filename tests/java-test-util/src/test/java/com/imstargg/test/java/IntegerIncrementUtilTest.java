package com.imstargg.test.java;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerIncrementUtilTest {

    @Test
    void next_메서드는_증가된_값을_반환한다() {
        int firstValue = IntegerIncrementUtil.next();
        int secondValue = IntegerIncrementUtil.next();

        assertThat(secondValue).isEqualTo(firstValue + 1);
    }

    @Test
    void next_메서드는_경계를_초과하지_않는다() {
        int bound = 10;
        for (int i = 0; i < 100; i++) {
            int value = IntegerIncrementUtil.next(bound);
            assertThat(value).isLessThan(bound);
        }
    }

    @Test
    void next_메서드는_원점과_경계_사이의_값을_반환한다() {
        int origin = 5;
        int bound = 15;
        for (int i = 0; i < 100; i++) {
            int value = IntegerIncrementUtil.next(origin, bound);
            assertThat(value).isBetween(origin, bound - 1);
        }
    }
}