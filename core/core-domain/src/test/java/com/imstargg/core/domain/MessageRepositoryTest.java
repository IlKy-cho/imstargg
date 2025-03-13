package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.MessageJpaRepository;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MessageRepositoryTest extends AbstractDataJpaTest {

    private MessageRepository messageRepository;

    @Autowired
    private MessageJpaRepository messageJpaRepository;

    @Autowired
    private MessageCollectionJpaRepository messageCollectionJpaRepository;

    @BeforeEach
    void setUp() {
        messageRepository = new MessageRepository(messageJpaRepository);
    }


    @Test
    void 메시지_컬렉션_조회() {
        // given
        String code = "TEST_CODE";
        messageCollectionJpaRepository.saveAll(List.of(
                new MessageCollectionEntity(code, Language.KOREAN, "한글 메시지"),
                new MessageCollectionEntity(code, Language.ENGLISH, "English message")
        ));

        // when
        MessageCollection collection = messageRepository.getCollection(code);

        // then
        assertThat(collection.code()).isEqualTo(code);
        assertThat(collection.messages())
                .hasSize(2)
                .containsEntry(Language.KOREAN, new Message(Language.KOREAN, "한글 메시지"))
                .containsEntry(Language.ENGLISH, new Message(Language.ENGLISH, "English message"));
    }

    @Test
    void 메시지가_존재하지_않는_컬렉션_조회() {
        // given
        String code = "NON_EXISTENT_CODE";

        // when
        MessageCollection collection = messageRepository.getCollection(code);

        // then
        assertThat(collection.code()).isEqualTo(code);
        assertThat(collection.messages()).isEmpty();
    }

    @Test
    void 메시지_컬렉션_리스트_조회() {
        // given
        String code1 = "CODE_1";
        String code2 = "CODE_2";
        String code3 = "CODE_3";
        messageCollectionJpaRepository.saveAll(List.of(
                new MessageCollectionEntity(code1, Language.ENGLISH, "First message"),
                new MessageCollectionEntity(code1, Language.KOREAN, "첫번째 메시지"),
                new MessageCollectionEntity(code2, Language.KOREAN, "두번째 메시지")
        ));

        // when
        List<MessageCollection> collections = messageRepository.getCollectionList(List.of(code1, code2, code3));

        // then
        assertThat(collections).hasSize(3);
        assertThat(collections.get(0).code()).isEqualTo(code1);
        assertThat(collections.get(0).messages())
                .hasSize(2)
                .containsEntry(Language.KOREAN, new Message(Language.KOREAN, "첫번째 메시지"))
                .containsEntry(Language.ENGLISH, new Message(Language.ENGLISH, "First message"))
                ;
        assertThat(collections.get(1).code()).isEqualTo(code2);
        assertThat(collections.get(1).messages())
                .hasSize(1)
                .containsEntry(Language.KOREAN, new Message(Language.KOREAN, "두번째 메시지"))
                ;
        assertThat(collections.get(2).code()).isEqualTo(code3);
        assertThat(collections.get(2).messages())
                .describedAs("메시지가 없는 컬렉션은 빈 리스트로 반환되어야 한다.")
                .isEmpty();
    }
}
