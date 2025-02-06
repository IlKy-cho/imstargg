package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.MessageEntity;
import com.imstargg.storage.db.core.MessageJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class MessageRepository {

    private final MessageJpaRepository messageJpaRepository;

    public MessageRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    public Message get(String code, Language language) {
        return messageJpaRepository.findByCodeAndLang(code, language)
                .map(entity -> new Message(entity.getLang(), entity.getContent()))
                .or(() -> messageJpaRepository.findByCodeAndLang(code, Language.DEFAULT)
                        .map(entity -> new Message(entity.getLang(), entity.getContent()))
                ).orElseThrow(() -> new CoreException("메시지를 찾을 수 없습니다. code: " + code + ", language: " + language));
    }

    public MessageCollection getCollection(String code) {
        return new MessageCollection(
                code,
                messageJpaRepository.findAllByCode(code)
                        .stream()
                        .map(entity -> new Message(entity.getLang(), entity.getContent()))
                        .toList()
        );
    }

    public List<MessageCollection> getCollectionList(Collection<String> codes) {
        Map<String, List<MessageEntity>> codeToMessageEntities = messageJpaRepository.findAllByCodeIn(codes).stream()
                .collect(groupingBy(MessageEntity::getCode));

        return codes.stream()
                .map(code -> new MessageCollection(
                        code,
                        codeToMessageEntities.getOrDefault(code, List.of()).stream()
                                .map(entity ->
                                        new Message(entity.getLang(), entity.getContent()))
                                .toList()
                )).toList();
    }
}
