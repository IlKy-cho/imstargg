package com.imstargg.storage.db.support;

public class LongSetToStringConverter extends AbstractSetToStringConverter<Long> {

    @Override
    protected String convertToString(Long element) {
        return element.toString();
    }

    @Override
    protected Long convertToEntity(String element) {
        return Long.parseLong(element);
    }
}
