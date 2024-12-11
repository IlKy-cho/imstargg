package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageCollection {

    private final String code;

    private final EnumMap<Language, Message> messages;

    public MessageCollection(String code, List<Message> messages) {
        this.code = code;
        this.messages = new EnumMap<>(messages.stream().collect(Collectors.toMap(
                Message::language, Function.identity()
        )));
    }

    public String getCode() {
        return code;
    }

    public Optional<Message> find(Language language) {
        return Optional.ofNullable(messages.get(language))
                .or(() -> Optional.ofNullable(messages.get(Language.DEFAULT)));
    }
}
