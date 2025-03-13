package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCollectionTest {

    @Test
    void 리스트로_메시지컬렉션_생성() {
        // given
        String code = "TEST_CODE";
        Message koMessage = new Message(Language.KOREAN, "한글 메시지");
        Message enMessage = new Message(Language.ENGLISH, "English message");
        
        // when
        MessageCollection collection = new MessageCollection(code, List.of(koMessage, enMessage));
        
        // then
        assertThat(collection.code()).isEqualTo(code);
        assertThat(collection.messages())
                .hasSize(2)
                .containsEntry(Language.KOREAN, koMessage)
                .containsEntry(Language.ENGLISH, enMessage);
    }

    @Test
    void 존재하는_언어로_메시지_찾기() {
        // given
        Message koMessage = new Message(Language.KOREAN, "한글 메시지");
        MessageCollection collection = new MessageCollection("TEST", List.of(koMessage));
        
        // when
        Optional<Message> found = collection.find(Language.KOREAN);
        
        // then
        assertThat(found)
                .isPresent()
                .contains(koMessage);
    }

    @Test
    void 존재하지_않는_언어는_기본언어_반환() {
        // given
        Message defaultMessage = new Message(Language.DEFAULT, "기본 메시지");
        MessageCollection collection = new MessageCollection("TEST", List.of(defaultMessage));
        
        // when
        Optional<Message> found = collection.find(Language.KOREAN);
        
        // then
        assertThat(found)
                .isPresent()
                .contains(defaultMessage);
    }

    @Test
    void 존재하지_않는_언어이고_기본언어도_없으면_빈_옵셔널_반환() {
        // given
        Message koMessage = new Message(Language.KOREAN, "한글 메시지");
        MessageCollection collection = new MessageCollection("TEST", List.of(koMessage));
        
        // when
        Optional<Message> found = collection.find(Language.ENGLISH);
        
        // then
        assertThat(found).isEmpty();
    }

    @Test
    void 기본메시지_생성() {
        // given
        String code = "TEST_CODE";
        String content = "기본 메시지";

        // when
        MessageCollection collection = MessageCollection.newDefault(code, content);

        // then
        assertThat(collection.code()).isEqualTo(code);
        assertThat(collection.messages())
                .hasSize(1)
                .containsEntry(Language.DEFAULT, new Message(Language.DEFAULT, content));
    }

    @Test
    void 빈_컬렉션의_경우_기본_메시지를_설정할_수_있다() {
        // given
        String code = "TEST_CODE";
        String content = "기본 메시지";
        MessageCollection collection = new MessageCollection(code, List.of());

        // when
        MessageCollection updatedCollection = collection.defaultMessage(content);

        // then
        assertThat(updatedCollection.code()).isEqualTo(code);
        assertThat(updatedCollection.messages())
                .hasSize(1)
                .containsEntry(Language.DEFAULT, new Message(Language.DEFAULT, content));
    }

    @Test
    void 컬렉션이_비어있지_않을_경우_기본_메시지를_설정할_수_없다() {
        // given
        String code = "TEST_CODE";
        String content = "기본 메시지";
        MessageCollection collection = new MessageCollection(code, List.of(new Message(Language.KOREAN, "한글 메시지")));

        // when
        MessageCollection updatedCollection = collection.defaultMessage(content);

        // then
        assertThat(updatedCollection.code()).isEqualTo(code);
        assertThat(updatedCollection.messages())
                .hasSize(1)
                .containsEntry(Language.KOREAN, new Message(Language.KOREAN, "한글 메시지"));
    }
}