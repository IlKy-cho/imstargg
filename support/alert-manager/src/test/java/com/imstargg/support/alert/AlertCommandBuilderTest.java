package com.imstargg.support.alert;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlertCommandBuilderTest {

    @Test
    void warn_타입으로_빌드_성공() {
        // given
        String title = "경고 알림";
        String content = "경고 내용";

        // when
        AlertCommand alertCommand = new AlertCommandBuilder()
                .warn()
                .title(title)
                .content(content)
                .build();

        // then
        assertThat(alertCommand.title()).isEqualTo("⚠️ " + title);
        assertThat(alertCommand.content()).isEqualTo(content);
        assertThat(alertCommand.ex()).isNull();
    }

    @Test
    void error_타입으로_빌드_성공() {
        // given
        String title = "에러 알림";
        Exception ex = new RuntimeException("에러 발생");

        // when
        AlertCommand alertCommand = new AlertCommandBuilder()
                .error()
                .title(title)
                .ex(ex)
                .build();

        // then
        assertThat(alertCommand.title()).isEqualTo("🚨 " + title);
        assertThat(alertCommand.ex()).isEqualTo(ex);
    }

    @Test
    void title이_없으면_예외발생() {
        // given
        AlertCommandBuilder builder = new AlertCommandBuilder()
                .warn();

        // when, then
        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void content과_exception이_없어도_빌드_성공() {
        // given
        String title = "알림";

        // when
        AlertCommand alertCommand = new AlertCommandBuilder()
                .title(title)
                .warn()
                .build();

        // then
        assertThat(alertCommand.content()).isNull();
        assertThat(alertCommand.ex()).isNull();
    }
}