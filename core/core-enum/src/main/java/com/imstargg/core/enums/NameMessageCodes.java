package com.imstargg.core.enums;

import java.util.regex.Pattern;

public abstract class NameMessageCodes {

    public static final NameMessageCode<String> BATTLE_MAP = new NameMessageCode<>("maps.{mapBrawlStarsName}.name");


    private NameMessageCodes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class NameMessageCode<T> {
        
        private final String codeTemplate;
        private final String codeKeyPlaceholder;

        private NameMessageCode(String codeTemplate) {
            this.codeTemplate = codeTemplate;
            this.codeKeyPlaceholder = extractCodeKeyPlaceholder(codeTemplate);
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
            return codeTemplate.replace(codeKeyPlaceholder, key.toString());
        }

        public String key(String code) {
            int codeKeyIndex = codeTemplate.indexOf(codeKeyPlaceholder);
            String prefix = codeTemplate.substring(0, codeKeyIndex);
            String suffix = codeTemplate.substring(codeKeyIndex + codeKeyPlaceholder.length());
            if (code.startsWith(prefix) && code.endsWith(suffix)) {
                return code.substring(prefix.length(), code.length() - suffix.length());
            } else {
                throw new IllegalArgumentException("코드가 올바르지 않습니다. code: " + code);
            }
        }
    }
}
