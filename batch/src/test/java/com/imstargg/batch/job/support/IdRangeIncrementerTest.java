package com.imstargg.batch.job.support;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdRangeIncrementerTest {

    @Test
    void 파라미터가_null이면_1부터_시작한다() {
        // given
        IdRangeIncrementer incrementer = new IdRangeIncrementer(10, () -> 100);

        // when
        JobParameters next = incrementer.getNext(null);

        // then
        assertThat(next.getLong(IdRangeJobParameter.ID_FROM_KEY)).isEqualTo(1L);
        assertThat(next.getLong(IdRangeJobParameter.ID_TO_KEY)).isEqualTo(10L);
    }

    @Test
    @DisplayName("이전 ID_TO 값을 기준으로 다음 범위를 계산한다")
    void 정상적인_범위_증가() {
        // given
        IdRangeIncrementer incrementer = new IdRangeIncrementer(10, () -> 100);
        JobParameters parameters = new JobParametersBuilder()
                .addLong(IdRangeJobParameter.ID_TO_KEY, 10L)
                .toJobParameters();

        // when
        JobParameters next = incrementer.getNext(parameters);

        // then
        assertThat(next.getLong(IdRangeJobParameter.ID_FROM_KEY)).isEqualTo(11L);
        assertThat(next.getLong(IdRangeJobParameter.ID_TO_KEY)).isEqualTo(20L);
    }

    @Test
    @DisplayName("최대 ID에 도달하면 남은 범위만 처리한다")
    void 최대_ID에_도달하면_제한된다() {
        // given
        IdRangeIncrementer incrementer = new IdRangeIncrementer(10, () -> 25);
        JobParameters parameters = new JobParametersBuilder()
                .addLong(IdRangeJobParameter.ID_TO_KEY, 20L)
                .toJobParameters();

        // when
        JobParameters next = incrementer.getNext(parameters);

        // then
        assertThat(next.getLong(IdRangeJobParameter.ID_FROM_KEY)).isEqualTo(21L);
        assertThat(next.getLong(IdRangeJobParameter.ID_TO_KEY)).isEqualTo(25L);
    }

    @Test
    void 잘못된_파라미터_입력시_예외발생() {
        // given
        IdRangeIncrementer incrementer = new IdRangeIncrementer(10, () -> 100);
        JobParameters parameters = new JobParametersBuilder()
                .addString(IdRangeJobParameter.ID_TO_KEY, "invalid")
                .toJobParameters();

        // when & then
        assertThatThrownBy(() -> incrementer.getNext(parameters))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid value for parameter");
    }
}