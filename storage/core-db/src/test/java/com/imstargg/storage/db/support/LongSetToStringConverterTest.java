package com.imstargg.storage.db.support;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LongSetToStringConverterTest {

    @Test
    void attribute가_null일_경우_칼럼값_null을_반환한다() {
        // given
        LongSetToStringConverter converter = new LongSetToStringConverter();

        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void attribute가_비어있을_경우_칼럼값_빈리스트를_반환한다() {
        // given
        LongSetToStringConverter converter = new LongSetToStringConverter();

        // when
        String result = converter.convertToDatabaseColumn(Set.of());

        // then
        assertThat(result).isEqualTo("[]");
    }

    @Test
    void attribute의_리스트값들을_정렬_후_콤마로_구분하여_문자열로_변환한다() {
        // given
        LongSetToStringConverter converter = new LongSetToStringConverter();

        // when
        String result = converter.convertToDatabaseColumn(Set.of(5L, 3L, 4L, 1L ,2L));

        // then
        assertThat(result).isEqualTo("[1,2,3,4,5]");
    }

    @Test
    void dbData가_null일_경우_빈_리스트를_반환한다() {
        // given
        LongSetToStringConverter converter = new LongSetToStringConverter();

        // when
        Set<Long> result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void dbData가_비어있을_경우_빈_리스트를_반환한다() {
        // given
        LongSetToStringConverter converter = new LongSetToStringConverter();

        // when
        Set<Long> result = converter.convertToEntityAttribute("[]");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void dbData를_콤마로_구분하여_셋으로_변환한다() {
        // given
        LongSetToStringConverter converter = new LongSetToStringConverter();

        // when
        Set<Long> result = converter.convertToEntityAttribute("[1,2,3,4,5]");

        // then
        assertThat(result).containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L);
    }
}