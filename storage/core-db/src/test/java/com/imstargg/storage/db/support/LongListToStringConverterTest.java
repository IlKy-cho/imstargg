package com.imstargg.storage.db.support;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LongListToStringConverterTest {

    @Test
    void attribute가_null일_경우_칼럼값_null을_반환한다() {
        // given
        LongListToStringConverter converter = new LongListToStringConverter();

        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void attribute가_비어있을_경우_칼럼값_null을_반환한다() {
        // given
        LongListToStringConverter converter = new LongListToStringConverter();

        // when
        String result = converter.convertToDatabaseColumn(List.of());

        // then
        assertThat(result).isNull();
    }

    @Test
    void attribute의_리스트값들을_콤마로_구분하여_문자열로_변환한다() {
        // given
        LongListToStringConverter converter = new LongListToStringConverter();

        // when
        String result = converter.convertToDatabaseColumn(List.of(1L, 2L, 3L));

        // then
        assertThat(result).isEqualTo("[1,2,3]");
    }

    @Test
    void dbData가_null일_경우_빈_리스트를_반환한다() {
        // given
        LongListToStringConverter converter = new LongListToStringConverter();

        // when
        List<Long> result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void dbData가_비어있을_경우_빈_리스트를_반환한다() {
        // given
        LongListToStringConverter converter = new LongListToStringConverter();

        // when
        List<Long> result = converter.convertToEntityAttribute("[]");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void dbData를_콤마로_구분하여_리스트로_변환한다() {
        // given
        LongListToStringConverter converter = new LongListToStringConverter();

        // when
        List<Long> result = converter.convertToEntityAttribute("[1,2,3]");

        // then
        assertThat(result).containsExactly(1L, 2L, 3L);
    }
}