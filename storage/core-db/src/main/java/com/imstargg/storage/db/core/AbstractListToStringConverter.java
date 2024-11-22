package com.imstargg.storage.db.core;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractListToStringConverter<T> implements AttributeConverter<List<T>, String> {

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        if (attribute == null) {
            return null;
        }

        return "[" +
                attribute.stream()
                        .map(T::toString)
                        .collect(Collectors.joining(","))
                + "]";
    }

    protected abstract String convertToString(T element);

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String data = dbData.substring(1, dbData.length() - 1);
        if (data.isEmpty()) {
            return List.of();
        }

        return Arrays.stream(data.split(","))
                .map(this::convertToEntity)
                .toList();
    }

    protected abstract T convertToEntity(String element);
}
