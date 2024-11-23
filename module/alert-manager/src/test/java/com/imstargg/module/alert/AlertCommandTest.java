package com.imstargg.module.alert;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlertCommandTest {

    @Test
    void 예외가_없을_때_exStackTrace는_null을_반환한다() {
        // given
        AlertCommand alertCommand = new AlertCommand("Test Title", "Test Content", null);

        // when
        String result = alertCommand.exStackTrace();

        // then
        assertThat(result).isNull();
    }

    @Test
    void 예외가_있을_때_exStackTrace는_스택트레이스를_반환한다() {
        // given
        Exception exception = new Exception("Test Exception");
        AlertCommand alertCommand = new AlertCommand("Test Title", "Test Content", exception);

        // when
        String result = alertCommand.exStackTrace();

        // then
        assertThat(result).contains("Test Exception");
    }
}