package com.imstargg.storage.db.core;

import java.util.regex.Pattern;

public abstract class MessageCodes {

    public static final MessageCodeTemplate<String> BATTLE_MAP_NAME = new MessageCodeTemplate<>("maps.{mapBrawlStarsName}.name");
    public static final MessageCodeTemplate<Long> BRAWLER_NAME = new MessageCodeTemplate<>("brawler.{brawlStarsId}.name");
    public static final MessageCodeTemplate<Long> GEAR_NAME = new MessageCodeTemplate<>("gear.{brawlStarsId}.name");
    public static final MessageCodeTemplate<Long> STAR_POWER_NAME = new MessageCodeTemplate<>("starpower.{brawlStarsId}.name");
    public static final MessageCodeTemplate<Long> GADGET_NAME = new MessageCodeTemplate<>("gadget.{brawlStarsId}.name");

    private MessageCodes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class MessageCodeTemplate<T> {

        private final String template;
        private final String keyPlaceholder;

        private MessageCodeTemplate(String template) {
            this.template = template;
            this.keyPlaceholder = extractCodeKeyPlaceholder(template);
        }

        private String extractCodeKeyPlaceholder(String codeTemplate) {
            Pattern pattern = Pattern.compile("\\{([^}]+)}");
            var matcher = pattern.matcher(codeTemplate);

            if (!matcher.find()) {
                throw new IllegalArgumentException(
                        "codeTemplate must contain a placeholder. codeTemplate: " + codeTemplate);
            }
            String placeholder = matcher.group();
            if (matcher.find()) {
                throw new IllegalArgumentException(
                        "codeTemplate must contain only one placeholder. codeTemplate: " + codeTemplate);
            }
            return placeholder;
        }

        public String code(T key) {
            return template.replace(keyPlaceholder, key.toString());
        }

        public String key(String code) {
            int codeKeyIndex = template.indexOf(keyPlaceholder);
            String prefix = template.substring(0, codeKeyIndex);
            String suffix = template.substring(codeKeyIndex + keyPlaceholder.length());
            if (code.startsWith(prefix) && code.endsWith(suffix)) {
                return code.substring(prefix.length(), code.length() - suffix.length());
            } else {
                throw new IllegalArgumentException("코드가 올바르지 않습니다. code: " + code);
            }
        }
    }
}
