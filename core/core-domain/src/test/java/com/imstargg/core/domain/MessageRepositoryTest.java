package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreException;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.MessageJpaRepository;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void 메시지_조회_성공() {
        // given
        String code = "TEST_CODE";
        String content = "테스트 메시지";
        messageCollectionJpaRepository.save(new MessageCollectionEntity(code, Language.KOREAN, content));

        // when
        Message message = messageRepository.get(code, Language.KOREAN);

        // then
        assertThat(message.language()).isEqualTo(Language.KOREAN);
        assertThat(message.content()).isEqualTo(content);
    }

    @Test
    void 요청_언어가_없으면_기본언어_메시지_반환() {
        // given
        String code = "TEST_CODE";
        String defaultContent = "기본 메시지";
        messageCollectionJpaRepository.save(new MessageCollectionEntity(code, Language.DEFAULT, defaultContent));

        // when
        Message message = messageRepository.get(code, Language.KOREAN);

        // then
        assertThat(message.language()).isEqualTo(Language.DEFAULT);
        assertThat(message.content()).isEqualTo(defaultContent);
    }

    @Test
    void 메시지가_없으면_예외_발생() {
        // given
        String code = "NOT_EXIST_CODE";

        // when & then
        assertThatThrownBy(() -> messageRepository.get(code, Language.KOREAN))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorType", CoreErrorType.DEFAULT_ERROR);
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
                .containsKeys(Language.KOREAN, Language.ENGLISH);
    }

    @Test
    void 메시지_컬렉션_리스트_조회() {
        // given
        String code1 = "CODE_1";
        String code2 = "CODE_2";
        messageCollectionJpaRepository.saveAll(List.of(
                new MessageCollectionEntity(code1, Language.KOREAN, "첫번째 한글 메시지"),
                new MessageCollectionEntity(code2, Language.KOREAN, "두번째 한글 메시지")
        ));

        // when
        List<MessageCollection> collections = messageRepository.getCollectionList(List.of(code1, code2));

        // then
        assertThat(collections).hasSize(2);
        assertThat(collections.get(0).code()).isEqualTo(code1);
        assertThat(collections.get(1).code()).isEqualTo(code2);
        assertThat(collections)
                .extracting(MessageCollection::code)
                .containsExactly(code1, code2);
    }
}