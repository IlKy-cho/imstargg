package com.imstargg.storage.db.support;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSetToStringConverter<T> implements AttributeConverter<Set<T>, String> {

    @Override
    public String convertToDatabaseColumn(Set<T> attribute) {
        if (attribute == null) {
            return null;
        }

        return "[" +
                attribute.stream()
                        .sorted()
                        .map(T::toString)
                        .collect(Collectors.joining(","))
                + "]";
    }

    protected abstract String convertToString(T element);

    @Override
    public Set<T> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String data = dbData.substring(1, dbData.length() - 1);
        if (data.isEmpty()) {
            return Set.of();
        }

        return Arrays.stream(data.split(","))
                .map(this::convertToEntity)
                .collect(Collectors.toSet());
    }

    protected abstract T convertToEntity(String element);
}
