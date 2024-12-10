package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.MessageEntity;
import com.imstargg.storage.db.core.MessageJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class MessageRepository {

    private final MessageJpaRepository messageJpaRepository;

    public MessageRepository(MessageJpaRepository messageJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
    }

    public MessageCollection getCollection(String code) {
        return new MessageCollection(
                code,
                messageJpaRepository.findAllByCode(code)
                        .stream()
                        .map(entity -> new Message(Language.of(entity.getLang()), entity.getContent()))
                        .toList()
        );
    }

    public List<MessageCollection> getCollectionList(List<String> codes) {
        Map<String, List<MessageEntity>> codeToMessageEntities = messageJpaRepository.findAllByCodeIn(codes).stream()
                .collect(groupingBy(MessageEntity::getCode));

        return codes.stream()
                .map(code -> new MessageCollection(
                        code,
                        codeToMessageEntities.getOrDefault(code, List.of()).stream()
                                .map(entity ->
                                        new Message(Language.of(entity.getLang()), entity.getContent()))
                                .toList()
                )).toList();
    }
}
