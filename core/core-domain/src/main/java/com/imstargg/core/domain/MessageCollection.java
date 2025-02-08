package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public record MessageCollection(
        String code,
        Map<Language, Message> messages
) {

    public static MessageCollection newDefault(String code, String content) {
        return new MessageCollection(code, List.of(new Message(Language.DEFAULT, content)));
    }

    public MessageCollection(String code, List<Message> messages) {
        this(
                code,
                messages.stream().collect(Collectors.toMap(
                        Message::language, Function.identity()
                ))
        );
    }

    public Optional<Message> find(Language language) {
        return Optional.ofNullable(messages.get(language))
                .or(() -> Optional.ofNullable(messages.get(Language.DEFAULT)));
    }

    public MessageCollection defaultMessage(String content) {
        if (!messages.isEmpty()) {
            return this;
        }

        return new MessageCollection(code, List.of(new Message(Language.DEFAULT, content)));
    }
}
