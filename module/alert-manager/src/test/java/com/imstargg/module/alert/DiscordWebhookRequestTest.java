package com.imstargg.module.alert;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscordWebhookRequestTest {

    @Test
    void content가_최대_길이를_초과할_때_자동으로_잘리고_후미에_메시지를_추가한다() {
        // given
        String longContent = "a".repeat(2500);

        // when
        DiscordWebhookRequest request = new DiscordWebhookRequest(longContent);

        // then
        String postfix = "\n...자세한 내용은 로그를 참고...";
        assertThat(request.content())
                .hasSize(2000)
                .endsWith(postfix);
    }

    @Test
    void AlertCommand를_기반으로_DiscordWebhookRequest를_생성할_수_있다() {
        // given
        AlertCommand alertCommand = new AlertCommand(
                "테스트 제목",
                "테스트 내용",
                new RuntimeException("테스트 예외")
        );

        // when
        DiscordWebhookRequest request = DiscordWebhookRequest.from(alertCommand);

        // then
        assertThat(request.content())
                .contains("# 테스트 제목")
                .contains("[내용]\n테스트 내용")
                .contains("[예외]\njava.lang.RuntimeException: 테스트 예외");
    }

    @Test
    void AlertCommand에_내용이_없으면_생성된_콘텐츠에_내용_섹션이_없다() {
        // given
        AlertCommand alertCommand = new AlertCommand(
                "테스트 제목",
                null,
                new RuntimeException("테스트 예외")
        );

        // when
        DiscordWebhookRequest request = DiscordWebhookRequest.from(alertCommand);

        // then
        assertThat(request.content())
                .contains("# 테스트 제목")
                .doesNotContain("[내용]");
    }

    @Test
    void AlertCommand에_예외가_없으면_생성된_콘텐츠에_예외_섹션이_없다() {
        // given
        AlertCommand alertCommand = new AlertCommand(
                "테스트 제목",
                "테스트 내용",
                null
        );

        // when
        DiscordWebhookRequest request = DiscordWebhookRequest.from(alertCommand);

        // then
        assertThat(request.content())
                .contains("# 테스트 제목")
                .contains("[내용]\n테스트 내용")
                .doesNotContain("[예외]");
    }
}