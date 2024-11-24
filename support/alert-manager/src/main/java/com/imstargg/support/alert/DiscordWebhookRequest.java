package com.imstargg.support.alert;

record DiscordWebhookRequest(
        String content
) {

    private static final int MAX_CONTENT_LENGTH = 2000;

    public DiscordWebhookRequest {
        if (content.length() > MAX_CONTENT_LENGTH) {
            String postfix = "\n...자세한 내용은 로그를 참고...";
            content = content.substring(0, MAX_CONTENT_LENGTH - postfix.length()) + postfix;
        }
    }

    public static DiscordWebhookRequest from(AlertCommand alertCommand) {
        StringBuilder sb = new StringBuilder();

        sb.append("# ").append(alertCommand.title()).append("\n\n");

        if (alertCommand.content() != null) {
            sb.append("[내용]\n").append(alertCommand.content()).append("\n\n");
        }

        if (alertCommand.ex() != null) {
            sb.append("[예외]\n").append(alertCommand.exStackTrace()).append("\n\n");
        }

        return new DiscordWebhookRequest(sb.toString());
    }
}
